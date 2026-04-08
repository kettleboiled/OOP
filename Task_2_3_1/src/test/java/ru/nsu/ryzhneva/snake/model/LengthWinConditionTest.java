package ru.nsu.ryzhneva.snake.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ru.nsu.ryzhneva.snake.model.data.Coordinates;
import ru.nsu.ryzhneva.snake.model.data.GameConfig;

/**
 * Тест LengthWinCondition.
 */
class LengthWinConditionTest {

    @Test
    void testIsWin() {
        LengthWinCondition cond = new LengthWinCondition();
        GameConfig config = new GameConfig(10, 10, 3, 100.0);
        GameState state = new GameState(config);
        assertFalse(cond.isWin(state, config));
        state.getSnake().add(new Coordinates(1, 1));
        assertFalse(cond.isWin(state, config));
        state.getSnake().add(new Coordinates(1, 2));
        assertTrue(cond.isWin(state, config));
    }
}
