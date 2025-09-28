package ru.nsu.ryzhneva.operation;

import ru.nsu.ryzhneva.Expression;
import ru.nsu.ryzhneva.values.Number;
import ru.nsu.ryzhneva.values.Variable;

public class Sub extends Operation {

    public Sub(Expression left, Expression right) {
        super(left, right, "-");
    }

    @Override
    public double applyOperation(double left, double right) {
        return left - right;
    }

    @Override
    public Expression derivative(String varName) {
        return new Sub(left.derivative(varName), right.derivative(varName));
    }

    @Override
    public Expression funcSimple() {
        Expression simpleLeft = left.funcSimple();
        Expression simpleRight = right.funcSimple();

        // Если выражения идентичны, возвращаем 0
        if (areExpressionsEqual(simpleLeft, simpleRight)) {
            return new Number(0);
        }

        // Если правая часть выражения равна 0, возвращаем левую часть
        if (simpleRight instanceof Number && ((Number) simpleRight).getValue() == 0) {
            return simpleLeft; // x - 0
        }

        // Если обе части выражения - числа, вычитаем их
        if (simpleLeft instanceof Number && simpleRight instanceof Number) {
            double result = ((Number) simpleLeft).getValue() - ((Number) simpleRight).getValue();
            return new Number(result);
        }

        // Если не удалось упростить выражение, возвращаем его как есть
        return new Sub(simpleLeft, simpleRight);
    }

    /**
     * Проверяет равенство двух выражений.
     *
     * @param expr1 первое выражение
     * @param expr2 второе выражение
     * @return true если выражения равны
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
