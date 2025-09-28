package ru.nsu.ryzhneva.values;

import ru.nsu.ryzhneva.Expression;
import java.util.Map;

public class Number extends Expression {
    private final double value;

    public Number(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
    @Override
    public Expression funcSimple() {
        return this;
    }

    @Override
    public Expression derivative(String varName) {
        return new Number(0);
    }

    @Override
    public double eval(Map<String, Double> variables) {
        return value;
    }

    @Override
    public String print() {
        if (value == (int) value) {
            return String.format("%d", (int) value);
        }
        return String.valueOf(value);
    }
}
