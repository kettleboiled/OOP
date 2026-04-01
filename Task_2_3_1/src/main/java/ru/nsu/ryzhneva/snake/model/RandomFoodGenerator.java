package ru.nsu.ryzhneva.snake.model;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;
import ru.nsu.ryzhneva.snake.model.data.Coordinates;
import ru.nsu.ryzhneva.snake.model.data.GameConfig;

public class RandomFoodGenerator implements FoodGenerator {
    private final Random random = new Random();

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
                    10, 1);
        }
        
        return null;
    }
}
