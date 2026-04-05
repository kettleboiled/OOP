package ru.nsu.ryzhneva.pizzeria.queue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Тесты для класса UnboundedThreadSafeQueue.
 */
class UnboundedThreadSafeQueueTest {

    @Test
    void testPutAndGet() throws Exception {
        UnboundedThreadSafeQueue<String> queue = new UnboundedThreadSafeQueue<>();
        queue.put("item1");
        queue.put("item2");
        assertEquals("item1", queue.get());
        assertEquals("item2", queue.get());
    }

    @Test
    void testClose() throws Exception {
        UnboundedThreadSafeQueue<String> queue = new UnboundedThreadSafeQueue<>();
        queue.close();
        assertThrows(QueueClosedException.class, () -> queue.put("item"));
        assertThrows(QueueClosedException.class, () -> queue.get());
    }
}

