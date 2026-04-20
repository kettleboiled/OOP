package ru.nsu.ryzhneva.snake.model;

import ru.nsu.ryzhneva.snake.model.data.GameConfig;

/**
 * Класс, определяющий условие победы по достижению змейкой определенной длины.
 */
public class LengthWinCondition implements WinCondition {

    @Override
    public boolean isWin(GameState state, GameConfig config) {
        return state.getSnake().size() >= config.lengthWin();
    }
}
