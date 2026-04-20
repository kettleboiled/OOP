package ru.nsu.ryzhneva.snake.model.food;

import ru.nsu.ryzhneva.snake.model.data.Coordinates;

/**
 * Интерфейс, описывающий абстрактную сущность еды в игре.
 */
public interface Food {
    /**
     * Получить позицию, на которой находится еда.
     *
     * @return двухмерные координаты еды
     */
    Coordinates getPosition();

    /**
     * Получить количество очков, даваемых за съедение еды.
     *
     * @return число очков
     */
    int getPoints();

    /**
     * Насколько увеличивается змейка.
     *
     * @return количество сегментов тела, на которое увеличится змейка
     */
    int getGrowth();
}
