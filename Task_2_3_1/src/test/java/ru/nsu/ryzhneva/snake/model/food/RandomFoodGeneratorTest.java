package ru.nsu.ryzhneva.snake.model.food;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayDeque;
import java.util.Deque;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.snake.model.data.Coordinates;
import ru.nsu.ryzhneva.snake.model.data.GameConfig;

/**
 * Тест RandomFoodGenerator.
 */
class RandomFoodGeneratorTest {

    @Test
    void testFoodGeneratedInBoundsAndNotOnSnake() {
        GameConfig config = new GameConfig(10, 10, 10, 100.0);
        RandomFoodGenerator generator = new RandomFoodGenerator();
        
        Deque<Coordinates> snake = new ArrayDeque<>();
        snake.add(new Coordinates(5, 5));
        snake.add(new Coordinates(4, 5));
        snake.add(new Coordinates(3, 5));
        
        Food food = generator.generateFood(config, snake);
        
        assertNotNull(food);
        Coordinates pos = food.getPosition();
        assertTrue(pos.x() >= 0 && pos.x() < 10);
        assertTrue(pos.y() >= 0 && pos.y() < 10);
        assertFalse(snake.contains(pos));
        assertEquals(10, food.getPoints());
        assertEquals(1, food.getGrowth());
    }

    @Test
    void testNoFreeSpace() {
        GameConfig config = new GameConfig(2, 2, 4, 100.0);
        RandomFoodGenerator generator = new RandomFoodGenerator();
        
        Deque<Coordinates> snake = new ArrayDeque<>();
        snake.add(new Coordinates(0, 0));
        snake.add(new Coordinates(0, 1));
        snake.add(new Coordinates(1, 0));
        snake.add(new Coordinates(1, 1));
        
        Food food = generator.generateFood(config, snake);
        assertNull(food);
    }
}
