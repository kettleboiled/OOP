package ru.nsu.ryzhneva.pizzeria.workers;

import ru.nsu.ryzhneva.pizzeria.order.Order;
import ru.nsu.ryzhneva.pizzeria.queue.ConsumerQueue;
import ru.nsu.ryzhneva.pizzeria.queue.ProducerQueue;
import ru.nsu.ryzhneva.pizzeria.queue.QueueClosedException;

/**
 * Рабочий поток, реализующий логику пекаря.
 * Извлекает заказы из входной очереди,
 * имитирует задержку приготовления
 * и помещает готовый продукт на склад.
 */
public class Baker implements Runnable {
    private final int speed;
    private final ConsumerQueue<Order> orders;
    private final ProducerQueue<Order> warehouse;

    /**
     * Инициализирует контекст пекаря.
     *
     * @param speed время приготовления одного заказа
     * @param orders разделяемый монитор входной очереди заказов
     * @param warehouse разделяемый монитор склада готовой продукции
     */
    public Baker(int speed,
                 ConsumerQueue<Order> orders,
                 ProducerQueue<Order> warehouse) {
        this.speed = speed;
        this.orders = orders;
        this.warehouse = warehouse;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Order order = orders.get();

                order.advanceState();
                Thread.sleep(speed);

                warehouse.put(order);
                order.advanceState();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (QueueClosedException e) {
        }
    }
}
