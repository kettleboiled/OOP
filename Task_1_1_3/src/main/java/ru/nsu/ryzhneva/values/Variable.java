package ru.nsu.ryzhneva.values;

import ru.nsu.ryzhneva.Expression;

import java.util.Map;

public class Variable extends Expression {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    public String getVar() {
        return name;
    }
    @Override
    public Expression funcSimple() {
        return this;
    }

    @Override
    public Expression derivative(String varName) {
        return new Number(this.name.equals(varName) ? 1: 0);
    }

    @Override
    public double eval(Map<String, Double> variables) {
        if (!variables.containsKey(name)) {
            throw new IllegalArgumentException("Значение для переменной '" + name + "' не задано.");
        }
        return variables.get(name);
    }

    @Override
    public String print() {
        return name;
    }
}
