package ru.nsu.ryzhneva.snake.controller;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import ru.nsu.ryzhneva.snake.model.FoodGenerator;
import ru.nsu.ryzhneva.snake.model.GameService;
import ru.nsu.ryzhneva.snake.model.GameUpdateListener;
import ru.nsu.ryzhneva.snake.model.WinCondition;
import ru.nsu.ryzhneva.snake.model.data.GameConfig;
import ru.nsu.ryzhneva.snake.model.data.GameStatus;
import ru.nsu.ryzhneva.snake.model.data.MoveDirection;

public class Controller implements GameUpdateListener {
    private GameService gameService;
    private GameRenderer renderer;

    @FXML
    private VBox root;
    @FXML
    private Canvas canvas;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label messageLabel;

    private final int sizeCell;
    private final int rows;
    private final int columns;
    private final int lengthWin;
    private final double tickMs;
    private final FoodGenerator foodGenerator;
    private final WinCondition winCondition;

    public Controller(int columns, int rows,
                      int sizeCell, int lengthWin, double tickMs,
                      FoodGenerator foodGenerator,
                      WinCondition winCondition) {
        this.columns = columns;
        this.rows = rows;
        this.sizeCell = sizeCell;
        this.lengthWin = lengthWin;
        this.tickMs = tickMs;
        this.foodGenerator = foodGenerator;
        this.winCondition = winCondition;
    }

    @FXML
    public void initialize() {
        GameConfig config = new GameConfig(columns, rows, lengthWin, tickMs);

        canvas.setWidth(columns * sizeCell);
        canvas.setHeight(rows * sizeCell);
        
        renderer = new GameRenderer(canvas, sizeCell, columns, rows);

        root.setFocusTraversable(true);
        root.setOnMouseClicked(event -> root.requestFocus());
        root.setOnKeyPressed(this::handleKeyPress);

        gameService = new GameService(config, this, foodGenerator, winCondition);

        messageLabel.setVisible(false);
        gameService.start();
    }

    @Override
    public void onGameUpdated() {
        updateUI();
    }

    @Override
    public void onGameEnded(GameStatus status) {
        if (status == GameStatus.WIN) {
            endGame("YOU WIN!\nНажмите ENTER для перезапуска", Color.WHITE);
        } else if (status == GameStatus.LOSE) {
            endGame("YOU LOSE\nНажмите ENTER для перезапуска", Color.WHITE);
        }
    }

    private void endGame(String message, Color color) {
        gameService.stop();
        updateUI();
        renderer.renderOverlay();
        messageLabel.setText(message);
        messageLabel.setTextFill(color);
        messageLabel.setVisible(true);
    }


    private void handleKeyPress(KeyEvent event) {
        if (gameService.getState().getStatus() != GameStatus.PLAYING) {
            if (event.getCode() == KeyCode.ENTER) {
                restartGame();
            }
            return;
        }

        switch (event.getCode()) {
            case UP, W -> gameService.changeDirection(MoveDirection.UP);
            case DOWN, S -> gameService.changeDirection(MoveDirection.DOWN);
            case LEFT, A -> gameService.changeDirection(MoveDirection.LEFT);
            case RIGHT, D -> gameService.changeDirection(MoveDirection.RIGHT);
            default -> {}
        }
    }


    private void updateUI() {
        renderer.render(gameService.getState());
        scoreLabel.setText("Score: " + gameService.getState().getScore());
    }

    private void restartGame() {
        GameConfig config = new GameConfig(columns, rows, lengthWin, tickMs);

        messageLabel.setVisible(false);

        gameService.restart(config);
    }
}
