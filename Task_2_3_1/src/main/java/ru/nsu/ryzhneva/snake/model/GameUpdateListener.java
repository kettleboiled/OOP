package ru.nsu.ryzhneva.snake.model;

/**
 * Интерфейс слушателя для получения уведомлений об обновлениях состояния игры.
 */
public interface GameUpdateListener {

    /**
     * Вызывается каждый раз, когда происходит изменение состояния игры.
     */
    void onGameUpdated(GameState gameState);

    /**
     * Вызывается, когда игра завершена.
     *
     * @param gameState финальное состояние игры
     */
    void onGameEnded(GameState gameState);
}
