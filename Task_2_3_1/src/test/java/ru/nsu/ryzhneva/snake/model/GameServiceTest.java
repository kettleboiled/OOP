package ru.nsu.ryzhneva.snake.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Deque;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.snake.model.data.Coordinates;
import ru.nsu.ryzhneva.snake.model.data.GameConfig;
import ru.nsu.ryzhneva.snake.model.data.GameStatus;
import ru.nsu.ryzhneva.snake.model.data.MoveDirection;
import ru.nsu.ryzhneva.snake.model.food.BasicFood;
import ru.nsu.ryzhneva.snake.model.food.Food;
import ru.nsu.ryzhneva.snake.model.food.FoodGenerator;

/**
 * Тест GameService.
 */
class GameServiceTest {

    private static class StubListener implements GameUpdateListener {
        boolean updated = false;
        GameStatus endedStatus = null;

        @Override
        public void onGameUpdated(GameState gameState) {
            updated = true;
        }

        @Override
        public void onGameEnded(GameState gameState) {
            endedStatus = gameState.getStatus();
        }
    }

    private static class FixedFoodGenerator implements FoodGenerator {
        Coordinates fixedPos;

        public FixedFoodGenerator(Coordinates pos) {
            this.fixedPos = pos;
        }

        @Override
        public Food generateFood(GameConfig config, Deque<Coordinates> snake) {
            return new BasicFood(fixedPos, 10, 1);
        }
    }

    @Test
    void testBasicMovementAndListenerUpdate() throws Exception {
        GameConfig config = new GameConfig(10, 10, 5, 100.0);
        StubListener listener = new StubListener();
        FixedFoodGenerator foodGen = new FixedFoodGenerator(new Coordinates(8, 8));
        WinCondition winCond = new LengthWinCondition();
        GameService service = new GameService(config, listener, foodGen, winCond);
        service.getState().setStatus(GameStatus.PLAYING);
        java.lang.reflect.Method tickMethod =
                GameService.class.getDeclaredMethod("gameTick");
        tickMethod.setAccessible(true);
        tickMethod.invoke(service);
        assertTrue(listener.updated);
        assertNull(listener.endedStatus);
        Coordinates head = service.getState().getSnake().getFirst();
        assertEquals(6, head.x());
        assertEquals(5, head.y());
        service.changeDirection(MoveDirection.DOWN);
        tickMethod.invoke(service);
        head = service.getState().getSnake().getFirst();
        assertEquals(6, head.x());
        assertEquals(6, head.y());
    }

    @Test
    void testEatFoodAndWin() throws Exception {
        GameConfig configWin =
                new GameConfig(10, 10, 2, 100.0);
        StubListener listener = new StubListener();
        FixedFoodGenerator foodGen = new FixedFoodGenerator(new Coordinates(6, 5));
        WinCondition winCond = new LengthWinCondition();
        GameService service = new GameService(configWin, listener, foodGen, winCond);
        service.getState().setStatus(GameStatus.PLAYING);
        java.lang.reflect.Method tickMethod =
                GameService.class.getDeclaredMethod("gameTick");
        tickMethod.setAccessible(true);
        tickMethod.invoke(service);
        assertEquals(GameStatus.WIN, listener.endedStatus);
        assertEquals(10, service.getState().getScore());
    }

    @Test
    void testWallCollision() throws Exception {
        GameConfig config = new GameConfig(6, 6, 10, 100.0);
        StubListener listener = new StubListener();
        FixedFoodGenerator foodGen = new FixedFoodGenerator(new Coordinates(1, 1));
        WinCondition winCond = new LengthWinCondition();
        GameService service = new GameService(config, listener, foodGen, winCond);
        service.getState().setStatus(GameStatus.PLAYING);
        java.lang.reflect.Method tickMethod =
                GameService.class.getDeclaredMethod("gameTick");
        tickMethod.setAccessible(true);
        for (int i = 0; i < 10; i++) {
            tickMethod.invoke(service);
            if (listener.endedStatus != null) {
                break;
            }
        }
        assertEquals(GameStatus.LOSE, listener.endedStatus);
    }

    @Test
    void testSelfCollisionAndGameEndedDoNotAdvance() throws Exception {
        GameConfig config = new GameConfig(10, 10, 10, 100.0);
        StubListener listener = new StubListener();
        GameService service = new GameService(config, listener,
                new FixedFoodGenerator(new Coordinates(1, 1)), new LengthWinCondition());
        java.lang.reflect.Method tickMethod = GameService.class.getDeclaredMethod("gameTick");
        tickMethod.setAccessible(true);
        service.getState().setStatus(GameStatus.LOSE);
        listener.endedStatus = null;
        tickMethod.invoke(service);
        assertEquals(GameStatus.LOSE, listener.endedStatus);
    }
}
