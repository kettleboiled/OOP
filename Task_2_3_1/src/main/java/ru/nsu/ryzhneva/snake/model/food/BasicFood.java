package ru.nsu.ryzhneva.snake.model.food;

import ru.nsu.ryzhneva.snake.model.data.Coordinates;

/**
 * Класс, представляющий еду в игре.
 * Реализует интерфейс {@link Food}.
 *
 * @param position координаты расположения еды на игровом поле
 * @param points количество очков, начисляемых за съедение еды
 * @param growth количество сегментов, на которое увеличится змейка
 */
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
