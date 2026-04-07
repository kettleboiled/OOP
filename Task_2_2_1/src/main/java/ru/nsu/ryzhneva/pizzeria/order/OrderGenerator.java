package ru.nsu.ryzhneva.pizzeria.order;

import java.util.concurrent.atomic.AtomicInteger;

import ru.nsu.ryzhneva.pizzeria.queue.ProducerQueue;
import ru.nsu.ryzhneva.pizzeria.queue.QueueClosedException;

/**
 * Рабочий поток-генератор (Producer).
 */
public class OrderGenerator implements Runnable {
    private final ProducerQueue<Order> orders;
    private final int generationSpeedMs;
    private final AtomicInteger orderIdCreator = new AtomicInteger(1);
    private final OrderStateListener stateListener;

    /**
     * Конструктор.
     *
     * @param orders потокобезопасная очередь.
     * @param generationSpeedMs период создания новых заказов.
     * @param stateListener состояние из наблюдателя.
     */
    public OrderGenerator(ProducerQueue<Order> orders, int generationSpeedMs, 
                          OrderStateListener stateListener) {
        this.orders = orders;
        this.generationSpeedMs = generationSpeedMs;
        this.stateListener = stateListener;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                orders.put(new Order(orderIdCreator.getAndIncrement(), stateListener));
                Thread.sleep(generationSpeedMs);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (QueueClosedException e) {
            System.out.println("Order generation stopped: " + e.getMessage());
        }
    }
}