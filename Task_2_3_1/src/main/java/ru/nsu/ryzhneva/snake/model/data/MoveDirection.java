package ru.nsu.ryzhneva.snake.model.data;

/**
 * Перечисление возможных направлений движения для объектов в игре.
 */
public enum MoveDirection {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private final int dx;
    private final int dy;

    /**
     * Конструктор с инициализацией векторов направлений.
     *
     * @param dx смещение по оси абсцисс
     * @param dy смещение по оси ординат
     */
    MoveDirection(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Получить смещение по горизонтали для данного направления.
     *
     * @return смещение по горизонтали
     */
    public int getDx() {
        return dx;
    }

    /**
     * Получить смещение по вертикали для данного направления.
     *
     * @return смещение по вертикали
     */
    public int getDy() {
        return dy;
    }

    /**
     * Проверяет, является ли указанное направление строго противоположным (разносмысленным) текущему.
     *
     * @param anotherDirection направление для проверки.
     * @return {@code true} если направления противоположны друг другу,
     *         иначе {@code false}.
     */
    public boolean isOpposite(MoveDirection anotherDirection) {
        return ((this.dx == anotherDirection.dx && this.dy != anotherDirection.dy)
                || (this.dy == anotherDirection.dy && this.dx != anotherDirection.dx));

    }
}
