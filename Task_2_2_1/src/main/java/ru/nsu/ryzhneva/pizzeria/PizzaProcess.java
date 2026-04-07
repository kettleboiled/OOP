package ru.nsu.ryzhneva.pizzeria;

import ru.nsu.ryzhneva.pizzeria.queue.CloseableQueue;
import ru.nsu.ryzhneva.pizzeria.workers.WorkerGroup;

/**
 * Управляющий класс - жизненный цикл симулятора пиццерии.
 */
public class PizzaProcess {
    private final CloseableQueue orders;
    private final CloseableQueue warehouse;

    private final WorkerGroup bakers;
    private final WorkerGroup couriers;
    private final Thread orderGeneratorThread;

    /**
     * Конструктор.
     *
     * @param orders очередь заказов.
     * @param warehouse очередь склада.
     * @param bakers группа пекарей.
     * @param couriers группа курьеров.
     * @param orderGeneratorThread поток генератора заказов.
     */
    public PizzaProcess(CloseableQueue orders,
                        CloseableQueue warehouse,
                        WorkerGroup bakers,
                        WorkerGroup couriers,
                        Thread orderGeneratorThread) {
        this.orders = orders;
        this.warehouse = warehouse;
        this.bakers = bakers;
        this.couriers = couriers;
        this.orderGeneratorThread = orderGeneratorThread;
    }

    /**
     * Метод запуска работы пиццерии.
     */
    public void work() {
        orderGeneratorThread.start();
        System.out.println("Pizzeria process coordination started.");
    }

    /**
     * Метод завершающий работу пиццерии.
     */
    public void close() {
        System.out.println("Stop accepting orders...");
        orderGeneratorThread.interrupt();

        orders.close();

        bakers.awaitTermination();
        System.out.println("All bakers finished. Closing warehouse...");

        warehouse.close();

        couriers.awaitTermination();
        System.out.println("All couriers finished.");
    }
}