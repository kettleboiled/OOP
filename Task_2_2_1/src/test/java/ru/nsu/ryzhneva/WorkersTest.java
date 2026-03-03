package ru.nsu.ryzhneva;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.pizzeria.workers.Baker;
import ru.nsu.ryzhneva.pizzeria.workers.Courier;
import ru.nsu.ryzhneva.pizzeria.ThreadSafeQueue;
import ru.nsu.ryzhneva.pizzeria.order.Order;


/**
 * Тесты для классов Baker и Courier.
 */
class WorkersTest {

    @Test
    void testBakerProcessesOrderAndTerminates() {
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            ThreadSafeQueue<Order> orders = new ThreadSafeQueue<>(5);
            ThreadSafeQueue<Order> warehouse = new ThreadSafeQueue<>(5);

            Order testOrder = new Order(1);
            orders.put(testOrder);
            orders.close();

            Baker baker = new Baker(1, 100, orders, warehouse);
            Thread bakerThread = new Thread(baker);
            bakerThread.start();
            bakerThread.join(); // Ждем завершения

            assertEquals(1, warehouse.size(),
                    "На склад должна поступить одна пицца");
            Order processedOrder = warehouse.get();
            assertNotNull(processedOrder);
            assertEquals(1, processedOrder.getId());
        });
    }

    @Test
    void testCourierProcessesBatchAndTerminates() {
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            ThreadSafeQueue<Order> warehouse = new ThreadSafeQueue<>(5);
            warehouse.put(new Order(1));
            warehouse.put(new Order(2));
            warehouse.close();

            Courier courier = new Courier(1, 2, 100, warehouse);
            Thread courierThread = new Thread(courier);
            courierThread.start();
            courierThread.join();

            assertEquals(0, warehouse.size(),
                    "Курьер должен опустошить склад");
        });
    }
}