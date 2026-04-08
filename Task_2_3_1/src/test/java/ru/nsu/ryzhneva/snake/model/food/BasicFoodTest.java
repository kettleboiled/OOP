package ru.nsu.ryzhneva.snake.model.food;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.snake.model.data.Coordinates;

/**
 * Тесты для BasicFood.
 */
class BasicFoodTest {
    @Test
    void testBasicFood() {
        Coordinates coords = new Coordinates(7, 3);
        BasicFood food = new BasicFood(coords, 10, 2);
        assertEquals(coords, food.getPosition());
        assertEquals(10, food.getPoints());
        assertEquals(2, food.getGrowth());
    }
}
