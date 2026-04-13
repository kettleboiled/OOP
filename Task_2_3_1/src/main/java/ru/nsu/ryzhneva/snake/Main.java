package ru.nsu.ryzhneva.snake;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import ru.nsu.ryzhneva.snake.controller.Controller;
import ru.nsu.ryzhneva.snake.model.GameService;
import ru.nsu.ryzhneva.snake.model.LengthWinCondition;
import ru.nsu.ryzhneva.snake.model.data.GameConfig;
import ru.nsu.ryzhneva.snake.model.food.RandomFoodGenerator;
import ru.nsu.ryzhneva.snake.view.GameView;

/**
 * Точка входа.
 */
public class Main extends Application {

    /**
     * Основной метод запуска окна и сцены JavaFX.
     *
     * @param stage сцена
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("snake-view1.fxml"));

            GameView view = new GameView(30, 20, 20);

            GameConfig config = new GameConfig(30, 20, 50, 150.0);
            GameService gameService = new GameService(config, view,
                    new RandomFoodGenerator(), new LengthWinCondition());
            Controller controller = new Controller(gameService, config);

            view.setInitialState(gameService.getState());
            view.setKeyPressHandler(controller::handleKeyPress);

            fxmlLoader.setControllerFactory(c -> view);

            Scene scene = new Scene(fxmlLoader.load());

            scene.getRoot().requestFocus();

            stage.setTitle("Snake Game");
            stage.setMinWidth(400);
            stage.setMinHeight(400);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.err.println("Ошибка при запуске приложения: " + e.getMessage());
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка запуска");
            alert.setHeaderText("Не удалось запустить игру");
            alert.setContentText("Произошла ошибка: " + e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Точка входа.
     *
     * @param args параметры командной строки
     */
    public static void main(String[] args) {
        launch(args);
    }
}