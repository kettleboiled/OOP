package ru.nsu.ryzhneva.operation;

/**
 * Перечисление, представляющее математические операторы.
 * Хранит символ оператора и его приоритет.
 */
public enum Operator {
    ADD("+", 1),
    SUB("-", 1),
    MUL("*", 2),
    DIV("/", 2);

    private final String symbol;
    private final int precedence;

    Operator(String symbol, int precedence) {
        this.symbol = symbol;
        this.precedence = precedence;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getPrecedence() {
        return precedence;
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
        for (Operator op : Operator.values()) {
            if (op.getSymbol().equals(symbol)) {
                return op;
            }
        }
        throw new IllegalArgumentException("Незнакомый оператор: " + symbol);
    }
}