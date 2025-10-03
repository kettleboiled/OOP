package ru.nsu.ryzhneva;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;

import ru.nsu.ryzhneva.operation.Operator;
import ru.nsu.ryzhneva.operation.types.Add;
import ru.nsu.ryzhneva.operation.types.Div;
import ru.nsu.ryzhneva.operation.types.Mul;
import ru.nsu.ryzhneva.operation.types.Sub;
import ru.nsu.ryzhneva.values.Number;
import ru.nsu.ryzhneva.values.Variable;

/**
 * Класс для парсинга математических выражений.
 */
public class ExpressionParser {

    private static final String NUMBER_REGEX = "-?\\d+(\\.\\d+)?";
    private static final String VARIABLE_REGEX = "[a-zA-Z_][a-zA-Z0-9_]*";
    private static final String OPERATOR_REGEX = "()+-*/";

    /**
     * Главный публичный метод для парсинга выражения.
     *
     * @param expressionString Входная строка, например "3 + 4 * x".
     * @return Корневой узел дерева выражений.
     */
    public Expression parse(String expressionString) {
        List<String> tokens = tokenize(expressionString);
        List<String> processedTokens = processUnaryMinus(tokens);
        List<String> rpn = toRpn(processedTokens);
        return buildTree(rpn);
    }

    /**
     * Разбивает строку на отдельные токены.
     *
     * @param expression Строковое математическое выражение.
     * @return Список токенов.
     */
    private List<String> tokenize(String expression) {
        List<String> tokens = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(expression, OPERATOR_REGEX, true);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken().trim();
            if (!token.isEmpty()) {
                tokens.add(token);
            }
        }
        return tokens;
    }

    /**
     * Преобразование выражения в постфиксную нотацию (RPN).
     *
     * @param tokens Список токенов.
     * @return Список токенов в постфиксной нотации.
     */
    private List<String> toRpn(List<String> tokens) {
        List<String> outputQueue = new ArrayList<>();
        Stack<String> operatorStack = new Stack<>();

        for (String token : tokens) {
            if (token.matches(NUMBER_REGEX) || token.matches(VARIABLE_REGEX)) {
                outputQueue.add(token);
            } else if (OPERATOR_REGEX.contains(token)) { // Проверяем, является ли токен оператором или скобкой
                switch (token) {
                    case "(":
                        operatorStack.push(token);
                        break;
                    case ")":
                        while (!operatorStack.isEmpty() && !"(".equals(operatorStack.peek())) {
                            outputQueue.add(operatorStack.pop());
                        }
                        if (operatorStack.isEmpty()) {
                            throw new IllegalArgumentException("Ошибка: несогласованные скобки.");
                        }
                        operatorStack.pop();
                        break;
                    case "+":
                    case "-":
                    case "*":
                    case "/":
                        while (!operatorStack.isEmpty()
                                && !"(".equals(operatorStack.peek())
                                && Operator.fromString(operatorStack.peek()).getPrecedence()
                                >= Operator.fromString(token).getPrecedence()) {
                            outputQueue.add(operatorStack.pop());
                        }
                        operatorStack.push(token);
                        break;
                }
            } else {
                throw new IllegalArgumentException("Неизвестный символ в выражении: " + token);
            }
        }
        while (!operatorStack.isEmpty()) {
            String op = operatorStack.pop();
            if ("(".equals(op)) {
                throw new IllegalArgumentException("Ошибка: несогласованные скобки.");
            }
            outputQueue.add(op);
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
            if (token.matches(NUMBER_REGEX)) {
                expressionStack.push(new Number(Double.parseDouble(token)));
            } else if (token.matches(VARIABLE_REGEX)) {
                expressionStack.push(new Variable(token));
            } else {
                if (expressionStack.size() < 2) {
                    throw new IllegalArgumentException("Expression error: "
                            + "there are not enough operands for the operator '" + token + "'.");
                }

                Expression right = expressionStack.pop();
                Expression left = expressionStack.pop();

                Operator op = Operator.fromString(token);
                switch (op) {
                    case ADD:
                        expressionStack.push(new Add(left, right));
                        break;
                    case SUB:
                        expressionStack.push(new Sub(left, right));
                        break;
                    case MUL:
                        expressionStack.push(new Mul(left, right));
                        break;
                    case DIV:
                        expressionStack.push(new Div(left, right));
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown operator: " + token);
                }
            }
        }
        if (expressionStack.size() != 1) {
            throw new IllegalArgumentException("Syntactic error.");
        }
        return expressionStack.pop();
    }

    /**
     * Обрабатывает унарные минусы, объединяя их со следующим числом.
     *
     * @param tokens Входной список токенов.
     * @return Обработанный список токенов.
     */
    private List<String> processUnaryMinus(List<String> tokens) {
        List<String> processed = new ArrayList<>();
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            boolean isUnary = (i == 0 || "(".equals(tokens.get(i - 1))
                    || "+-*/".contains(tokens.get(i - 1)));

            if (token.equals("-") && isUnary) {
                processed.add("-" + tokens.get(i + 1));
                i++;
            } else {
                processed.add(token);
            }
        }
        return processed;
    }
}