package ru.nsu.ryzhneva.pizzeria.workers;

import java.util.List;
import ru.nsu.ryzhneva.pizzeria.order.Order;
import ru.nsu.ryzhneva.pizzeria.queue.BatchConsumerQueue;
import ru.nsu.ryzhneva.pizzeria.queue.QueueClosedException;

/**
 * Рабочий поток (Worker), реализующий логику курьера.
 * Поддерживает обработку нескольких заказов
 * на основе доступного объема багажника.
 */
public class Courier implements Runnable {
    private final int trunkVolume;
    private final int speedCourier;
    private final BatchConsumerQueue<Order> warehouse;

    /**
     * Конструктор.
     *
     * @param trunkVolume вместимость багажника.
     * @param speedCourier скорость доставки курьера.
     * @param warehouse очередь склада.
     */
    public Courier(int trunkVolume, int speedCourier,
                   BatchConsumerQueue<Order> warehouse) {
        this.trunkVolume = trunkVolume;
        this.speedCourier = speedCourier;
        this.warehouse = warehouse;
    }

    @Override
    public void run() {
        List<Order> orders = null;
        try {
            while (!Thread.currentThread().isInterrupted()) {
                orders = warehouse.getBatch(trunkVolume);

                for (Order order : orders) {
                    order.advanceState();
                }

                Thread.sleep(speedCourier);

                for (Order order : orders) {
                    order.advanceState();
                }
                orders = null;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            if (orders != null) {
                for (Order order : orders) {
                    order.advanceState();
                }
            }
        } catch (QueueClosedException e) {
        }
    }
}
