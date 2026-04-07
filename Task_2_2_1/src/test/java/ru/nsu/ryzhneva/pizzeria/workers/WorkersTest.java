package ru.nsu.ryzhneva.pizzeria.workers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.pizzeria.PizzeriaConfig;
import ru.nsu.ryzhneva.pizzeria.order.Order;
import ru.nsu.ryzhneva.pizzeria.order.OrderState;
import ru.nsu.ryzhneva.pizzeria.queue.BoundedThreadSafeQueue;
import ru.nsu.ryzhneva.pizzeria.queue.UnboundedThreadSafeQueue;

/**
 * Тесты для классов Baker, Courier и StaffFactory.
 */
class WorkersTest {

    @Test
    void testBaker() throws Exception {
        UnboundedThreadSafeQueue<Order> orders = new UnboundedThreadSafeQueue<>();
        BoundedThreadSafeQueue<Order> warehouse = new BoundedThreadSafeQueue<>(5);

        Order order = new Order(1, null);
        orders.put(order);

        Baker baker = new Baker(50, orders, warehouse);
        Thread thread = new Thread(baker);
        thread.start();

        Thread.sleep(100);
        thread.interrupt();
        thread.join();

        assertEquals(OrderState.READY_SELECT_COURIER, order.getState());
        assertEquals(order, warehouse.get());
    }

    @Test
    void testCourier() throws Exception {
        Order order1 = new Order(1, null);
        Order order2 = new Order(2, null);
        order1.advanceState();
        order1.advanceState();
        order2.advanceState();
        order2.advanceState();
        
        BoundedThreadSafeQueue<Order> warehouse = new BoundedThreadSafeQueue<>(5);
        warehouse.put(order1);
        warehouse.put(order2);

        Courier courier = new Courier(2, 50, warehouse);
        Thread thread = new Thread(courier);
        thread.start();

        Thread.sleep(150);
        thread.interrupt();
        thread.join();

        assertEquals(OrderState.DELIVERED, order1.getState());
        assertEquals(OrderState.DELIVERED, order2.getState());
    }
    
    @Test
    void testStaffFactory() {
        PizzeriaConfig config = new PizzeriaConfig(
            1, new int[]{100}, 
            1, new int[]{1}, 
            5, 1000, 50
        );

        UnboundedThreadSafeQueue<Order> orders = new UnboundedThreadSafeQueue<>();
        BoundedThreadSafeQueue<Order> warehouse = new BoundedThreadSafeQueue<>(5);

        WorkerGroup bakers = StaffFactory.createBakers(config, orders, warehouse);
        WorkerGroup couriers = StaffFactory.createCouriers(config, warehouse);

        assertNotNull(bakers);
        assertNotNull(couriers);
        
        orders.close();
        bakers.awaitTermination();
        warehouse.close();
        couriers.awaitTermination();
    }
}

