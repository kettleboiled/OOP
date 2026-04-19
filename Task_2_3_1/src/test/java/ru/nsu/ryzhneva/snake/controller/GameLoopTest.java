package ru.nsu.ryzhneva.snake.controller;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Тесты для GameLoop.
 */
@Disabled("Проблемы с запуском JavaFX в CI")
class GameLoopTest {

    @BeforeAll
    static void initJfx() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            Platform.startup(latch::countDown);
            if (!latch.await(5, TimeUnit.SECONDS)) {
                assumeTrue(false, "JavaFX startup timed out");
            }
        } catch (IllegalStateException e) {
        } catch (Throwable e) {
            assumeTrue(false, "JavaFX not supported");
        }
    }

    @Test
    void testGameLoop() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                GameLoop loop = new GameLoop(() -> {}, 100);
                assertDoesNotThrow(loop::start);
                assertDoesNotThrow(loop::stop);
                assertDoesNotThrow(loop::restart);
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);
    }
}
