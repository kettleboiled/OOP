package ru.nsu.ryzhneva.snake.model.data;

/**
 * Конфиг, хранящий настройки конкретной игровой сессии.
 *
 * @param width ширина игрового поля в клетках
 * @param height высота игрового поля в клетках
 * @param lengthWin требуемая длина змеи для победы
 * @param tickMs длительность одного хода игры
 */
public record GameConfig(int width, int height, int lengthWin, double tickMs) {

    /**
     * Конструктор с валидацией конфигурации.
     *
     * @throws IllegalArgumentException если заданы недопустимые
     * или отрицательные размеры или параметры
     */
    public GameConfig {
        if (width <= 0 || height <= 0 || lengthWin <= 1 || lengthWin > width * height || tickMs <= 0.0) {
            throw new IllegalArgumentException("Некорректные значения конфигурации.");
        }
    }
}
