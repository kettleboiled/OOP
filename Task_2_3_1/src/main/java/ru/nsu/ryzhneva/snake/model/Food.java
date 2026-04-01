package ru.nsu.ryzhneva.snake.model;

import ru.nsu.ryzhneva.snake.model.data.Coordinates;

public interface Food {
    Coordinates getPosition();
    int getPoints();
    int getGrowth();
}

