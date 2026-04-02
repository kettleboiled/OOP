package ru.nsu.ryzhneva.snake.controller;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.snake.model.GameState;
import ru.nsu.ryzhneva.snake.model.data.Coordinates;
import ru.nsu.ryzhneva.snake.model.data.GameConfig;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Тесты GameRenderer.
 */
class GameRendererTest {

    @BeforeAll
    static void initJfx() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            Platform.startup(latch::countDown);
            latch.await();
        } catch (IllegalStateException e) {
        } catch (UnsupportedOperationException e) {
            org.junit.jupiter.api.Assumptions.assumeTrue(false, "JavaFX is not supported in this environment. Skipping tests.");
        }
    }

    @Test
    void testRender() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                Canvas canvas = new Canvas(100, 100);
                GameRenderer renderer = new GameRenderer(canvas, 10, 10, 10);
                
                GameState state = new GameState(new GameConfig(10, 10, 100, 1.0));
                state.getSnake().add(new Coordinates(5, 5));
                state.getSnake().add(new Coordinates(5, 6));
                
                assertDoesNotThrow(() -> renderer.render(state));
                assertDoesNotThrow(() -> renderer.renderOverlay());
            } finally {
                latch.countDown();
            }
        });
        latch.await();
    }
}
