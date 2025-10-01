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
     *  - Если выражения идентичны, результат равен 0.
     *  - Если правое выражение равно 0, возвращаем левое.
     *  - Если оба выражения - числа, производим их вычитание.
     *
     * @return Упрощенное выражение.
     */
    @Override
    public Expression funcSimple() {
        Expression simpleLeft = left.funcSimple();
        Expression simpleRight = right.funcSimple();

        if (areExpressionsEqual(simpleLeft, simpleRight)) {
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

    /**
     * Проверяет равенство двух выражений.
     * Равенство проверяется рекурсивно для всех типов выражений: числа, переменные, операции.
     *
     * @param expr1 Первое выражение.
     * @param expr2 Второе выражение.
     * @return true, если выражения равны, иначе false.
     */
    private boolean areExpressionsEqual(Expression expr1, Expression expr2) {
        if (expr1.getClass() != expr2.getClass()) {
            return false;
        }

        if (expr1 instanceof Number) {
            return ((Number) expr1).getValue() == ((Number) expr2).getValue();
        }

        if (expr1 instanceof Variable) {
            return ((Variable) expr1).getVar().equals(((Variable) expr2).getVar());
        }

        if (expr1 instanceof Add) {
            Add add1 = (Add) expr1;
            Add add2 = (Add) expr2;
            return areExpressionsEqual(add1.getLeft(), add2.getLeft())
                    && areExpressionsEqual(add1.getRight(), add2.getRight());
        }

        if (expr1 instanceof Sub) {
            Sub sub1 = (Sub) expr1;
            Sub sub2 = (Sub) expr2;
            return areExpressionsEqual(sub1.getLeft(), sub2.getLeft())
                    && areExpressionsEqual(sub1.getRight(), sub2.getRight());
        }

        if (expr1 instanceof Mul) {
            Mul mul1 = (Mul) expr1;
            Mul mul2 = (Mul) expr2;
            return areExpressionsEqual(mul1.getLeft(), mul2.getLeft())
                    && areExpressionsEqual(mul1.getRight(), mul2.getRight());
        }

        if (expr1 instanceof Div) {
            Div div1 = (Div) expr1;
            Div div2 = (Div) expr2;
            return areExpressionsEqual(div1.getLeft(), div2.getLeft())
                    && areExpressionsEqual(div1.getRight(), div2.getRight());
        }

        return false;
    }
}
