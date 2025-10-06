package ru.nsu.ryzhneva.operation.types;

import ru.nsu.ryzhneva.Expression;
import ru.nsu.ryzhneva.operation.Operation;
import ru.nsu.ryzhneva.operation.Operator;
import ru.nsu.ryzhneva.values.Number;
import ru.nsu.ryzhneva.values.Variable;

/**
 * Класс, представляющий операцию вычитания двух выражений.
 * Реализует операцию вычитания для выражений в виде дерева.
 * Этот класс наследует от {@link Operation} и предоставляет реализацию
 * для методов дифференцирования, упрощения и вычисления значения выражений.
 */
public class Sub extends Operation {

    /**
     * Конструктор для создания операции вычитания.
     *
     * @param left  Левое выражение (minuend).
     * @param right Правое выражение (subtrahend).
     */
    public Sub(Expression left, Expression right) {
        super(left, right, Operator.SUB);
    }

    /**
     * Выполняет операцию вычитания для двух чисел.
     *
     * @param left  Число, из которого вычитается (minuend).
     * @param right Число, которое вычитается (subtrahend).
     * @return Результат вычитания (left - right).
     */
    @Override
    public double applyOperation(double left, double right) {
        return left - right;
    }

    /**
     * Выполняет дифференцирование выражения по правилу дифференцирования для вычитания.
     * Производная от выражения вида (f - g) вычисляется как (f' - g').
     *
     * @param varName Имя переменной, по которой производится дифференцирование.
     * @return Новое выражение, представляющее собой производную.
     */
    @Override
    public Expression derivative(String varName) {
        return new Sub(left.derivative(varName), right.derivative(varName));
    }

    /**
     * Упрощает выражение вычитания.
     * Применяет базовые правила упрощения, такие как:
     * - Если выражения идентичны, результат равен 0.
     * - Если правое выражение равно 0, возвращаем левое.
     * - Если оба выражения - числа, производим их вычитание.
     *
     * @return Упрощенное выражение.
     */
    @Override
    public Expression funcSimple() {
        Expression simpleLeft = left.funcSimple();
        Expression simpleRight = right.funcSimple();

        if (simpleLeft.equals(simpleRight)) {
            return new Number(0);
        }
        if (simpleRight instanceof Number && ((Number) simpleRight).getValue() == 0) {
            return simpleLeft; // x - 0
        }
        if (simpleLeft instanceof Number && simpleRight instanceof Number) {
            double result = ((Number) simpleLeft).getValue() - ((Number) simpleRight).getValue();
            return new Number(result);
        }

        return new Sub(simpleLeft, simpleRight);
    }
}