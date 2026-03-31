package ru.nsu.ryzhneva.pizzeria.queue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Тесты для класса BoundedThreadSafeQueue.
 */
class BoundedThreadSafeQueueTest {

    @Test
    void testCapacityException() {
        assertThrows(IllegalArgumentException.class, () -> new BoundedThreadSafeQueue<>(0));
    }

    @Test
    void testPutAndGet() throws Exception {
        BoundedThreadSafeQueue<String> queue = new BoundedThreadSafeQueue<>(2);
        queue.put("item1");
        queue.put("item2");
        assertEquals("item1", queue.get());
        assertEquals("item2", queue.get());
    }

    @Test
    void testGetBatch() throws Exception {
        BoundedThreadSafeQueue<String> queue = new BoundedThreadSafeQueue<>(5);
        queue.put("1");
        queue.put("2");
        queue.put("3");
        List<String> batch = queue.getBatch(2);
        assertEquals(2, batch.size());
        assertEquals("1", batch.get(0));
        assertEquals("2", batch.get(1));
    }

    @Test
    void testClose() throws Exception {
        BoundedThreadSafeQueue<String> queue = new BoundedThreadSafeQueue<>(5);
        queue.close();
        assertThrows(IllegalStateException.class, () -> queue.put("item"));
        assertThrows(QueueClosedException.class, () -> queue.get());
    }
}

