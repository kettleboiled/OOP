package ru.nsu.ryzhneva.snake.model.data;

/**
 * Перечисление возможных направлений движения для объектов в игре.
 */
public enum MoveDirection {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private final int x;
    private final int y;

    /**
     * Конструктор с инициализацией векторов направлений.
     *
     * @param x смещение по оси абсцисс
     * @param y смещение по оси ординат
     */
    MoveDirection(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return смещение по горизонтали для данного направления.
     */
    public int getX() {
        return x;
    }

    /**
     * @return смещение по вертикали для данного направления.
     */
    public int getY() {
        return y;
    }

    /**
     * Проверяет, является ли указанное направление противоположным текущему.
     *
     * @param anotherDirection направление для проверки.
     * @return {@code true} если направления противоположны друг другу,
     * иначе {@code false}.
     */
    public boolean isOpposite(MoveDirection anotherDirection) {
        return ((this.x == anotherDirection.x && this.y != anotherDirection.y)
                || (this.y == anotherDirection.y && this.x != anotherDirection.x));

    }
}
