package ru.nsu.ryzhneva.snake.view;

import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import ru.nsu.ryzhneva.snake.model.GameState;
import ru.nsu.ryzhneva.snake.model.GameUpdateListener;
import ru.nsu.ryzhneva.snake.model.data.GameStatus;

/**
 * Класс, отвечающий за отображение игрового интерфейса.
 */
public class GameView implements GameUpdateListener {
    private GameRenderer renderer;
    private GameState currentState;
    private Consumer<KeyEvent> keyPressHandler;

    @FXML
    private VBox root;
    @FXML
    private Canvas canvas;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label messageLabel;

    private final int columns;
    private final int rows;
    private final int sizeCell;

    /**
     * Конструктор.
     *
     * @param columns ширина поля
     * @param rows высота поля
     * @param sizeCell размер клетки в пикселях
     */
    public GameView(int columns, int rows, int sizeCell) {
        this.columns = columns;
        this.rows = rows;
        this.sizeCell = sizeCell;
    }

    /**
     * Устанавливает слушатель для нажатий клавиш.
     * 
     * @param handler обработчик
     */
    public void setKeyPressHandler(Consumer<KeyEvent> handler) {
        this.keyPressHandler = handler;
        if (root != null && handler != null) {
            root.setOnKeyPressed(event -> keyPressHandler.accept(event));
        }
    }

    /**
     * Устанавливает начальное состояние и отрисовывает его.
     * 
     * @param state начальное состояние
     */
    public void setInitialState(GameState state) {
        this.currentState = state;
        if (renderer != null && state != null) {
            renderer.render(currentState);
            renderer.renderOverlay();
            showMessage("Нажмите ENTER для запуска");
            scoreLabel.setText("Score: " + currentState.getScore());
        }
    }

    /**
     * Инициализация после загрузки FXML.
     * Запускает инициализацию доски, контроллера,
     * событий клавиатуры и игрового цикла.
     */
    @FXML
    public void initialize() {
        canvas.setWidth(columns * sizeCell);
        canvas.setHeight(rows * sizeCell);
        renderer = new GameRenderer(canvas, sizeCell, columns, rows);

        root.setFocusTraversable(true);
        root.setOnMouseClicked(event -> root.requestFocus());
        
        if (keyPressHandler != null) {
            root.setOnKeyPressed(event -> keyPressHandler.accept(event));
        }

        if (currentState != null) {
            renderer.render(currentState);
            renderer.renderOverlay();
            showMessage("Нажмите ENTER для запуска");
            scoreLabel.setText("Score: " + currentState.getScore());
        }

        root.widthProperty().addListener((obs, oldVal, newVal) -> resizeCanvas());
        root.heightProperty().addListener((obs, oldVal, newVal) -> resizeCanvas());
    }

    /**
     * Событие, вызываемое игрой при каждом успешном игровом тике.
     * Обновляет интерфейс.
     */
    @Override
    public void onGameUpdated(GameState gameState) {
        this.currentState = gameState;
        renderer.render(gameState);
        scoreLabel.setText("Score: " + gameState.getScore());

        if (gameState.getStatus() == GameStatus.READY) {
            renderer.renderOverlay();
            showMessage("Нажмите ENTER для запуска");
        } else {
            if (messageLabel.isVisible()) {
                messageLabel.setVisible(false);
            }
        }
    }

    /**
     * Событие, вызываемое игрой при ее окончании.
     * Отображает экран победы или поражения.
     *
     * @param gameState финальное состояние игры
     */
    @Override
    public void onGameEnded(GameState gameState) {
        this.currentState = gameState;
        renderer.render(gameState);
        renderer.renderOverlay();

        GameStatus status = gameState.getStatus();
        if (status == GameStatus.WIN) {
            showMessage("YOU WIN!\nНажмите ENTER для запуска");
        } else if (status == GameStatus.LOSE) {
            showMessage("YOU LOSE\nНажмите ENTER для запуска");
        }
    }

    /**
     * Отображает заданное сообщение на экране.
     *
     * @param text текст сообщения
     */
    private void showMessage(String text) {
        messageLabel.setVisible(true);
        messageLabel.setText(text);
        messageLabel.setTextFill(Color.WHITE);
    }

    /**
     * Высчитывает размер клетки для изменненного окна игры
     * и дает команду перерисовать поле.
     */
    private void resizeCanvas() {
        double availableWidth = root.getWidth() - 20;
        double availableHeight = root.getHeight() - scoreLabel.getHeight() - 40;

        if (availableWidth <= 0 || availableHeight <= 0) {
            return;
        }

        int newSizeCell = (int) Math.min(availableWidth / columns, availableHeight / rows);

        renderer.setSizeCell(newSizeCell);
        canvas.setHeight(rows * newSizeCell);
        canvas.setWidth(columns * newSizeCell);

        if (currentState != null) {
            renderer.render(currentState);

            if (currentState.getStatus() != GameStatus.PLAYING) {
                renderer.renderOverlay();
            }
        }
    }

}
