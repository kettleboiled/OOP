package ru.nsu.ryzhneva.operation.types;

import ru.nsu.ryzhneva.Expression;
import ru.nsu.ryzhneva.operation.Operation;
import ru.nsu.ryzhneva.operation.Operator;
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
        super(left, right, Operator.DIV);
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
        Expression fPrimeG = new Mul(left.derivative(varName), right);
        Expression fgPrime = new Mul(left, right.derivative(varName));
        Expression numerator = new Sub(fPrimeG, fgPrime);
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

        if (simpleLeft instanceof Number && ((Number) simpleLeft).getValue() == 0) {
            return new Number(0);
        }
        if (simpleRight instanceof Number && ((Number) simpleRight).getValue() == 1) {
            return simpleLeft;
        }
        if (simpleLeft.equals(simpleRight)) {
            return new Number(1);
        }
        if (simpleLeft instanceof Number && simpleRight instanceof Number) {
            double rightValue = ((Number) simpleRight).getValue();
            if (rightValue != 0) {
                double result = ((Number) simpleLeft).getValue() / rightValue;
                return new Number(result);
            }
        }

        return new Div(simpleLeft, simpleRight);
    }
}