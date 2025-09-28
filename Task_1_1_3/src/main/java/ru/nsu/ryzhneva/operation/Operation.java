package ru.nsu.ryzhneva.operation;

import ru.nsu.ryzhneva.Expression;

import java.util.Map;

public abstract class Operation extends Expression {
    protected Expression left;
    protected Expression right;
    protected String operator;

    public Operation(Expression left, Expression right, String operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    @Override
    public String print() {
        return "(" + left.print() + operator + right.print() + ")";
    }

    protected abstract double applyOperation(double left, double right);

    @Override
    public double eval(Map<String, Double> variables) {
        double resLeft = left.eval(variables);
        double resRight = right.eval(variables);
        return applyOperation(resLeft, resRight);
    }

}
