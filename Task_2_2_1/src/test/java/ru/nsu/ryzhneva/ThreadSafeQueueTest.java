package ru.nsu.ryzhneva;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.pizzeria.ThreadSafeQueue;


/**
 * Тесты для класса ThreadSafeQueueTest.
 */
class ThreadSafeQueueTest {

    private ThreadSafeQueue<Integer> queue;

    @BeforeEach
    void setUp() {
        queue = new ThreadSafeQueue<>(3);
    }

    @Test
    void testPutAndGetSingleThread() throws InterruptedException {
        queue.put(1);
        queue.put(2);

        assertEquals(2, queue.size(),
                "Размер очереди должен быть 2");
        assertEquals(1, queue.get(),
                "Элементы должны извлекаться по принципу FIFO");
        assertEquals(1, queue.size(),
                "Размер очереди должен уменьшиться");
    }

    @Test
    void testGetForCourierBatching() throws InterruptedException {
        queue.put(10);
        queue.put(20);
        queue.put(30);

        List<Integer> batch = queue.getForCourier(5);

        assertEquals(3, batch.size(),
                "Курьер должен забрать только доступные элементы");
        assertTrue(batch.containsAll(List.of(10, 20, 30)));
        assertEquals(0, queue.size(),
                "Очередь должна быть пуста после извлечения");
    }

    @Test
    void testCloseUnblocksWaitingConsumer() {
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            CountDownLatch threadStarted = new CountDownLatch(1);

            Thread consumer = new Thread(() -> {
                try {
                    threadStarted.countDown();
                    Integer result = queue.get();
                    assertNull(result,
                            "После закрытия пустой очереди метод get() должен возвращать null");
                } catch (InterruptedException e) {
                    fail("Поток не должен прерываться через Exception при корректном close()");
                }
            });

            consumer.start();

            threadStarted.await();
            Thread.sleep(100);

            queue.close();
            consumer.join();
        }, "Тест завис: метод close() не разбудил ожидающий поток");
    }
}