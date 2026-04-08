package ru.nsu.ryzhneva.snake.model.data;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Тест для GameStatus.
 */
class GameStatusTest {
    @Test
    void testGameStatus() {
        assertEquals(4, GameStatus.values().length);
        assertEquals(GameStatus.READY, GameStatus.valueOf("READY"));
        assertEquals(GameStatus.PLAYING, GameStatus.valueOf("PLAYING"));
        assertEquals(GameStatus.WIN, GameStatus.valueOf("WIN"));
        assertEquals(GameStatus.LOSE, GameStatus.valueOf("LOSE"));
    }
}
