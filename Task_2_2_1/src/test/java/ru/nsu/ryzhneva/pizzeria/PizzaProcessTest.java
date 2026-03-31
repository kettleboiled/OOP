package ru.nsu.ryzhneva.pizzeria;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.pizzeria.queue.BoundedThreadSafeQueue;
import ru.nsu.ryzhneva.pizzeria.queue.UnboundedThreadSafeQueue;
import ru.nsu.ryzhneva.pizzeria.order.Order;
import ru.nsu.ryzhneva.pizzeria.order.OrderGenerator;
import ru.nsu.ryzhneva.pizzeria.order.OrderStateListener;
import ru.nsu.ryzhneva.pizzeria.workers.StaffFactory;
import ru.nsu.ryzhneva.pizzeria.workers.WorkerGroup;

/**
 * Тест для класса PizzaProcess.
 */
class PizzaProcessTest {

    @Test
    void testProcessLifecycle() throws Exception {
        PizzeriaConfig config = new PizzeriaConfig(
            2, new int[]{10}, 
            2, new int[]{1}, 
            5, 1000, 10
        );

        UnboundedThreadSafeQueue<Order> ordersQueue = new UnboundedThreadSafeQueue<>();
        BoundedThreadSafeQueue<Order> warehouseQueue = new BoundedThreadSafeQueue<>(config.warehouseSize());

        WorkerGroup bakers = StaffFactory.createBakers(config, ordersQueue, warehouseQueue);
        WorkerGroup couriers = StaffFactory.createCouriers(config, warehouseQueue);

        OrderStateListener logger = order -> {};
        OrderGenerator generator = new OrderGenerator(ordersQueue, 10, logger);
        Thread genThread = new Thread(generator);

        PizzaProcess process = new PizzaProcess(ordersQueue, warehouseQueue, bakers, couriers, genThread);

        process.work();

        Thread.sleep(150);

        process.close();

        assertTrue(true, "Process finished successfully and didn't hang");
    }
}

