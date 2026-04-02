package ru.nsu.ryzhneva.snake.model;

import ru.nsu.ryzhneva.snake.model.data.GameStatus;

/**
 * Интерфейс слушателя для получения уведомлений об обновлениях состояния игры.
 */
public interface GameUpdateListener {

    /**
     * Вызывается каждый раз, когда происходит изменение состояния игры.
     */
    void onGameUpdated();

    /**
     * Вызывается, когда игра завершена.
     *
     * @param status финальный статус игры
     */
    void onGameEnded(GameStatus status);
}
