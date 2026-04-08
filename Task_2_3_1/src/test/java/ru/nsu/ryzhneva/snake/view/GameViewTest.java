package ru.nsu.ryzhneva.snake.view;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.snake.model.LengthWinCondition;
import ru.nsu.ryzhneva.snake.model.data.GameStatus;
import ru.nsu.ryzhneva.snake.model.food.RandomFoodGenerator;

/**
 * Тесты для {@link GameView}.
 */
@Disabled("Проблемы с запуском JavaFX в CI")
class GameViewTest {

    @BeforeAll
    static void initJfx() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            Platform.startup(latch::countDown);
            if (!latch.await(5, TimeUnit.SECONDS)) {
                assumeTrue(false, "JavaFX startup timed out.");
            }
        } catch (IllegalStateException e) {
        } catch (Throwable e) {
            assumeTrue(false, "JavaFX is not supported in this environment.");
        }
    }

    private void setField(Object obj, String fieldName, Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    @Test
    void testInitializeAndResize() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        final java.util.concurrent.atomic.AtomicReference<Throwable> error =
                new java.util.concurrent.atomic.AtomicReference<>();
        Platform.runLater(() -> {
            try {
                GameView gameView = new GameView(
                        10, 10, 10, 5, 100,
                        new RandomFoodGenerator(),
                        new LengthWinCondition()
                );

                VBox root = new VBox();
                Canvas canvas = new Canvas();
                Label scoreLabel = new Label();
                Label messageLabel = new Label();

                setField(gameView, "root", root);
                setField(gameView, "canvas", canvas);
                setField(gameView, "scoreLabel", scoreLabel);
                setField(gameView, "messageLabel", messageLabel);

                assertDoesNotThrow(gameView::initialize);
                root.resize(400, 400);
                
            } catch (Throwable t) {
                error.set(t);
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);
        if (error.get() != null) {
            throw new RuntimeException(error.get());
        }
    }

    @Test
    void testGameUpdatingAndEnding() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        final java.util.concurrent.atomic.AtomicReference<Throwable> error =
                new java.util.concurrent.atomic.AtomicReference<>();
        Platform.runLater(() -> {
            try {
                GameView gameView = new GameView(
                        10, 10, 10, 5, 100,
                        new RandomFoodGenerator(),
                        new LengthWinCondition()
                );

                VBox root = new VBox();
                Canvas canvas = new Canvas();
                Label scoreLabel = new Label();
                Label messageLabel = new Label();

                setField(gameView, "root", root);
                setField(gameView, "canvas", canvas);
                setField(gameView, "scoreLabel", scoreLabel);
                setField(gameView, "messageLabel", messageLabel);

                gameView.initialize();

                Field gsField = GameView.class.getDeclaredField("gameService");
                gsField.setAccessible(true);
                ru.nsu.ryzhneva.snake.model.GameService gameService =
                        (ru.nsu.ryzhneva.snake.model.GameService) gsField.get(gameView);
                gameService.getState().setStatus(GameStatus.PLAYING);

                gameView.onGameUpdated();
                assertFalse(messageLabel.isVisible());
                assertTrue(scoreLabel.getText().contains("Score:"));

                gameView.onGameEnded(GameStatus.WIN);
                assertTrue(messageLabel.isVisible());
                assertEquals("YOU WIN!\nНажмите ENTER для запуска", messageLabel.getText());

                messageLabel.setVisible(false);
                gameView.onGameEnded(GameStatus.LOSE);
                assertTrue(messageLabel.isVisible());
                assertEquals("YOU LOSE\nНажмите ENTER для запуска", messageLabel.getText());

                gameService.getState().setStatus(GameStatus.READY);
                gameView.onGameUpdated();
                assertTrue(messageLabel.isVisible());
                assertEquals("Нажмите ENTER для запуска", messageLabel.getText());

            } catch (Throwable t) {
                error.set(t);
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);
        if (error.get() != null) {
            throw new RuntimeException(error.get());
        }
    }
}
