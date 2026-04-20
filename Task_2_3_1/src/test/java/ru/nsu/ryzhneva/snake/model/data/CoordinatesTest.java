package ru.nsu.ryzhneva.snake.model.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

/**
 * Тест для Coordinates.
 */
class CoordinatesTest {
    @Test
    void testCoordinates() {
        Coordinates c1 = new Coordinates(10, 20);
        assertEquals(10, c1.x());
        assertEquals(20, c1.y());
        Coordinates c2 = new Coordinates(10, 20);
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        Coordinates c3 = new Coordinates(15, 20);
        assertNotEquals(c1, c3);
    }
}
