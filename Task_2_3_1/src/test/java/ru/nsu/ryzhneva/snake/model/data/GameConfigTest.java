package ru.nsu.ryzhneva.snake.model.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Тест GameConfig.
 */
class GameConfigTest {

    @Test
    void testValidConfig() {
        GameConfig config = new GameConfig(30, 20, 50, 150.0);
        assertEquals(30, config.width());
        assertEquals(20, config.height());
        assertEquals(50, config.lengthWin());
        assertEquals(150.0, config.tickMs());
    }

    @Test
    void testInvalidWidth() {
        assertThrows(IllegalArgumentException.class, () ->
                new GameConfig(0, 20, 50, 150.0));
        assertThrows(IllegalArgumentException.class, () ->
                new GameConfig(-10, 20, 50, 150.0));
    }

    @Test
    void testInvalidHeight() {
        assertThrows(IllegalArgumentException.class, () ->
                new GameConfig(30, 0, 50, 150.0));
    }

    @Test
    void testInvalidLengthWin() {
        assertThrows(IllegalArgumentException.class, () ->
                new GameConfig(5, 5, 1, 150.0));
        assertThrows(IllegalArgumentException.class, () ->
                new GameConfig(5, 5, 30, 150.0));
    }

    @Test
    void testInvalidTickMs() {
        assertThrows(IllegalArgumentException.class, () ->
                new GameConfig(30, 20, 50, 0.0));
        assertThrows(IllegalArgumentException.class, () ->
                new GameConfig(30, 20, 50, -50.0));
    }
}
