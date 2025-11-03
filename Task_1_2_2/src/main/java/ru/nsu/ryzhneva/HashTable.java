package ru.nsu.ryzhneva;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Реализация параметризованной хеш-таблицы.
 *
 * @param <K> Тип ключа.
 * @param <V> Тип значения.
 */
public class HashTable<K, V> implements Iterable<HashTable.Entry<K, V>> {
    /**
     * Внутренний интерфейс для представления пары ключ-значение.
     *
     * @param <K> Тип ключа.
     * @param <V> Тип значения.
     */
    public interface Entry<K, V> {
        K getKey();
        V getValue();
    }

    /**
     * Внутренний узел хранения данных.
     */
    private static class Node<K, V> implements HashTable.Entry<K, V> {
        final K key;
        V value;
        final int hash;
        Node<K, V> next;

        /**
         * Конструктор узла.
         *
         * @param key   Ключ.
         * @param value Значение.
         * @param hash  Предварительно рассчитанный хеш ключа.
         * @param next  Следующий узел в связном списке.
         */
        Node(K key, V value, int hash, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public final String toString() {
            return key + "=" + value;
        }

        @Override
        public final boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o instanceof HashTable.Entry) {
                HashTable.Entry<?, ?> e = (HashTable.Entry<?, ?>) o;
                return Objects.equals(key, e.getKey())
                        && Objects.equals(value, e.getValue());
            }
            return false;
        }
    }

    private Node<K, V>[] table;
    private int size = 0;
    private int capTable;
    private int modCount = 0;
    private static final int DEF_INIT_CAP = 16;
    private static final float LOAD_FACTOR = 0.75f;

    /**
     * Создает пустую хеш-таблицу с начальной емкостью
     * {@value #DEF_INIT_CAP}.
     */
    public HashTable() {
        this.capTable = DEF_INIT_CAP;
        this.table = (Node<K, V>[]) new Node[capTable];
    }
    /**
     * Рассчитывает хеш-код для ключа.
     *
     * @param key Ключ для хеширования.
     * @return Улучшенный хеш-код.
     */
    private int hash(K key) {
        if (key == null) {
            return 0;
        }
        int h = key.hashCode();
        return (h ^ (h >>> 16));
    }

    /**
     * Определяет индекс ячейки в массиве {@code table} по хешу.
     *
     * @param hash Хеш-код ключа.
     * @return Индекс в массиве (0...capTable-1).
     */
    private int getIndex(int hash) {
        return (capTable - 1) & hash;
    }

    /**
     * Находит узел по ключу.
     *
     * @param key Ключ для поиска.
     * @return Найденный {@link Node} или {@code null}, если ключ отсутствует.
     */
    private Node<K, V> findNode(K key) {
        int hash = hash(key);
        int index = getIndex(hash);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (node.hash == hash && Objects.equals(node.key, key)) {
                return node;
            }
        }
        return null;
    }

    /**
     * Увеличивает размер таблицы.
     */
    private void resize() {
        int prevCap = capTable;
        Node<K, V>[] prevTable = table;

        capTable = prevCap << 1;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[capTable];
        for (int i = 0; i < prevCap; i++) {
            Node<K, V> node = prevTable[i];
            while (node != null) {
                Node<K, V> next = node.next;
                int index = getIndex(node.hash);
                node.next = newTable[index];
                newTable[index] = node;

                node = next;
            }
        }
        table = newTable;
    }

    /**
     * Добавляет пару ключ-значение в таблицу.
     * Если ключ уже существует, обновляет его значение.
     *
     * @param key   Ключ.
     * @param value Значение.
     * @return Предыдущее значение.
     */
    public V put(K key, V value) {
        int hash = hash(key);
        int index = getIndex(hash);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (node.hash == hash && Objects.equals(node.key, key)) {
                V prevValue = node.value;
                node.value = value;
                return prevValue;
            }
        }
        Node<K, V> newNode = new Node<>(key, value, hash, table[index]);
        table[index] = newNode;
        size++;
        modCount++;
        if ((float) size / capTable > LOAD_FACTOR) {
            resize();
        }
        return null;
    }

    /**
     * Возвращает значение по ключу.
     *
     * @param key Ключ для поиска.
     * @return Значение, связанное с ключом.
     */
    public V get(K key) {
        Node<K, V> node = findNode(key);
        return (node != null) ? node.value : null;
    }

    /**
     * Обновляет значение для существующего ключа.
     *
     * @param key   Ключ, значение которого нужно обновить.
     * @param value Новое значение.
     * @return Старое значение.
     */
    public V update(K key, V value) {
        Node<K, V> node = findNode(key);
        if (node != null) {
            V prevValue = node.value;
            node.value = value;
            return prevValue;
        }
        return null;
    }

    /**
     * Проверяет наличие ключа в таблице.
     *
     * @param key Ключ для проверки.
     * @return {@code true}, если ключ найден, иначе {@code false}.
     */
    public boolean check(K key) {
        return findNode(key) != null;
    }

    /**
     * Удаляет пару ключ-значение.
     *
     * @param key Ключ для удаления.
     * @return Удаленное значение.
     */
    public V remove(K key) {
        int hash = hash(key);
        int index = getIndex(hash);

        Node<K, V> node = table[index];
        Node<K, V> prev = null;

        while (node != null) {
            if (node.hash == hash && Objects.equals(node.key, key)) {
                if (prev == null) {
                    table[index] = node.next;
                } else {
                    prev.next = node.next;
                }
                size--;
                modCount++;
                return node.value;
            }
            prev = node;
            node = node.next;
        }
        return null;
    }

    /**
     * Возвращает итератор по всем ключ-значение.
     * парам в хеш-таблице.
     *
     * @return Итератор {@link Entry}
     */
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new HashIterator();
    }

    /**
     * Внутренний класс итератора.
     */
    private class HashIterator implements Iterator<Entry<K, V>> {
        private int currentCell;
        private int expectedModCount;
        private Node<K, V> nextNode;

        /**
         * Конструктор итератора.
         */
        HashIterator() {
            this.expectedModCount = modCount;
            this.currentCell = 0;
            this.nextNode = null;
            findFirstNode();
        }

        /**
         * Ищет первый узел в таблице.
         */
        private void findFirstNode() {
            while (currentCell < capTable) {
                if (table[currentCell] != null) {
                    nextNode = table[currentCell];
                    return;
                }
                currentCell++;
            }
        }

        /**
         * Ищет следующий узел для итерации.
         */
        private void findNextNode() {
            if (nextNode != null && nextNode.next != null) {
                nextNode = nextNode.next;
                return;
            }
            currentCell++;
            while (currentCell < capTable) {
                if (table[currentCell] != null) {
                    nextNode = table[currentCell];
                    return;
                }
                currentCell++;
            }
            nextNode = null;
        }

        @Override
        public boolean hasNext() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException(
                        "The table was modified during the iteration.");
            }
            return nextNode != null;
        }

        @Override
        public Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException("There are no elements.");
            }
            Node<K, V> nodeToReturn = nextNode;
            findNextNode();
            return nodeToReturn;
        }
    }

    /**
     * Сравнивает эту хеш-таблицу с другой.
     *
     * @param o Объект для сравнения.
     * @return {@code true}, если таблицы равны, иначе {@code false}.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HashTable)) {
            return false;
        }

        HashTable<K, V> otherTable = (HashTable<K, V>) o;

        if (otherTable.size != this.size) {
            return false;
        }

        try {
            for (Entry<K, V> entry : this) {
                K key = entry.getKey();
                V value = entry.getValue();
                Object otherTableValue = otherTable.get(key);

                if (!Objects.equals(value, otherTableValue)) {
                    return false;
                }
                if (value == null && !otherTable.check(key)) {
                    return false;
                }
            }
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    /**
     * Возвращает строковое представление хеш-таблицы в формате {k1=v1, k2=v2}.
     *
     * @return Строковое представление.
     */
    @Override
    public String toString() {
        if (size == 0) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");

        Iterator<Entry<K, V>> iter = iterator();
        while (iter.hasNext()) {
            Entry<K, V> entry = iter.next();
            sb.append(entry.getKey()).append("=").append(entry.getValue());
            if (iter.hasNext()) {
                sb.append(", ");
            }
        }

        sb.append("}");
        return sb.toString();
    }
}