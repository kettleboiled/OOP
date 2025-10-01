package ru.nsu.ryzhneva;

import java.util.Map;

/**
 * Абстрактный базовый класс для всех математических выражений.
 */
public abstract class Expression {

    /**
     * Выполняет символьное дифференцирование по указанной переменной.
     *
     * @param varName Имя переменной.
     * @return Новое выражение, представляющее собой производную.
     */
    public abstract Expression derivative(String varName);

    /**
     * Вычисляет значение выражения с заданными значениями переменных.
     *
     * @param variables Переменные со своими значениями.
     * @return Результат вычисления.
     */
    public abstract double eval(Map<String, Double> variables);

    /**
     * Вычисляет значение выражения по строке с переменными.
     * @param assignments Строка вида "x=10; y=5"
     * @return Результат вычисления.
     */
    public double eval(String assignments) {
        java.util.Map<String, Double> variables = new java.util.HashMap<>();
        if (assignments != null && !assignments.isEmpty()) {
            String[] pairs = assignments.split(";");
            for (String pair : pairs) {
                String trimmedPair = pair.trim();
                if (trimmedPair.isEmpty()) {
                    continue;
                }
                String[] parts = trimmedPair.split("=");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Неверный формат присваивания: " + trimmedPair);
                }
                String key = parts[0].trim();
                String value = parts[1].trim();
                if (key.isEmpty() || value.isEmpty()) {
                    throw new IllegalArgumentException("Пустое имя или значение переменной в: " + trimmedPair);
                }
                try {
                    variables.put(key, Double.parseDouble(value));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Неверный формат числа в: " + trimmedPair, e);
                }
            }
        }
        return this.eval(variables);
    }

    /**
     * Вывод строки.
     *
     * @return Выражение в строковом представлении.
     */
    public abstract String print();

    /**
     * Упрощает текущее выражение согласно заданным правилам.
     *
     * @return Новое, упрощенное выражение.
     */
    public abstract Expression funcSimple();

}
