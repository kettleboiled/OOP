package ru.nsu.ryzhneva.pizzeria.queue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Потокобезопасная очередь
 * с ограничением в колчестве элементов.
 *
 * @param <T> тип хранимых элементов.
 */
public class BoundedThreadSafeQueue<T> 
        implements ProducerQueue<T>, ConsumerQueue<T>, BatchConsumerQueue<T>, CloseableQueue {
    private final int capacity;
    private final Queue<T> queue = new LinkedList<>();
    private boolean isClosed = false;

    /**
     * Конструктор.
     *
     * @param capacity максимальное количество элементов в очереди.
     */
    public BoundedThreadSafeQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        this.capacity = capacity;
    }

    @Override
    public synchronized void put(T item) throws InterruptedException, QueueClosedException {
        if (isClosed) {
            throw new QueueClosedException();
        }
        while (queue.size() >= capacity) {
            wait();
            if (isClosed) {
                throw new QueueClosedException();
            }
        }
        queue.add(item);
        notifyAll();
    }

    @Override
    public synchronized T get() throws InterruptedException, QueueClosedException {
        while (queue.isEmpty()) {
            if (isClosed) {
                throw new QueueClosedException();
            }
            wait();
        }
        T item = queue.poll();
        notifyAll();
        return item;
    }

    @Override
    public synchronized List<T> getBatch(int size) 
            throws InterruptedException, QueueClosedException {
        while (queue.isEmpty()) {
            if (isClosed) {
                throw new QueueClosedException();
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

    @Override
    public synchronized void close() {
        isClosed = true;
        notifyAll();
    }
}
