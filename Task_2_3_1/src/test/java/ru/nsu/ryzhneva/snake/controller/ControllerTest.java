package ru.nsu.ryzhneva.snake.controller;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.snake.model.LengthWinCondition;
import ru.nsu.ryzhneva.snake.model.RandomFoodGenerator;
import ru.nsu.ryzhneva.snake.model.data.GameStatus;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    @BeforeAll
    static void initJfx() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            Platform.startup(latch::countDown);
            latch.await();
        } catch (IllegalStateException e) {
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
        Platform.runLater(() -> {
            try {
                Controller controller = new Controller(
                    10, 10, 10, 5, 100,
                    new RandomFoodGenerator(),
                    new LengthWinCondition()
                );

                VBox root = new VBox();
                Canvas canvas = new Canvas();
                Label scoreLabel = new Label();
                Label messageLabel = new Label();

                setField(controller, "root", root);
                setField(controller, "canvas", canvas);
                setField(controller, "scoreLabel", scoreLabel);
                setField(controller, "messageLabel", messageLabel);

                controller.initialize();

                controller.onGameUpdated();
                assertTrue(scoreLabel.getText().startsWith("Score:"));

                controller.onGameEnded(GameStatus.LOSE);
                assertTrue(messageLabel.isVisible());
                assertEquals("YOU LOSE\nНажмите ENTER для перезапуска", messageLabel.getText());

                KeyEvent enterEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ENTER, false, false, false, false);
                java.lang.reflect.Method handleKeyPress = Controller.class.getDeclaredMethod("handleKeyPress", KeyEvent.class);
                handleKeyPress.setAccessible(true);
                handleKeyPress.invoke(controller, enterEvent);

                assertFalse(messageLabel.isVisible());

                KeyEvent upEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "",
                        KeyCode.UP, false, false, false, false);
                handleKeyPress.invoke(controller, upEvent);
                KeyEvent downEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "",
                        KeyCode.DOWN, false, false, false, false);
                handleKeyPress.invoke(controller, downEvent);
                KeyEvent leftEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "",
                        KeyCode.LEFT, false, false, false, false);
                handleKeyPress.invoke(controller, leftEvent);
                KeyEvent rightEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "",
                        KeyCode.RIGHT, false, false, false, false);
                handleKeyPress.invoke(controller, rightEvent);

                controller.onGameEnded(GameStatus.WIN);
                assertEquals("YOU WIN!\nНажмите ENTER для перезапуска", messageLabel.getText());

            } catch (Exception e) {
                fail(e);
            } finally {
                latch.countDown();
            }
        });
        latch.await();
    }
}
