package ru.nsu.ryzhneva.operation;

import java.util.Map;
import ru.nsu.ryzhneva.Expression;

/**
 * Абстрактный класс, представляющий математическую операцию с двумя операндами.
 * Этот класс наследуется от {@link Expression} и реализует базовые методы для работы с операциями,
 * такими как печать выражения, вычисление и выполнение операции над двумя выражениями.
 */
public abstract class Operation extends Expression {

    protected Expression left;
    protected Expression right;
    protected Operator operator;

    /**
     * Конструктор для создания операции с двумя операндами.
     *
     * @param left     Левый операнд.
     * @param right    Правый операнд.
     * @param operator Оператор операции.
     */
    public Operation(Expression left, Expression right, Operator operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    /**
     * Возвращает левый операнд операции.
     *
     * @return Левый операнд.
     */
    public Expression getLeft() {
        return left;
    }

    /**
     * Возвращает правый операнд операции.
     *
     * @return Правый операнд.
     */
    public Expression getRight() {
        return right;
    }

    /**
     * Метод для вывода строки, представляющей операцию.
     * Форматирует выражение в виде "(левое выражение оператор правое выражение)".
     *
     * @return Строковое представление операции.
     */
    @Override
    public String print() {
        return "(" + " " + left.print() + " " + operator + " " + right.print() + " " + ")";
    }

    /**
     * Абстрактный метод для выполнения операции над двумя числовыми значениями.
     * Этот метод должен быть реализован в подклассах для конкретных операций
     * (например, сложение или вычитание).
     *
     * @param left  Число, представляющее левый операнд.
     * @param right Число, представляющее правый операнд.
     * @return Результат выполнения операции.
     */
    protected abstract double applyOperation(double left, double right);

    /**
     * Вычисляет значение выражения с подставленными значениями переменных.
     * Рекурсивно вычисляет значения левого и правого операндов и применяет операцию.
     *
     * @param variables Карта с переменными и их значениями.
     * @return Результат вычисления выражения.
     */
    @Override
    public double eval(Map<String, Double> variables) {
        double resLeft = left.eval(variables);
        double resRight = right.eval(variables);
        return applyOperation(resLeft, resRight);
    }
}