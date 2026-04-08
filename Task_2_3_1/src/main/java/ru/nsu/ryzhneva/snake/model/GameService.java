package ru.nsu.ryzhneva.snake.model;

import ru.nsu.ryzhneva.snake.controller.GameLoop;
import ru.nsu.ryzhneva.snake.model.data.Coordinates;
import ru.nsu.ryzhneva.snake.model.data.GameConfig;
import ru.nsu.ryzhneva.snake.model.data.GameStatus;
import ru.nsu.ryzhneva.snake.model.data.MoveDirection;
import ru.nsu.ryzhneva.snake.model.food.Food;
import ru.nsu.ryzhneva.snake.model.food.FoodGenerator;

/**
 * Сервис.
 */
public class GameService {
    private GameState state;
    private final GameUpdateListener listener;
    private final GameLoop gameLoop;
    private final FoodGenerator foodGenerator;
    private final WinCondition winCondition;
    private final GameConfig config;

    /**
     * Создает новый сервис игры с заданными правилами.
     *
     * @param config настройки игры
     * @param listener слушатель событий для обновления UI
     * @param foodGenerator объект, отвечающий за появление еды
     * @param winCondition условие победы
     */
    public GameService(GameConfig config,
                       GameUpdateListener listener,
                       FoodGenerator foodGenerator,
                       WinCondition winCondition) {
        this.config = config;
        this.listener = listener;
        this.foodGenerator = foodGenerator;
        this.winCondition = winCondition;
        this.state = new GameState(config);
        generateInitialFood();
        this.gameLoop = new GameLoop(this::gameTick, config.tickMs());
    }

    /**
     * Запускает игровой цикл.
     */
    public void start() {
        state.setStatus(GameStatus.PLAYING);
        gameLoop.start();
    }

    /**
     * Останавливает игровой цикл.
     */
    public void stop() {
        gameLoop.stop();
    }

    /**
     * Перезапускает игру.
     *
     * @param config конфигурация
     */
    public void restart(GameConfig config) {
        this.state = new GameState(config);
        generateInitialFood();

        gameLoop.stop();
        start();
        listener.onGameUpdated();
    }

    /**
     * Генерирует начальную еду на поле.
     */
    private void generateInitialFood() {
        Food newFood = foodGenerator.generateFood(config, state.getSnake());
        state.setFood(newFood);
    }

    /**
     * Возвращает текущее состояние игры.
     *
     * @return объект {@link GameState}
     */
    public GameState getState() {
        return state;
    }

    /**
     * Изменяет направление движения змеи.
     *
     * @param direction новое направление
     */
    public void changeDirection(MoveDirection direction) {
        state.changeDirection(direction);
    }

    /**
     * Проверяет столкновение со стенами.
     *
     * @param head координаты новой головы
     * @return true, если произошло столкновение со стеной
     */
    private boolean isWallCollision(Coordinates head) {
        return head.x() >= config.width() || head.x() < 0
                || head.y() >= config.height() || head.y() < 0;
    }

    /**
     * Проверяет столкновение змеи с самой собой.
     *
     * @param head координаты новой головы
     * @return true, если змея врезалась в свое тело
     */
    private boolean isSelfCollision(Coordinates head) {
        if (!state.getSnake().contains(head)) {
            return false;
        }

        boolean isTail = head.equals(state.getSnake().getLast());
        boolean isFood = state.getFood() != null
                && head.equals(state.getFood().getPosition());

        return !(isTail && !isFood);
    }

    /**
     * Продвигает логику игры на один шаг вперед (тик).
     */
    private void advanceGame() {
        if (state.getStatus() != GameStatus.PLAYING) {
            return;
        }

        state.applyPendingDirection();
        Coordinates nextHead = state.getNextHeadPosition();

        if (isWallCollision(nextHead) || isSelfCollision(nextHead)) {
            state.setStatus(GameStatus.LOSE);
            return;
        }

        state.getSnake().addFirst(nextHead);

        if (state.getFood() != null
                && nextHead.equals(state.getFood().getPosition())) {
            state.addScore(state.getFood().getPoints());
            state.addGrowth(state.getFood().getGrowth());

            if (winCondition.isWin(state, config)) {
                state.setStatus(GameStatus.WIN);
            } else {
                state.setFood(foodGenerator.generateFood(config, state.getSnake()));
            }
        }

        if (state.getPendingGrowth() > 0) {
            state.decrementGrowth();
        } else {
            state.getSnake().removeLast();
        }
    }

    /**
     * Обработчик такта, вызываемый из {@link GameLoop}.
     */
    private void gameTick() {
        advanceGame();
        GameStatus status = state.getStatus();

        if (status == GameStatus.PLAYING) {
            listener.onGameUpdated();
        } else {
            stop();
            listener.onGameEnded(status);
        }
    }
}
