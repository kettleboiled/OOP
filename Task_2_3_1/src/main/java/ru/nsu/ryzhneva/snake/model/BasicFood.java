package ru.nsu.ryzhneva.snake.model;

import ru.nsu.ryzhneva.snake.model.data.Coordinates;

public record BasicFood(Coordinates position, int points, int growth) implements Food {

    @Override
    public Coordinates getPosition() {
        return position;
    }

    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public int getGrowth() {
        return growth;
    }
}

