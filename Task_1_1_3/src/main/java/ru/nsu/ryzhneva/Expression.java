package ru.nsu.ryzhneva;

import java.util.Map;

/**
 * Абстрактный базовый класс для всех математических выражений.
 */
public abstract class Expression {
    /**
     * Выполняет символьное дифференцирование по указанной переменной.
     * @param varName Имя переменной.
     * @return Новое выражение, представляющее собой производную.
     */
    public abstract Expression derivative(String varName);

    /**
     * Вычисляет значение выражения с заданными значениями переменных.
     * @param variables Переменные со своими значениями.
     * @return Результат вычисления.
     */
    public abstract double eval(Map<String, Double> variables);

    /**
     * @return Выражение в строковом представлении.
     */
    public abstract String print();

    /**
     * Упрощает текущее выражение согласно заданным правилам.
     * @return Новое, упрощенное выражение.
     */
    public abstract Expression funcSimple();

}
