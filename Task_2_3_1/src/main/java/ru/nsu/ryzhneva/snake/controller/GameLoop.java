package ru.nsu.ryzhneva.snake.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Класс, отвечающий за игровой цикл.
 * Выполняет тактовые обновления игры через заданные интервалы времени.
 */
public class GameLoop {
    private final Timeline timeline;

    /**
     * Создает игровой цикл.
     *
     * @param onTick действие, выполняемое на каждом тике
     * @param millis интервал между тиками в миллисекундах
     */
    public GameLoop(Runnable onTick, double millis) {
        timeline = new Timeline(new KeyFrame(Duration.millis(millis), e -> onTick.run()));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Запускает игровой цикл (таймер).
     */
    public void start() {
        timeline.play();
    }

    /**
     * Останавливает игровой цикл (таймер).
     */
    public void stop() {
        timeline.stop();
    }

    /**
     * Перезапускает игровой цикл с самого начала.
     */
    public void restart() {
        timeline.playFromStart();
    }
}
