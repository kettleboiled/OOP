package ru.nsu.ryzhneva.operation.types;

import ru.nsu.ryzhneva.Expression;
import ru.nsu.ryzhneva.operation.Operation;
import ru.nsu.ryzhneva.operation.Operator;
import ru.nsu.ryzhneva.values.Number;

/**
 * Класс, представляющий операцию умножения двух выражений.
 */
public class Mul extends Operation {

    /**
     * Конструктор класса.
     *
     * @param left Левый операнд.
     * @param right Правый операнд.
     */
    public Mul(Expression left, Expression right) {
        super(left, right, Operator.MUL);
    }

    /**
     * Операция умножения двух выражений.
     *
     * @param left Делимое.
     * @param right Делитель.
     * @return Результат.
     */
    @Override
    protected double applyOperation(double left, double right) {
        return left * right;
    }

    /**
     * Выполняет дифференцирование по правилу произведения: (f*g)' = (f'g + fg').
     *
     * @param varName Имя переменной, по которой производится дифференцирование.
     * @return Новое выражение, представляющее собой производную.
     */
    @Override
    public Expression derivative(String varName) {
        return new Add(new Mul(left.derivative(varName), right),
                new Mul(left, right.derivative(varName))
        );
    }

    /**
     * Упрощает выражение умножения.
     *
     * @return Новое, упрощенное выражение.
     */
    @Override
    public Expression funcSimple() {
        Expression simplifiedLeft = left.funcSimple();
        Expression simplifiedRight = right.funcSimple();

        if ((simplifiedLeft instanceof Number && ((Number) simplifiedLeft).getValue() == 0)
                || (simplifiedRight instanceof Number && ((Number) simplifiedRight).getValue() == 0)) {
            return new Number(0);
        }

        if (simplifiedLeft instanceof Number && ((Number) simplifiedLeft).getValue() == 1) {
            return simplifiedRight;
        }
        if (simplifiedRight instanceof Number && ((Number) simplifiedRight).getValue() == 1) {
            return simplifiedLeft;
        }

        if (simplifiedLeft instanceof Number && simplifiedRight instanceof Number) {
            return new Number(((Number) simplifiedLeft).getValue() * ((Number) simplifiedRight).getValue());
        }
        return new Mul(simplifiedLeft, simplifiedRight);
    }
}
