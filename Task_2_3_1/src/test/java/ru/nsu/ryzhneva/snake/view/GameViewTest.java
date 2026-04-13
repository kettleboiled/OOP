package ru.nsu.ryzhneva.snake.view;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.nsu.ryzhneva.snake.model.GameState;
import ru.nsu.ryzhneva.snake.model.data.GameStatus;
import ru.nsu.ryzhneva.snake.model.food.Food;

/**
 * Тесты для {@link GameView}.
 */
@ExtendWith(MockitoExtension.class)
class GameViewTest {

    @BeforeAll
    static void initJavaFx() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            Platform.startup(latch::countDown);
            latch.await(5, TimeUnit.SECONDS);
        } catch (IllegalStateException | UnsupportedOperationException e) {
        }
    }

    @Mock private Canvas canvas;
    @Mock private GraphicsContext graphicsContext;
    @Mock private Label scoreLabel;
    @Mock private Label messageLabel;
    @Mock private VBox root;

    @Mock private ReadOnlyDoubleProperty rootWidthProperty;
    @Mock private ReadOnlyDoubleProperty rootHeightProperty;
    @Mock private DoubleProperty canvasWidthProperty;
    @Mock private DoubleProperty canvasHeightProperty;

    @Mock private GameState gameState;
    @Mock private Food mockFood;

    private GameView gameView;

    @BeforeEach
    void setUp() throws Exception {
        gameView = new GameView(30, 20, 20);

        setField(gameView, "root", root);
        setField(gameView, "canvas", canvas);
        setField(gameView, "scoreLabel", scoreLabel);
        setField(gameView, "messageLabel", messageLabel);

        lenient().when(canvas.getGraphicsContext2D()).thenReturn(graphicsContext);
        lenient().when(gameState.getSnake()).thenReturn(new java.util.ArrayDeque<>());
        lenient().when(gameState.getFood()).thenReturn(mockFood);
        lenient().when(mockFood.getPosition())
                .thenReturn(new ru.nsu.ryzhneva.snake.model.data.Coordinates(0, 0));

        lenient().when(root.widthProperty()).thenReturn(rootWidthProperty);
        lenient().when(root.heightProperty()).thenReturn(rootHeightProperty);
        lenient().when(canvas.widthProperty()).thenReturn(canvasWidthProperty);
        lenient().when(canvas.heightProperty()).thenReturn(canvasHeightProperty);
    }

    private void setField(Object obj, String fieldName, Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    @Test
    void testInitializeAndResize() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<Throwable> error =
                new AtomicReference<>();
        Platform.runLater(() -> {
            try {
                when(gameState.getScore()).thenReturn(0);
                gameView.setInitialState(gameState);

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
        final AtomicReference<Throwable> error =
                new AtomicReference<>();
        Platform.runLater(() -> {
            try {
                when(gameState.getScore()).thenReturn(10);
                when(gameState.getStatus()).thenReturn(GameStatus.READY);

                gameView.setInitialState(gameState);
                gameView.initialize();

                when(messageLabel.isVisible()).thenReturn(true);
                when(gameState.getStatus()).thenReturn(GameStatus.PLAYING);
                gameView.onGameUpdated(gameState);

                verify(messageLabel).setVisible(false);
                verify(scoreLabel, atLeastOnce()).setText("Score: 10");

                when(gameState.getStatus()).thenReturn(GameStatus.WIN);
                gameView.onGameEnded(gameState);
                
                verify(messageLabel, atLeastOnce()).setVisible(true);
                verify(messageLabel).setText("YOU WIN!\nНажмите ENTER для запуска");

                when(gameState.getStatus()).thenReturn(GameStatus.LOSE);
                gameView.onGameEnded(gameState);
                
                verify(messageLabel, atLeastOnce()).setVisible(true);
                verify(messageLabel).setText("YOU LOSE\nНажмите ENTER для запуска");

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
