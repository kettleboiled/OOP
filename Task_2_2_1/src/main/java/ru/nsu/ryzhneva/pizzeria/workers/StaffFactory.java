package ru.nsu.ryzhneva.pizzeria.workers;

import ru.nsu.ryzhneva.pizzeria.PizzeriaConfig;
import ru.nsu.ryzhneva.pizzeria.order.Order;
import ru.nsu.ryzhneva.pizzeria.queue.BatchConsumerQueue;
import ru.nsu.ryzhneva.pizzeria.queue.ConsumerQueue;
import ru.nsu.ryzhneva.pizzeria.queue.ProducerQueue;

/**
 * Фабрика для сборки групп сотрудников.
 */
public class StaffFactory {

    /**
     * Создает группу пекарей.
     *
     * @param config конфигурационный файл.
     * @param orders очередь заказов.
     * @param warehouse очередь склада.
     * @return группу иниуиализированных пекарей
     */
    public static WorkerGroup createBakers(PizzeriaConfig config,
                                           ConsumerQueue<Order> orders,
                                           ProducerQueue<Order> warehouse) {
        WorkerGroup group = new WorkerGroup();
        int[] speeds = config.bakerSpeeds();
        for (int i = 0; i < config.bakersCount(); i++) {
            int speed = speeds[i % speeds.length];
            group.addAndStart(new Baker(speed, orders, warehouse));
        }
        return group;
    }

    /**
     * Создает группу курьеров.
     *
     * @param config конфигурационный файл.
     * @param warehouse очередь склада.
     * @return группу инициализированных курьеров.
     */
    public static WorkerGroup createCouriers(PizzeriaConfig config,
                                             BatchConsumerQueue<Order> warehouse) {
        WorkerGroup group = new WorkerGroup();
        int[] volumes = config.couriersTrunkVolume();
        int speed = config.courierSpeed();
        for (int i = 0; i < config.couriersCount(); i++) {
            int volume = volumes[i % volumes.length];
            group.addAndStart(new Courier(volume, speed, warehouse));
        }
        return group;
    }
}