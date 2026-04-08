package ru.nsu.ryzhneva.snake;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nsu.ryzhneva.snake.model.LengthWinCondition;
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
     * @throws IOException если файл FXML не может быть загружен
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("snake-view.fxml"));

        fxmlLoader.setControllerFactory(c -> new GameView(
                30, 20, 20, 50, 150.0,
                new RandomFoodGenerator(),
                new LengthWinCondition()
        ));

        Scene scene = new Scene(fxmlLoader.load());

        scene.getRoot().requestFocus();

        stage.setTitle("Snake Game");
        stage.setMinWidth(400);
        stage.setMinHeight(400);
        stage.setScene(scene);
        stage.show();
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