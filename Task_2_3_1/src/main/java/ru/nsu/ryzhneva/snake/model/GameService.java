package ru.nsu.ryzhneva.snake.model;

import ru.nsu.ryzhneva.snake.controller.GameLoop;
import ru.nsu.ryzhneva.snake.model.data.GameConfig;
import ru.nsu.ryzhneva.snake.model.data.GameStatus;
import ru.nsu.ryzhneva.snake.model.data.MoveDirection;
import ru.nsu.ryzhneva.snake.model.data.Coordinates;

public class GameService {
    private GameState state;
    private final GameUpdateListener listener;
    private final GameLoop gameLoop;
    private final FoodGenerator foodGenerator;
    private final WinCondition winCondition;
    private final GameConfig config;

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

    public void start() {
        gameLoop.start();
    }

    public void stop() {
        gameLoop.stop();
    }

    public void restart(GameConfig config) {
        this.state = new GameState(config);
        generateInitialFood();
        gameLoop.restart();
    }

    private void generateInitialFood() {
        Food newFood = foodGenerator.generateFood(config, state.getSnake());
        state.setFood(newFood);
    }

    public GameState getState() {
        return state;
    }

    public void changeDirection(MoveDirection direction) {
        state.changeDirection(direction);
    }

    private boolean isWallCollision(Coordinates head) {
        return head.x() >= config.width() || head.x() < 0
                || head.y() >= config.height() || head.y() < 0;
    }

    private boolean isSelfCollision(Coordinates head) {
        if (!state.getSnake().contains(head)) {
            return false;
        }

        boolean isTail = head.equals(state.getSnake().getLast());
        boolean isFood = state.getFood() != null
                && head.equals(state.getFood().getPosition());

        return !(isTail && !isFood);
    }

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

    private void gameTick() {
        advanceGame();
        GameStatus status = state.getStatus();

        if (status == GameStatus.PLAYING) {
            listener.onGameUpdated();
        } else {
            listener.onGameEnded(status);
        }
    }
}
