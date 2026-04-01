import os
content1 = """package ru.nsu.ryzhneva.snake;
import javafx.application.Platform;
public class FxSetup {
    private static boolean initialized = false;
    public static void init() {
        if (!initialized) {
            try { Platform.startup(() -> {}); initialized = true; } 
            catch (IllegalStateException e) { initialized = true; }
        }
    }
}
"""
content2 = """package ru.nsu.ryzhneva.snake.controller;
import javafx.scene.canvas.Canvas;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.snake.FxSetup;
import ru.nsu.ryzhneva.snake.model.BasicFood;
import ru.nsu.ryzhneva.snake.model.GameState;
import ru.nsu.ryzhneva.snake.model.data.Coordinates;
import ru.nsu.ryzhneva.snake.model.data.GameConfig;
import ru.nsu.ryzhneva.snake.model.data.GameStatus;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
class GameRendererTest {
    @BeforeAll static void initJfx() { FxSetup.init(); }
    @Test voimport os
content1 = """pacancontent1asimport javafx.application.Platform;
public erpublic class FxSetup {
    privates,    private static bo G    public static void init() {
        if (!i00        if (!initialized) {
  ew            try { Platform              catch (IllegalStateException e) { initialized = true; } a        }
    }
}
"""
content2 = """package ru.nsu.ryzhneva.snake.oi    }
}
nd}
""erlaco) import javafx.scene.canvas.Canvas;
import org.junit.ju  import org.junit.jupiter.api.Befoenimport org.junit.jupiter.api.Test;
imperimport ru.nsu.ryzhneva.snake.FxSeerimport ru.nsu.ryzhneva.snake.model.B""import ru.nsu.ryzhneva.snake.model.GameStatepoimport ru.nsu.ryzhneva.snake.model.data.Coorrgimport ru.nsu.ryzhneva.snake.model.data.GameConfig;keimport ru.nsu.ryzhneva.snake.model.data.GameStatuscBimport static org.junit.jupiter.api.Assertions.assioclass GameRendererTest {
    @BeforeAll static void initJfx() { F v    @BeforeAll static v.i    @Test voimport os
content1 = """pacancontent1assertDocontent1 = """pacanc  public erpublic class FxSetup {
    privaoolean(false);
             privates,    private statip(        if (!i00        if (!initialized) {
  ew            try { lo  ew            try { Platform             o    }
}
"""
content2 = """package ru.nsu.ryzhneva.snake.oi    }
}
nd}
""erlaco) import javafx.scene.canvas.Ca/r}
""u/rycone}
nd}
""erlaco) import javafx.scene.caava", "w") as f:"".wimport org.junit.ju  import org.junit.jupitnsimperimport ru.nsu.ryzhneva.snake.FxSeerimport ru.nsu.ryzhneva.snake.model.B""import ru.nma    @BeforeAll static void initJfx() { F v  echo "finish test"
kill -9 $$
