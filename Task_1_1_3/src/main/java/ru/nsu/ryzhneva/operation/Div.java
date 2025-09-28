package ru.nsu.ryzhneva.operation;

import ru.nsu.ryzhneva.Expression;
import ru.nsu.ryzhneva.values.Number;

/**
 * Класс, представляющий операцию деления двух выражений.
 */
public class Div extends Operation {

    /**
     * Конструктор класса.
     *
     * @param left Левый операнд.
     * @param right Правый операнд.
     */
    public Div(Expression left, Expression right) {
        super(left, right, "/");
    }

    /**
     * Операция деления двух выражений.
     *
     * @param left Делимое.
     * @param right Делитель.
     * @return Результат.
     */
    @Override
    public double applyOperation(double left, double right) {
        // Защита от деления на ноль
        if (right == 0) {
            throw new ArithmeticException("Деление на ноль.");
        }
        return left / right;
    }

    /**
     * Выполняет дифференцирование по правилу частного: (f/g)' = (f'g - fg') / g^2.
     *
     * @param varName Имя переменной, по которой производится дифференцирование.
     * @return Новое выражение, представляющее собой производную.
     */
    @Override
    public Expression derivative(String varName) {
        // f'g
        Expression fPrimeG = new Mul(left.derivative(varName), right);
        // fg'
        Expression fgPrime = new Mul(left, right.derivative(varName));
        // f'g - fg'
        Expression numerator = new Sub(fPrimeG, fgPrime);
        // g^2
        Expression denominator = new Mul(right, right);

        return new Div(numerator, denominator);
    }

    /**
     * Упрощает выражение деления.
     * 
     * @return Новое, упрощенное выражение.
     */
    @Override
    public Expression funcSimple() {
        Expression simpleLeft = left.funcSimple();
        Expression simpleRight = right.funcSimple();

        // ПРАВИЛО: 0 / x = 0 (если x не 0)
        if (simpleLeft instanceof Number && ((Number) simpleLeft).getValue() == 0) {
            return new Number(0);
        }

        // ПРАВИЛО: x / 1 = x
        if (simpleRight instanceof Number && ((Number) simpleRight).getValue() == 1) {
            return simpleLeft;
        }

        // ПРАВИЛО: x / x = 1 (если x не 0)
        if (simpleLeft.equals(simpleRight)) {
            return new Number(1);
        }

        // ПРАВИЛО: вычисление констант (a / b)
        if (simpleLeft instanceof Number && simpleRight instanceof Number) {
            double rightValue = ((Number) simpleRight).getValue();
            if (rightValue == 0) {
                // Если знаменатель 0, не упрощаем, чтобы избежать ошибки
                return new Div(simpleLeft, simpleRight);
            }
            double result = ((Number) simpleLeft).getValue() / rightValue;
            return new Number(result);
        }

        // Если упростить не удалось, возвращаем новое деление с упрощенными частями
        return new Div(simpleLeft, simpleRight);
    }
}