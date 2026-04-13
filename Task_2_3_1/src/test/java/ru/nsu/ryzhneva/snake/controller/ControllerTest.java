package ru.nsu.ryzhneva.snake.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.snake.model.GameService;
import ru.nsu.ryzhneva.snake.model.GameState;
import ru.nsu.ryzhneva.snake.model.LengthWinCondition;
import ru.nsu.ryzhneva.snake.model.data.GameConfig;
import ru.nsu.ryzhneva.snake.model.data.GameStatus;
import ru.nsu.ryzhneva.snake.model.food.RandomFoodGenerator;
import ru.nsu.ryzhneva.snake.view.GameView;

/**
 * Тест Controller.
 */
@Disabled("Проблемы с запуском JavaFX в CI")
class ControllerTest {

    @BeforeAll
    static void initJavaFx() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            Platform.startup(latch::countDown);
            latch.await(5, TimeUnit.SECONDS);
        } catch (IllegalStateException | UnsupportedOperationException e) {
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
        AtomicReference<Throwable> error =
                new AtomicReference<>();
        Platform.runLater(() -> {
            try {
                GameView controller = new GameView(10, 10, 10);
                GameConfig config = new GameConfig(10, 10, 5, 100);
                GameService gameService = new GameService(config, controller,
                        new RandomFoodGenerator(), new LengthWinCondition());
                Controller inputController = new Controller(gameService, config);
                controller.setInitialState(gameService.getState());
                controller.setKeyPressHandler(inputController::handleKeyPress);

                VBox root = new VBox();
                Canvas canvas = new Canvas();
                Label scoreLabel = new Label();
                final Label messageLabel = new Label();

                setField(controller, "root", root);
                setField(controller, "canvas", canvas);
                setField(controller, "scoreLabel", scoreLabel);
                setField(controller, "messageLabel", messageLabel);

                controller.initialize();

                GameState state = gameService.getState();

                controller.onGameUpdated(state);
                assertTrue(scoreLabel.getText().startsWith("Score:"));

                state.setStatus(GameStatus.LOSE);

                controller.onGameEnded(state);
                assertTrue(messageLabel.isVisible());
                assertEquals("YOU LOSE\nНажмите ENTER для запуска", messageLabel.getText());

                KeyEvent enterEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ENTER,
                        false, false, false, false);
                
                Method handleKeyPress = Controller.class.getDeclaredMethod(
                        "handleKeyPress", KeyEvent.class);
                handleKeyPress.setAccessible(true);
                handleKeyPress.invoke(inputController, enterEvent);

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

                state.setStatus(GameStatus.WIN);
                controller.onGameEnded(state);
                assertEquals("YOU WIN!\nНажмите ENTER для запуска", messageLabel.getText());

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
