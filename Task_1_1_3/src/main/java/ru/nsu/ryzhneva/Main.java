package ru.nsu.ryzhneva;

import java.util.Scanner;

/**
 * Точка входа для программы.
 */
public class Main {
    /**
     * Запускает проект.
     *
     * @param args Аргументы.
     */
    public static void main(String[] args) {
        Scanner sn = new Scanner(System.in);

        System.out.print("Enter the expression: ");
        String input = sn.nextLine();
        sn.close();
        ExpressionParser parser = new ExpressionParser();
        Expression parsedExpression = parser.parse(input);

        System.out.println("Original Expression: "
                + parsedExpression.print());

        Expression simplifiedExpression = parsedExpression.funcSimple();

        System.out.println("Simplified Expression: "
                + simplifiedExpression.print());
    }
}