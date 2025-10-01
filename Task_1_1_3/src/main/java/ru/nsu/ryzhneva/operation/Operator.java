package ru.nsu.ryzhneva.operation;


import ru.nsu.ryzhneva.Expression;
import ru.nsu.ryzhneva.operation.types.Add;
import ru.nsu.ryzhneva.operation.types.Div;
import ru.nsu.ryzhneva.operation.types.Mul;
import ru.nsu.ryzhneva.operation.types.Sub;

public enum Operator {
    ADD("+") {
        @Override
        public Operation createOperation(Expression left, Expression right) {
            return new Add(left, right);
        }
    },

    SUB("-") {
        @Override
        public Operation createOperation(Expression left, Expression right) {
            return new Sub(left, right);
        }
    },

    MUL("*") {
        @Override
        public Operation createOperation(Expression left, Expression right) {
            return new Mul(left, right);
        }
    },

    DIV("/") {
        @Override
        public Operation createOperation(Expression left, Expression right) {
            return new Div(left, right);
        }
    };

    public abstract Operation createOperation(Expression left, Expression right);

    private String symbol;

    Operator(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return getSymbol();
    }

    public Operator fromString(String symbol) {
        for(Operator operator: Operator.values()) {
            if (operator.getSymbol().equals(symbol)) {
                return operator;
            }
        }
        throw new IllegalArgumentException("Незнакомый оператор: " + symbol);
    }
}
