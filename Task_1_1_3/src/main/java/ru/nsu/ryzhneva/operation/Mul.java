package ru.nsu.ryzhneva.operation;

import ru.nsu.ryzhneva.Expression;
import ru.nsu.ryzhneva.values.Number;

public class Mul extends Operation {
    public Mul(Expression left, Expression right) {
        super(left, right, "*");
    }

    @Override
    protected double applyOperation(double left, double right) {
        return left * right;
    }

    @Override
    public Expression derivative(String varName) {
        return new Add(new Mul(left.derivative(varName), right), new Mul(left, right.derivative(varName))
        );
    }

    @Override
    public Expression funcSimple() {
        Expression simplifiedLeft = left.funcSimple();
        Expression simplifiedRight = right.funcSimple();

        // Умножение на 0
        if ((simplifiedLeft instanceof Number && ((Number) simplifiedLeft).getValue() == 0) ||
                (simplifiedRight instanceof Number && ((Number) simplifiedRight).getValue() == 0)) {
            return new Number(0);
        }
        // Умножение на 1
        if (simplifiedLeft instanceof Number && ((Number) simplifiedLeft).getValue() == 1) {
            return simplifiedRight;
        }
        if (simplifiedRight instanceof Number && ((Number) simplifiedRight).getValue() == 1) {
            return simplifiedLeft;
        }
        // Вычисление констант
        if (simplifiedLeft instanceof Number && simplifiedRight instanceof Number) {
            return new Number(((Number) simplifiedLeft).getValue() * ((Number) simplifiedRight).getValue());
        }
        return new Mul(simplifiedLeft, simplifiedRight);
    }
}
