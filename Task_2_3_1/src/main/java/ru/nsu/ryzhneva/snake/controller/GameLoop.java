package ru.nsu.ryzhneva.snake.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class GameLoop {
    private final Timeline timeline;

    public GameLoop(Runnable onTick, double millis) {
        timeline = new Timeline(new KeyFrame(Duration.millis(millis), e -> onTick.run()));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void start() {
        timeline.play();
    }

    public void stop() {
        timeline.stop();
    }

    public void restart() {
        timeline.playFromStart();
    }
}

