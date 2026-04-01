package ru.nsu.ryzhneva.snake.model.data;

public record GameConfig(int width, int height, int lengthWin, double tickMs) {

    public GameConfig {
        if (width <= 0 || height <= 0 || lengthWin <= 1 || lengthWin > width * height || tickMs <= 0.0) {
            throw new IllegalArgumentException("Некорректные значения конфигурации.");
        }
    }
}
