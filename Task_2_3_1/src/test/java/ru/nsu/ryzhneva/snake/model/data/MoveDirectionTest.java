package ru.nsu.ryzhneva.snake.model.data;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Тест для MoveDirection.
 */
class MoveDirectionTest {
    @Test
    void testIsOpposite() {
        assertTrue(MoveDirection.UP.isOpposite(MoveDirection.DOWN));
        assertTrue(MoveDirection.DOWN.isOpposite(MoveDirection.UP));
        assertTrue(MoveDirection.LEFT.isOpposite(MoveDirection.RIGHT));
        assertTrue(MoveDirection.RIGHT.isOpposite(MoveDirection.LEFT));
        assertFalse(MoveDirection.UP.isOpposite(MoveDirection.LEFT));
        assertFalse(MoveDirection.UP.isOpposite(MoveDirection.RIGHT));
        assertFalse(MoveDirection.UP.isOpposite(MoveDirection.UP));
    }
}
