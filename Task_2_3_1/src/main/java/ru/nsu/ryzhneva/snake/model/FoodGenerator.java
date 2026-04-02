package ru.nsu.ryzhneva.snake.model;

import java.util.Deque;
import ru.nsu.ryzhneva.snake.model.data.Coordinates;
import ru.nsu.ryzhneva.snake.model.data.GameConfig;

/**
 * Интерфейс, описывающий генератор еды на игровом поле.
 */
public interface FoodGenerator {

    /**
     * Создает новую еду на свободном месте поля.
     *
     * @param config конфигурация игры с размерами поля
     * @param snake список координат сегментов змейки
     * @return сгенерированный объект еды, или null, если свободных клеток нет
     */
    Food generateFood(GameConfig config, Deque<Coordinates> snake);
}
