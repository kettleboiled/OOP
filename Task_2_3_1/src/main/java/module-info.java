module ru.nsu.ryzhneva.snake {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens ru.nsu.ryzhneva.snake to javafx.fxml;
    opens ru.nsu.ryzhneva.snake.controller to javafx.fxml;
    exports ru.nsu.ryzhneva.snake;
    exports ru.nsu.ryzhneva.snake.controller;
    exports ru.nsu.ryzhneva.snake.model;
    exports ru.nsu.ryzhneva.snake.model.data;
    exports ru.nsu.ryzhneva.snake.model.food;
    exports ru.nsu.ryzhneva.snake.view;
    opens ru.nsu.ryzhneva.snake.view to javafx.fxml;
}