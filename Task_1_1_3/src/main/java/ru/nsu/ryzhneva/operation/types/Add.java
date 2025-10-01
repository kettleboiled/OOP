package ru.nsu.ryzhneva.operation.types;

import ru.nsu.ryzhneva.Expression;
import ru.nsu.ryzhneva.operation.Operation;
import ru.nsu.ryzhneva.operation.Operator;
import ru.nsu.ryzhneva.values.Number;

/**
 * Класс, представляющий операцию сложения двух выражений.
 */
public class Add extends Operation {

    /**
     * Конструктор класса.
     *
     * @param left Левый операнд.
     * @param right Правый операнд.
     */
    public Add(Expression left, Expression right) {
        super(left, right, Operator.ADD);
    }

    /**
     * Операция сложения двух выражений.
     *
     * @param left Левое слагаемое.
     * @param right Правое слагаемое.
     * @return Результат.
     */
    @Override
    public double applyOperation(double left, double right) {
        return left + right;
    }

    /**
     * Выполняет дифференцирование по правилу сложения: (f+g)' = (f' + g').
     *
     * @param varName Имя переменной, по которой производится дифференцирование.
     * @return Новое выражение, представляющее собой производную.
     */
    @Override
    public Expression derivative(String varName) {
        return new Add(left.derivative(varName), right.derivative(varName));
    }

    /**
     * Упрощает выражение сложения.
     *
     * @return Новое, упрощенное выражение.
     */
    @Override
    public Expression funcSimple() {
        Expression simpleLeft = left.funcSimple();
        Expression simpleRight = right.funcSimple();

        if (simpleLeft instanceof Number && ((Number) simpleLeft).getValue() == 0) {
            return simpleRight;
        }

        if (simpleRight instanceof Number && ((Number) simpleRight).getValue() == 0) {
            return simpleLeft;
        }

        if (simpleLeft instanceof Number && simpleRight instanceof Number) {
            double leftValue = ((Number) simpleLeft).getValue();
            double rightValue = ((Number) simpleRight).getValue();
            return new Number(leftValue + rightValue);
        }

        return new Add(simpleLeft, simpleRight);
    }
}
