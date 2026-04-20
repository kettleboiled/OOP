package ru.nsu.ryzhneva.snake.model;

import ru.nsu.ryzhneva.snake.model.data.GameConfig;

/**
 * Интерфейс, описывающий условие победы в игре.
 */
public interface WinCondition {

    /**
     * Проверяет, удовлетворены ли условия для победы в текущем состоянии игры.
     *
     * @param state текущее состояние игры
     * @param config конфигурация игры
     * @return true, если условие победы выполнено; иначе false
     */
    boolean isWin(GameState state, GameConfig config);
}
