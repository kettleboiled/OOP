package ru.nsu.ryzhneva.pizzeria.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.pizzeria.queue.ProducerQueue;

/**
 * Тест класса OrderGenerator.
 */
class OrderGeneratorTest {

    @Test
    void testGeneratorProducesOrders() throws InterruptedException {
        List<Order> producedOrders = new ArrayList<>();
        ProducerQueue<Order> fakeQueue = new ProducerQueue<Order>() {
            @Override
            public void put(Order item) {
                producedOrders.add(item);
            }
        };

        OrderStateListener listener = order -> {};
        OrderGenerator generator = new OrderGenerator(fakeQueue, 10, listener);
        
        Thread thread = new Thread(generator);
        thread.start();
        
        Thread.sleep(50);
        thread.interrupt();
        thread.join();
        
        assertTrue(producedOrders.size() >= 2, "Generator should have generated some orders");
        assertEquals(1, producedOrders.get(0).getId());
        assertEquals(2, producedOrders.get(1).getId());
    }
}

