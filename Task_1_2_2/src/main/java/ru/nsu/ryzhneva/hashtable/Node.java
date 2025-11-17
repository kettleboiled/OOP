package ru.nsu.ryzhneva.hashtable;

import java.util.Objects;

/**
 * Узел хранения данных.
 */
public class Node<K, V> implements Entry<K, V> {
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
    public Node(K key, V value, int hash, Node<K, V> next) {
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
        if (o instanceof Entry) {
            Entry<?, ?> e = (Entry<?, ?>) o;
            return Objects.equals(key, e.getKey())
                    && Objects.equals(value, e.getValue());
        }
        return false;
    }
}
