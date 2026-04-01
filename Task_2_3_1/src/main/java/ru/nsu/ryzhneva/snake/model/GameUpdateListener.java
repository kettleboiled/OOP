package ru.nsu.ryzhneva.snake.model;

import ru.nsu.ryzhneva.snake.model.data.GameStatus;

public interface GameUpdateListener {
    void onGameUpdated();
    void onGameEnded(GameStatus status);
}

