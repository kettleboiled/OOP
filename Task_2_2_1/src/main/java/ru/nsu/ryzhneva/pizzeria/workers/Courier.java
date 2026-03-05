package ru.nsu.ryzhneva.pizzeria.workers;

import java.util.List;
import ru.nsu.ryzhneva.pizzeria.ThreadSafeQueue;
import ru.nsu.ryzhneva.pizzeria.order.Order;
import ru.nsu.ryzhneva.pizzeria.order.OrderState;


/**
 * Рабочий поток (Worker), реализующий логику курьера.
 * Поддерживает обработку нескольких заказов
 * на основе доступного объема багажника.
 */
public class Courier implements Runnable {
    private final int number;
    private final int trunkVolume;
    private final int speedCourier;
    private ThreadSafeQueue<Order> warehouse;

    /**
     * Конструктор.
     *
     * @param number номер курьера.
     * @param trunkVolume вместимость багажника.
     * @param speedCourier скорость доставки курьера.
     * @param warehouse очередь склада.
     */
    public Courier(int number, int trunkVolume, int speedCourier,
                 ThreadSafeQueue<Order> warehouse) {
        this.number = number;
        this.trunkVolume = trunkVolume;
        this.speedCourier = speedCourier;
        this.warehouse = warehouse;
    }

    /**
     * Основной цикл выполнения потока.
     * Ожидает появления готовой продукции на складе,
     * извлекает доступный пакет
     * и имитирует доставку с обновлением состояний заказа.
     * Завершает работу штатно при опустошении
     * и закрытии склада.
     */
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                List<Order> orders = warehouse.getBatch(trunkVolume);
                if (orders.isEmpty()) {
                    break;
                }
                for (Order order : orders) {
                    order.setState(OrderState.COMING);
                }
                Thread.sleep(speedCourier);

                for (Order order : orders) {
                    order.setState(OrderState.DELIVERED);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
