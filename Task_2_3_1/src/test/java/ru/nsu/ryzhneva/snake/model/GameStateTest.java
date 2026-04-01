package ru.nsu.ryzhneva.snake.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.snake.model.data.Coordinates;
import ru.nsu.ryzhneva.snake.model.data.GameConfig;
import ru.nsu.ryzhneva.snake.model.data.GameStatus;
import ru.nsu.ryzhneva.snake.model.data.MoveDirection;

class GameStateTest {
    @Test
    void testInitialState() {
        GameConfig config = new GameConfig(10, 10, 5, 100.0);
        GameState state = new GameState(config);
        assertEquals(GameStatus.PLAYING, state.getStatus());
        assertEquals(0, state.getScore());
        assertEquals(0, state.getPendingGrowth());
        assertNull(state.getFood());
        assertEquals(1, state.getSnake().size());
        Coordinates head = state.getSnake().getFirst();
        assertEquals(5, head.x());
        assertEquals(5, head.y());
    }
    @Test
    void testChangeDirection() {
        GameConfig config = new GameConfig(10, 10, 5, 100.0);
        GameState state = new GameState(config);
        state.applyPendingDirection(); 
        assertEquals(6, state.getNextHeadPosition().x());
        state.changeDirection(MoveDirection.DOWN);
        state.applyPendingDirection();
        assertEquals(5, state.getNextHeadPosition().x());
        assertEquals(6, state.getNextHeadPosition().y());
        state.changeDirection(MoveDirection.UP);
        state.applyPendingDirection();
        assertEquals(6, state.getNextHeadPosition().y()); 
    }
    @Test
    void testScoreAndGrowth() {
        GameConfig config = new GameConfig(10, 10, 5, 100.0);
        GameState state = new GameState(config);
        state.addScore(15);
        assertEquals(15, state.getScore());
        state.addGrowth(2);
        assertEquals(2, state.getPendingGrowth());
        state.decrementGrowth();
        assertEquals(1, state.getPendingGrowth());
        state.decrementGrowth();
        assertEquals(0, state.getPendingGrowth());
        state.decrementGrowth(); 
        assertEquals(0, state.getPendingGrowth());
    }
}
