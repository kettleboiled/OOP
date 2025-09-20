package ru.nsu.ryzhneva;

/**
 * Точка входа в программу.
 * Запускает игру Блэкджек.
 */
public class Main {

    /**
     * Главный метод программы.
     *
     * @param args аргументы командной строки.
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}
