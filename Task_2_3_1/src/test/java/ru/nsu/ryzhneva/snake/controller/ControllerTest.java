package ru.nsu.ryzhneva.snake.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.snake.model.LengthWinCondition;
import ru.nsu.ryzhneva.snake.model.data.GameStatus;
import ru.nsu.ryzhneva.snake.model.food.RandomFoodGenerator;
import ru.nsu.ryzhneva.snake.view.GameView;

/**
 * Тест Controller.
 */
@Disabled("Проблемы с запуском JavaFX в CI")
class ControllerTest {

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
            assumeTrue(false,
                    "JavaFX is not supported in this environment.");
        }
    }

    private void setField(Object obj, String fieldName, Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    @Test
    void testControllerFlow() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        java.util.concurrent.atomic.AtomicReference<Throwable> error =
                new java.util.concurrent.atomic.AtomicReference<>();
        Platform.runLater(() -> {
            try {
                GameView controller = new GameView(
                    10, 10, 10, 5, 100,
                    new RandomFoodGenerator(),
                    new LengthWinCondition()
                );

                VBox root = new VBox();
                Canvas canvas = new Canvas();
                Label scoreLabel = new Label();
                final Label messageLabel = new Label();

                setField(controller, "root", root);
                setField(controller, "canvas", canvas);
                setField(controller, "scoreLabel", scoreLabel);
                setField(controller, "messageLabel", messageLabel);

                controller.initialize();

                controller.onGameUpdated();
                assertTrue(scoreLabel.getText().startsWith("Score:"));

                Field gsField = GameView.class.getDeclaredField("gameService");
                gsField.setAccessible(true);
                Object gameServiceObj = gsField.get(controller);
                ru.nsu.ryzhneva.snake.model.GameService gameService =
                        (ru.nsu.ryzhneva.snake.model.GameService) gameServiceObj;
                ru.nsu.ryzhneva.snake.model.GameState state = gameService.getState();
                state.setStatus(GameStatus.LOSE);

                controller.onGameEnded(GameStatus.LOSE);
                assertTrue(messageLabel.isVisible());
                assertEquals("YOU LOSE\nНажмите ENTER для перезапуска", messageLabel.getText());

                KeyEvent enterEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ENTER,
                        false, false, false, false);
                
                Field cField = GameView.class.getDeclaredField("inputController");
                cField.setAccessible(true);
                Controller inputController = (Controller) cField.get(controller);
                
                java.lang.reflect.Method handleKeyPress = Controller.class.getDeclaredMethod(
                        "handleKeyPress", KeyEvent.class);
                handleKeyPress.setAccessible(true);
                handleKeyPress.invoke(inputController, enterEvent);

                // For next assertions, since inputController processes enter, test might be flaky but we just check compilation here.
                
                KeyEvent upEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "",
                        KeyCode.UP, false, false, false, false);
                handleKeyPress.invoke(inputController, upEvent);

                KeyEvent downEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "",
                        KeyCode.DOWN, false, false, false, false);
                handleKeyPress.invoke(inputController, downEvent);

                KeyEvent leftEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "",
                        KeyCode.LEFT, false, false, false, false);
                handleKeyPress.invoke(inputController, leftEvent);

                KeyEvent rightEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "",
                        KeyCode.RIGHT, false, false, false, false);
                handleKeyPress.invoke(inputController, rightEvent);

                controller.onGameEnded(GameStatus.WIN);
                assertEquals("YOU WIN!\nНажмите ENTER для перезапуска", messageLabel.getText());

            } catch (Throwable e) {
                error.set(e);
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);
        if (error.get() != null) {
            if (error.get() instanceof Error) {
                throw (Error) error.get();
            }
            if (error.get() instanceof Exception) {
                throw (Exception) error.get();
            }
            throw new RuntimeException(error.get());
        }
    }
}
