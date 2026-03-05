package ru.nsu.ryzhneva.pizzeria.workers;

import ru.nsu.ryzhneva.pizzeria.ThreadSafeQueue;
import ru.nsu.ryzhneva.pizzeria.order.Order;
import ru.nsu.ryzhneva.pizzeria.order.OrderState;

/**
 * Рабочий поток, реализующий логику пекаря.
 * Извлекает заказы из входной очереди,
 * имитирует задержку приготовления
 * и помещает готовый продукт на склад.
 */
public class Baker implements Runnable {
    private final int number;
    private final int speed;
    private final ThreadSafeQueue<Order> orders;
    private final ThreadSafeQueue<Order> warehouse;

    /**
     * Инициализирует контекст пекаря.
     *
     * @param number уникальный идентификатор пекаря
     * @param speed время приготовления одного заказа
     * @param orders разделяемый монитор входной очереди заказов
     * @param warehouse разделяемый монитор склада готовой продукции
     */
    public Baker(int number, int speed,
                 ThreadSafeQueue<Order> orders,
                 ThreadSafeQueue<Order> warehouse) {
        this.number = number;
        this.speed = speed;
        this.orders = orders;
        this.warehouse = warehouse;
    }

    /**
     * Основной цикл выполнения потока.
     */
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Order order = orders.get();
                if (order == null) {
                    break;
                }
                order.setState(OrderState.COOKING);
                Thread.sleep(speed);

                warehouse.put(order);
                order.setState(OrderState.READY_SELECT_COURIER);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
