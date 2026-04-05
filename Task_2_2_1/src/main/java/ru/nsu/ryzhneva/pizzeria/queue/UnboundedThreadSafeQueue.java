package ru.nsu.ryzhneva.pizzeria.queue;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Потокобезопасная очередь
 * с неограниченым колчеством элементов.
 *
 * @param <T> тип хранимых элементов.
 */
public class UnboundedThreadSafeQueue<T> 
        implements ProducerQueue<T>, ConsumerQueue<T>, CloseableQueue {
    private final Queue<T> queue = new LinkedList<>();
    private boolean isClosed = false;

    @Override
    public synchronized void put(T item) throws InterruptedException, QueueClosedException {
        if (isClosed) {
            throw new QueueClosedException();
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
        return item;
    }

    @Override
    public synchronized void close() {
        isClosed = true;
        notifyAll();
    }
}