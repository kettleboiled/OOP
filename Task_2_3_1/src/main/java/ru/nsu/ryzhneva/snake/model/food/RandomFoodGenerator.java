package ru.nsu.ryzhneva.snake.model.food;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;
import ru.nsu.ryzhneva.snake.model.data.Coordinates;
import ru.nsu.ryzhneva.snake.model.data.GameConfig;

/**
 * Класс, реализующий случайную генерацию еды на пустых клетках поля.
 */
public class RandomFoodGenerator implements FoodGenerator {
    private final Random random = new Random();

    private static final int DEFAULT_FOOD_POINTS = 10;
    /** Значение длины по умолчанию, на которое увеличивается змейка. */
    private static final int DEFAULT_FOOD_GROWTH = 1;

    /**
     * Случайным образом генерирует еду на координатах, не занятых змейкой.
     *
     * @param config конфигурация игры
     * @param snake список элементов тела змейки
     * @return случайно сгенерированный элемент, или null, если поле заполнено
     */
    @Override
    public Food generateFood(GameConfig config, Deque<Coordinates> snake) {
        int width = config.width();
        int height = config.height();

        boolean[][] occupied = new boolean[width][height];

        for (Coordinates o : snake) {
            occupied[o.x()][o.y()] = true;
        }

        List<Coordinates> free = new ArrayList<>(width * height - snake.size());

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (!occupied[x][y]) {
                    free.add(new Coordinates(x, y));
                }
            }
        }

        if (!free.isEmpty()) {
            return new BasicFood(free.get(random.nextInt(free.size())),
                    DEFAULT_FOOD_POINTS, DEFAULT_FOOD_GROWTH);
        }
        
        return null;
    }
}
