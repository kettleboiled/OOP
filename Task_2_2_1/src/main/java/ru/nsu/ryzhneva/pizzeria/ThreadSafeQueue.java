package ru.nsu.ryzhneva.pizzeria;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Потокобезопасная очередь.
 *
 * @param <T> тип хранимых элементов.
 */
public class ThreadSafeQueue<T> {
    private final int count;
    private final Queue<T> queue = new LinkedList<>();
    private volatile boolean isClosed = false;

    /**
     * Конструктор.
     *
     * @param count максимальное количество элементов в очереди.
     */
    public ThreadSafeQueue(int count) {
        this.count = count;
    }

    /**
     * Добавить элемент в очередь.
     *
     * @param item элемент для добавления.
     * @throws InterruptedException если поток был прерван
     * во время ожидания освобождения места.
     */
    public synchronized void put(T item) throws InterruptedException {
        while (queue.size() >= count) {
            wait();
        }
        queue.add(item);
        notifyAll();
    }

    /**
     * Получить элемент (один) из очереди.
     *
     * @return извлеченный элемент, либо {@code null},
     * если очередь пуста и переведена в закрытое состояние.
     * @throws InterruptedException если поток был
     * прерван во время ожидания новых элементов.
     */
    public synchronized T get() throws InterruptedException {
        while (queue.isEmpty()) {
            if (isClosed) {
                return null;
            }
            wait();
        }
        T item = queue.poll();
        notifyAll();
        return item;
    }

    /**
     * Получить список элементов из очереди.
     *
     * @param size вместимость багажника.
     * @return список извлеченных элементов.
     * @throws InterruptedException если поток был прерван во время ожидания.
     */
    public synchronized List<T> getForCourier(int size) throws InterruptedException {
        while (queue.isEmpty()) {
            if (isClosed) {
                return new ArrayList<>();
            }
            wait();
        }
        List<T> items = new ArrayList<>();
        while (!queue.isEmpty() && items.size() < size) {
            items.add(queue.poll());
        }
        notifyAll();
        return items;
    }

    /**
     * Возвращает текущее количество элементов в очереди.
     *
     * @return текущий размер очереди.
     */
    public synchronized int size() {
        return queue.size();
    }

    /**
     * Изменяет состояние монитора на закрытое.
     */
    public synchronized void close() {
        isClosed = true;
        notifyAll();
    }
}
