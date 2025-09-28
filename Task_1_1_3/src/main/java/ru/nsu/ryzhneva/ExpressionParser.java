package ru.nsu.ryzhneva;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;

import ru.nsu.ryzhneva.values.*;
import ru.nsu.ryzhneva.values.Number;
import ru.nsu.ryzhneva.operation.*;

public class ExpressionParser {

    // Метод для определения приоритета операторов
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

    // 1. Метод токенизации строки
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

    // 2. Метод преобразования в RPN (Shunting-yard)
    private List<String> toRPN(List<String> tokens) {
        List<String> outputQueue = new ArrayList<>();
        Stack<String> operatorStack = new Stack<>();

        for (String token : tokens) {
            if (token.matches("-?\\d+(\\.\\d+)?") || token.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                outputQueue.add(token);
            } else if (token.equals("(")) {
                operatorStack.push(token);
            } else if (token.equals(")")) {
                // ИСПРАВЛЕНО: Добавлена проверка на пустой стек
                while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
                    outputQueue.add(operatorStack.pop());
                }

                // ИСПРАВЛЕНО: Проверка на несогласованные скобки
                if (operatorStack.isEmpty()) {
                    throw new IllegalArgumentException("Ошибка в выражении: несогласованные скобки.");
                }
                operatorStack.pop(); // Выкидываем открывающую скобку
            } else { // Оператор
                while (!operatorStack.isEmpty() && getPrecedence(operatorStack.peek()) >= getPrecedence(token)) {
                    outputQueue.add(operatorStack.pop());
                }
                operatorStack.push(token);
            }
        }

        while (!operatorStack.isEmpty()) {
            // ИСПРАВЛЕНО: Если на стеке осталась скобка - ошибка
            if (operatorStack.peek().equals("(")) {
                throw new IllegalArgumentException("Ошибка в выражении: несогласованные скобки.");
            }
            outputQueue.add(operatorStack.pop());
        }
        return outputQueue;
    }

    // 3. Метод построения дерева из RPN
    private Expression buildTree(List<String> rpn) {
        Stack<Expression> expressionStack = new Stack<>();
        for (String token : rpn) {
            if (token.matches("-?\\d+(\\.\\d+)?")) {
                expressionStack.push(new Number(Double.parseDouble(token)));
            } else if (token.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                expressionStack.push(new Variable(token));
            } else { // Оператор
                // ИСПРАВЛЕНО: Проверяем, достаточно ли операндов в стеке
                if (expressionStack.size() < 2) {
                    throw new IllegalArgumentException("Ошибка в выражении: недостаточно операндов для оператора '" + token + "'.");
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
     * @param expressionString Входная строка, например "3 + 4 * x"
     * @return Корневой узел дерева выражений.
     */
    public Expression parse(String expressionString) {
        List<String> tokens = tokenize(expressionString);
        List<String> rpn = toRPN(tokens);
        return buildTree(rpn);
    }
}