package ru.nsu.ryzhneva.operation;

import ru.nsu.ryzhneva.Expression;
import ru.nsu.ryzhneva.values.Number;

public class Add extends Operation {

    public Add(Expression left, Expression right) {
        super(left, right, "+");
    }

    @Override
    public double applyOperation(double left, double right) {
        return left + right;
    }

    @Override
    public Expression derivative(String varName) {
        return new Add(left.derivative(varName), right.derivative(varName));
    }

    @Override
    public Expression funcSimple() {
        Expression simpleLeft = left.funcSimple();
        Expression simpleRight = right.funcSimple();

        // Если левая часть выражения равна 0, возвращаем правую часть
        if (simpleLeft instanceof Number && ((Number) simpleLeft).getValue() == 0) {
            return simpleRight;
        }
        // Если правая часть выражения равна 0, возвращаем левую часть
        if (simpleRight instanceof Number && ((Number) simpleRight).getValue() == 0) {
            return simpleLeft; // x + 0 = x
        }
        // Если обе части выражения - числа, складываем их
        if (simpleLeft instanceof Number && simpleRight instanceof Number) {
            double leftValue = ((Number) simpleLeft).getValue();
            double rightValue = ((Number) simpleRight).getValue();
            return new Number(leftValue + rightValue);
        }
        // Если не удалось упростить выражение, возвращаем его как есть
        return new Add(simpleLeft, simpleRight);
    }
}
