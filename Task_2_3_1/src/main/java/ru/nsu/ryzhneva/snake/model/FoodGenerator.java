package ru.nsu.ryzhneva.snake.model;

import java.util.Deque;
import ru.nsu.ryzhneva.snake.model.data.Coordinates;
import ru.nsu.ryzhneva.snake.model.data.GameConfig;

public interface FoodGenerator {

    Food generateFood(GameConfig config, Deque<Coordinates> snake);
}
