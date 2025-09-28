package ru.nsu.ryzhneva;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;
import ru.nsu.ryzhneva.operation.Add;
import ru.nsu.ryzhneva.operation.Div;
import ru.nsu.ryzhneva.operation.Mul;
import ru.nsu.ryzhneva.operation.Sub;
import ru.nsu.ryzhneva.values.Number;
import ru.nsu.ryzhneva.values.Variable;

/**
 * Класс для парсинга математических выражений.
 * Реализует алгоритм Шунтинга для преобразования
 * выражений в постфиксную нотацию (RPN),
 * а затем строит дерево выражений из полученной RPN.
 */
public class ExpressionParser {

    /**
     * Метод для определения приоритета операторов.
     *
     * @param operator Оператор.
     * @return Приоритет оператора.
     */
    private int getPrecedence(String operator) {
        switch (operator) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            default:
                return 0; // Для скобок
        }
    }

    /**
     * Метод токенизации строки.
     * Разбивает строку на отдельные токены
     * (операторы, операнды и скобки).
     *
     * @param expression Строковое математическое выражение.
     * @return Список токенов.
     */
    private List<String> tokenize(String expression) {
        List<String> tokens = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(expression, "()+-*/", true);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken().trim();
            if (!token.isEmpty()) {
                tokens.add(token);
            }
        }
        return tokens;
    }

    /**
     * Метод преобразования в постфиксную нотацию (RPN)
     * с использованием алгоритма Шунтинга.
     *
     * @param tokens Список токенов.
     * @return Список токенов в постфиксной нотации.
     */
    private List<String> toRpn(List<String> tokens) {
        List<String> outputQueue = new ArrayList<>();
        Stack<String> operatorStack = new Stack<>();

        for (String token : tokens) {
            if (token.matches("-?\\d+(\\.\\d+)?")
                    || token.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                outputQueue.add(token);
            } else if (token.equals("(")) {
                operatorStack.push(token);
            } else if (token.equals(")")) {
                while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
                    outputQueue.add(operatorStack.pop());
                }

                if (operatorStack.isEmpty()) {
                    throw new IllegalArgumentException("Ошибка в выражении: несогласованные скобки.");
                }
                operatorStack.pop(); // Выкидываем открывающую скобку
            } else { // Оператор
                while (!operatorStack.isEmpty() &&
                        getPrecedence(operatorStack.peek()) >= getPrecedence(token)) {
                    outputQueue.add(operatorStack.pop());
                }
                operatorStack.push(token);
            }
        }

        while (!operatorStack.isEmpty()) {
            if (operatorStack.peek().equals("(")) {
                throw new IllegalArgumentException("Ошибка в выражении: несогласованные скобки.");
            }
            outputQueue.add(operatorStack.pop());
        }
        return outputQueue;
    }

    /**
     * Метод построения дерева выражений из постфиксной нотации (RPN).
     *
     * @param rpn Список токенов в постфиксной нотации.
     * @return Корневой узел дерева выражений.
     */
    private Expression buildTree(List<String> rpn) {
        Stack<Expression> expressionStack = new Stack<>();
        for (String token : rpn) {
            if (token.matches("-?\\d+(\\.\\d+)?")) {
                expressionStack.push(new Number(Double.parseDouble(token)));
            } else if (token.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                expressionStack.push(new Variable(token));
            } else { // Оператор
                if (expressionStack.size() < 2) {
                    throw new IllegalArgumentException("Ошибка в выражении: " +
                            "недостаточно операндов для оператора '" + token + "'.");
                }
                Expression right = expressionStack.pop();
                Expression left = expressionStack.pop();
                switch (token) {
                    case "+":
                        expressionStack.push(new Add(left, right));
                        break;
                    case "-":
                        expressionStack.push(new Sub(left, right));
                        break;
                    case "*":
                        expressionStack.push(new Mul(left, right));
                        break;
                    case "/":
                        expressionStack.push(new Div(left, right));
                        break;
                }
            }
        }
        return expressionStack.pop();
    }

    /**
     * Главный публичный метод для парсинга выражения.
     *
     * @param expressionString Входная строка, например "3 + 4 * x".
     * @return Корневой узел дерева выражений.
     */
    public Expression parse(String expressionString) {
        List<String> tokens = tokenize(expressionString);
        List<String> rpn = toRpn(tokens);
        return buildTree(rpn);
    }
}