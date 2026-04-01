package ru.nsu.ryzhneva.snake.model;

import ru.nsu.ryzhneva.snake.model.data.GameConfig;

public interface WinCondition {
    boolean isWin(GameState state, GameConfig config);
}

