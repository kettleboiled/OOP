package ru.nsu.ryzhneva.snake.view;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import ru.nsu.ryzhneva.snake.controller.Controller;
import ru.nsu.ryzhneva.snake.model.GameService;
import ru.nsu.ryzhneva.snake.model.GameUpdateListener;
import ru.nsu.ryzhneva.snake.model.WinCondition;
import ru.nsu.ryzhneva.snake.model.data.GameConfig;
import ru.nsu.ryzhneva.snake.model.data.GameStatus;
import ru.nsu.ryzhneva.snake.model.food.FoodGenerator;

public class GameView implements GameUpdateListener {
    private GameRenderer renderer;
    private GameService gameService;
    private Controller inputController;

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
    private final int lengthWin;
    private final double tickMs;
    private final FoodGenerator foodGenerator;
    private final WinCondition winCondition;

    /**
     * Конструктор.
     *
     * @param columns ширина поля
     * @param rows высота поля
     * @param sizeCell размер клетки в пикселях
     * @param lengthWin количество сегментов, при котором засчитывается победа
     * @param tickMs длительность одного хода
     * @param foodGenerator генератор еды на поле
     * @param winCondition класс, проверяющий условия победы
     */
    public GameView(int columns, int rows, int sizeCell, int lengthWin, double tickMs,
                    FoodGenerator foodGenerator, WinCondition winCondition) {
        this.columns = columns;
        this.rows = rows;
        this.sizeCell = sizeCell;
        this.lengthWin = lengthWin;
        this.tickMs = tickMs;
        this.foodGenerator = foodGenerator;
        this.winCondition = winCondition;
    }

    /**
     * Инициализация после загрузки FXML.
     * Запускает инициализацию доски, контроллера,
     * событий клавиатуры и игрового цикла.
     */
    @FXML
    public void initialize() {
        final GameConfig config = new GameConfig(columns, rows, lengthWin, tickMs);

        canvas.setWidth(columns * sizeCell);
        canvas.setHeight(rows * sizeCell);
        renderer = new GameRenderer(canvas, sizeCell, columns, rows);

        gameService = new GameService(config, this, foodGenerator, winCondition);

        inputController = new Controller(gameService, config);

        root.setFocusTraversable(true);
        root.setOnMouseClicked(event -> root.requestFocus());
        root.setOnKeyPressed(inputController::handleKeyPress);

        renderer.render(gameService.getState());
        renderer.renderOverlay();
        showMessage("Нажмите ENTER для запуска");

        scoreLabel.setText("Score: " + gameService.getState().getScore());

        root.widthProperty().addListener((obs, oldVal, newVal) -> resizeCanvas());
        root.heightProperty().addListener((obs, oldVal, newVal) -> resizeCanvas());
    }

    /**
     * Событие, вызываемое игрой при каждом успешном игровом тике.
     * Обновляет интерфейс.
     */
    @Override
    public void onGameUpdated() {
        renderer.render(gameService.getState());
        scoreLabel.setText("Score: " + gameService.getState().getScore());

        if (gameService.getState().getStatus() == GameStatus.READY) {
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
     * @param status статус окончания игры
     */
    @Override
    public void onGameEnded(GameStatus status) {
        renderer.render(gameService.getState());
        renderer.renderOverlay();

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

        int newSizeCell = (int)Math.min(availableWidth / columns, availableHeight / rows);

        renderer.setSizeCell(newSizeCell);
        canvas.setHeight(rows * newSizeCell);
        canvas.setWidth(columns * newSizeCell);

        if (gameService != null && gameService.getState() != null) {
            renderer.render(gameService.getState());

            if (gameService.getState().getStatus() != GameStatus.PLAYING) {
                renderer.renderOverlay();
            }
        }
    }

}
