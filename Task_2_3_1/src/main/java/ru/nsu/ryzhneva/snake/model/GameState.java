package ru.nsu.ryzhneva.snake.model;

import ru.nsu.ryzhneva.snake.model.data.Coordinates;
import ru.nsu.ryzhneva.snake.model.data.GameConfig;
import ru.nsu.ryzhneva.snake.model.data.GameStatus;
import ru.nsu.ryzhneva.snake.model.data.MoveDirection;

import java.util.Deque;
import java.util.LinkedList;

public class GameState {
    private final GameConfig config;
    private final Deque<Coordinates> snake;
    private Food food;
    private int score;
    private int pendingGrowth;

    private MoveDirection currentDirection;
    private MoveDirection pendingDirection;

    private GameStatus status;

    public GameState(GameConfig config) {
        this.config = config;
        this.snake = new LinkedList<>();
        initGame();
    }

    private void initGame() {
        snake.clear();
        snake.addFirst(new Coordinates(
                config.width() / 2,
                config.height() / 2));
        currentDirection = MoveDirection.RIGHT;
        pendingDirection = MoveDirection.RIGHT;
        status = GameStatus.PLAYING;
        score = 0;
        pendingGrowth = 0;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public void changeDirection(MoveDirection direction) {
        if (!currentDirection.isOpposite(direction)) {
            pendingDirection = direction;
        }
    }

    public void applyPendingDirection() {
        currentDirection = pendingDirection;
    }

    public Coordinates getNextHeadPosition() {
        Coordinates head = snake.getFirst();
        return new Coordinates(
                head.x() + currentDirection.getX(),
                head.y() + currentDirection.getY()
        );
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public Deque<Coordinates> getSnake() {
        return snake;
    }
    public Food getFood() {
        return food;
    }
    public GameStatus getStatus() {
        return status;
    }
    public int getScore() {
        return score;
    }
    public void addScore(int points) {
        this.score += points;
    }
    public int getPendingGrowth() {
        return pendingGrowth;
    }
    public void addGrowth(int growth) {
        this.pendingGrowth += growth;
    }
    public void decrementGrowth() {
        if (this.pendingGrowth > 0) {
            this.pendingGrowth--;
        }
    }
}
