package ru.nsu.ryzhneva.snake.model.data;

public enum MoveDirection {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private final int x;
    private final int y;

    MoveDirection(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isOpposite(MoveDirection anotherDirection) {
        return ((this.x == anotherDirection.x && this.y != anotherDirection.y)
                || (this.y == anotherDirection.y && this.x != anotherDirection.x));

    }
}
