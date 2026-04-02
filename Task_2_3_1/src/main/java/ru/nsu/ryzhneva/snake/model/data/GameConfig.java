package ru.nsu.ryzhneva.snake.model.data;

/**
 * Класс-сущность (рекорд), хранящий настройки конкретной игровой сессии.
 *
 * @param width ширина игрового поля в клетках
 * @param height высота игрового поля в клетках
 * @param lengthWin требуемая длина змеи для победы
 * @param tickMs длительность одного хода игры (в миллисекундах)
 */
public record GameConfig(int width, int height, int lengthWin, double tickMs) {

    /**
     * Конструктор с валидацией конфигурации.
     *
     * @throws IllegalArgumentException если заданы недопустимые или отрицательные размеры
     */
    public GameConfig {
        if (width <= 0 || height <= 0 || lengthWin <= 1
                || lengthWin > width * height || tickMs <= 0.0) {
            throw new IllegalArgumentException("Некорректные значения конфигурации.");
        }
    }
}
