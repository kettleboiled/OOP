package ru.nsu.ryzhneva.snake.controller;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ru.nsu.ryzhneva.snake.model.GameService;
import ru.nsu.ryzhneva.snake.model.data.GameConfig;
import ru.nsu.ryzhneva.snake.model.data.GameStatus;
import ru.nsu.ryzhneva.snake.model.data.MoveDirection;

/**
 * Контроллер для взаимодействия
 * пользователя с бизнес логикой.
 */
public class Controller {
    private final GameService gameService;
    private final GameConfig config;

    /**
     * Конструктор.
     *
     * @param gameService сервис, управляющий игрой
     * @param config конфигурация игры
     */
    public Controller(GameService gameService, GameConfig config) {
        this.gameService = gameService;
        this.config = config;
    }

    /**
     * Обрабатывает нажатия клавиш пользователя
     * и передает их в сервис игры.
     *
     * @param event событие нажатия клавиши
     */
    public void handleKeyPress(KeyEvent event) {
        GameStatus currentStatus = gameService.getState().getStatus();
        if (currentStatus == GameStatus.READY) {
            if (event.getCode() == KeyCode.ENTER) {
                gameService.start();
            }
            return;
        }

        if (currentStatus != GameStatus.PLAYING) {
            if (event.getCode() == KeyCode.ENTER) {
                gameService.restart(config);
            }
            return;
        }

        switch (event.getCode()) {
            case UP, W -> gameService.changeDirection(MoveDirection.UP);
            case DOWN, S -> gameService.changeDirection(MoveDirection.DOWN);
            case LEFT, A -> gameService.changeDirection(MoveDirection.LEFT);
            case RIGHT, D -> gameService.changeDirection(MoveDirection.RIGHT);
            default -> { }
        }
    }
}
