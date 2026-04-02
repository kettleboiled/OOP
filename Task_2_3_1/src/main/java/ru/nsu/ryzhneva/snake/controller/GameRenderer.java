package ru.nsu.ryzhneva.snake.controller;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import ru.nsu.ryzhneva.snake.model.GameState;
import ru.nsu.ryzhneva.snake.model.data.Coordinates;

/**
 * Класс, отвечающий за отрисовку игры на компоненте Canvas.
 */
public class GameRenderer {
    private final Canvas canvas;
    private final int sizeCell;
    private final int columns;
    private final int rows;

    private static final Color COLOR_FIELD_NORMAL =
            Color.rgb(148, 206, 126);
    private static final Color COLOR_FIELD_ALTERNATE =
            Color.rgb(62, 195, 103);
    private static final Color COLOR_FOOD =
            Color.RED;
    private static final Color COLOR_SNAKE_BODY =
            Color.BLUEVIOLET;
    private static final Color COLOR_SNAKE_HEAD =
            Color.DARKSLATEBLUE;
    private static final Color COLOR_OVERLAY =
            Color.rgb(0, 0, 0, 0.7);

    /**
     * Создает новый отрисовщик с заданными параметрами поля.
     *
     * @param canvas компонент Canvas для вывода графики
     * @param sizeCell размер одного блока (клетки) в пикселях
     * @param columns количество логических столбцов
     * @param rows количество логических строк
     */
    public GameRenderer(Canvas canvas, int sizeCell, int columns, int rows) {
        this.canvas = canvas;
        this.sizeCell = sizeCell;
        this.columns = columns;
        this.rows = rows;
    }

    /**
     * Отрисовывает все элементы игры.
     *
     * @param model текущее состояние игры для отрисовки
     */
    public void render(GameState model) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                if ((x + y) % 2 == 0) {
                    gc.setFill(COLOR_FIELD_NORMAL);
                } else {
                    gc.setFill(COLOR_FIELD_ALTERNATE);
                }
                gc.fillRect(x * sizeCell, y * sizeCell, sizeCell, sizeCell);
            }
        }

        if (model.getFood() != null) {
            Coordinates food = model.getFood().getPosition();
            gc.setFill(COLOR_FOOD);
            int foodPadding = 5;
            int foodSize = sizeCell - (foodPadding * 2);
            gc.fillRect(food.x() * sizeCell + foodPadding,
                    food.y() * sizeCell + foodPadding,
                    foodSize, foodSize);
        }

        gc.setFill(COLOR_SNAKE_BODY);
        for (Coordinates o : model.getSnake()) {
            gc.fillRect(o.x() * sizeCell + 1, o.y() * sizeCell + 1,
                    sizeCell - 2, sizeCell - 2);
        }

        Coordinates head = model.getSnake().peekFirst();
        if (head != null) {
            gc.setFill(COLOR_SNAKE_HEAD);
            gc.fillRect(head.x() * sizeCell + 1, head.y() * sizeCell + 1,
                    sizeCell - 2, sizeCell - 2);
        }
    }

    /**
     * Отрисовывает слой поверх игрового поля,
     * используемый после завершения игры.
     */
    public void renderOverlay() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(COLOR_OVERLAY);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}
