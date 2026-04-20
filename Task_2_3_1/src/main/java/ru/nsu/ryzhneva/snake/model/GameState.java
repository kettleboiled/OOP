package ru.nsu.ryzhneva.snake.model;

import java.util.Deque;
import java.util.LinkedList;
import ru.nsu.ryzhneva.snake.model.data.Coordinates;
import ru.nsu.ryzhneva.snake.model.data.GameConfig;
import ru.nsu.ryzhneva.snake.model.data.GameStatus;
import ru.nsu.ryzhneva.snake.model.data.MoveDirection;
import ru.nsu.ryzhneva.snake.model.food.Food;

/**
 * Класс, представляющий текущее состояние игры.
 * Хранит положение змеи, еды, счетчик очков и текущее поведение.
 */
public class GameState {
    private final GameConfig config;
    private final Deque<Coordinates> snake;
    private Food food;
    private int score;
    private int pendingGrowth;

    private MoveDirection currentDirection;
    private MoveDirection pendingDirection;

    private GameStatus status;

    /**
     * Создает новое игровое состояние на основе конфигурации.
     *
     * @param config конфигурация
     */
    public GameState(GameConfig config) {
        this.config = config;
        this.snake = new LinkedList<>();
        initGame();
    }

    /**
     * Инициализирует или сбрасывает переменные состояния игры на начальные.
     */
    private void initGame() {
        snake.clear();
        snake.addFirst(new Coordinates(
                config.width() / 2,
                config.height() / 2));
        currentDirection = MoveDirection.RIGHT;
        pendingDirection = MoveDirection.RIGHT;
        status = GameStatus.READY;
        score = 0;
        pendingGrowth = 0;
    }

    /**
     * Устанавливает еду на поле.
     *
     * @param food объект еды
     */
    public void setFood(Food food) {
        this.food = food;
    }

    /**
     * Задает направление для следующего хода.
     *
     * @param direction новое направление движения
     */
    public void changeDirection(MoveDirection direction) {
        if (!currentDirection.isOpposite(direction)) {
            pendingDirection = direction;
        }
    }

    /**
     * Применяет запланированное направление к текущему.
     */
    public void applyPendingDirection() {
        currentDirection = pendingDirection;
    }

    /**
     * Вычисляет координаты,
     * на которых будет находиться голова змеи на следующем тике.
     *
     * @return новые координаты головы
     */
    public Coordinates getNextHeadPosition() {
        Coordinates head = snake.getFirst();
        return new Coordinates(
                head.x() + currentDirection.getDx(),
                head.y() + currentDirection.getDy()
        );
    }

    /**
     * Изменяет текущий статус игры.
     *
     * @param status новый статус
     */
    public void setStatus(GameStatus status) {
        this.status = status;
    }

    /**
     * Возвращает очередь координат сегментов змеи.
     *
     * @return змея в виде двусторонней очереди координат
     */
    public Deque<Coordinates> getSnake() {
        return snake;
    }

    /**
     * Возвращает еду, находящуюся на поле в данный момент.
     *
     * @return еда или null, если ее нет
     */
    public Food getFood() {
        return food;
    }

    /**
     * Считывает текущий статус игры.
     *
     * @return текущий статус
     */
    public GameStatus getStatus() {
        return status;
    }

    /**
     * Считывает текущий счет.
     *
     * @return количество заработанных очков
     */
    public int getScore() {
        return score;
    }

    /**
     * Добавляет очки к текущему результату.
     *
     * @param points количество очков для добавления
     */
    public void addScore(int points) {
        this.score += points;
    }

    /**
     * Возвращает количество сегментов,
     * на которое змея должна вырасти.
     *
     * @return отложенный рост змеи
     */
    public int getPendingGrowth() {
        return pendingGrowth;
    }

    /**
     * Увеличивает счетчик отложенного роста змеи.
     * Вызывается при съедении еды.
     *
     * @param growth количество сегментов
     */
    public void addGrowth(int growth) {
        this.pendingGrowth += growth;
    }

    /**
     * Уменьшает счетчик отложенного роста на 1.
     * Вызывается после каждого такта роста змейки.
     */
    public void decrementGrowth() {
        if (this.pendingGrowth > 0) {
            this.pendingGrowth--;
        }
    }
}
