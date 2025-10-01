package ru.nsu.ryzhneva.operation;

import ru.nsu.ryzhneva.Expression;
import ru.nsu.ryzhneva.operation.types.Add;
import ru.nsu.ryzhneva.operation.types.Div;
import ru.nsu.ryzhneva.operation.types.Mul;
import ru.nsu.ryzhneva.operation.types.Sub;

/**
 * Перечисление, представляющее математические операторы.
 * Хранит символ оператора и логику для создания соответствующей операции.
 */
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

    private final String symbol;

    Operator(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return this.symbol;
    }

    /**
     * Находит оператор по его строковому символу.
     *
     * @param symbol Символ для поиска (например, "+").
     * @return Найденный элемент Operator.
     * @throws IllegalArgumentException если оператор не найден.
     */
    public static Operator fromString(String symbol) {
        for (Operator operator : Operator.values()) {
            if (operator.getSymbol().equals(symbol)) {
                return operator;
            }
        }
        throw new IllegalArgumentException("Незнакомый оператор: " + symbol);
    }
}