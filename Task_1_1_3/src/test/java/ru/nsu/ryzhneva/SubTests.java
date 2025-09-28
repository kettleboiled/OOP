package ru.nsu.ryzhneva;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import ru.nsu.ryzhneva.operation.Mul;
import ru.nsu.ryzhneva.operation.Sub;
import ru.nsu.ryzhneva.values.Number;
import ru.nsu.ryzhneva.values.Variable;

import java.util.Map;

public class SubTests {

    private final Variable x = new Variable("x");
    private final Expression five = new Number(5);
    private final Expression zero = new Number(0);

    @Test
    void testEvaluation() {
        Expression exp = new Sub(x, five); // x - 5
        assertEquals(5.0, exp.eval(Map.of("x", 10.0)));
    }

    @Test
    void testDifferentiation() {
        // (x - 5)' = 1 - 0 = 1
        Expression exp = new Sub(x, five);
        assertEquals(1.0, exp.derivative("x").eval(null));
    }

    @Test
    void testSimplificationOfSubtractingZero() {
        // x - 0 simplifies to x
        Expression exp = new Sub(x, zero);
        assertEquals("x", exp.funcSimple().print());
    }

    @Test
    void testSimplificationOfSubtractingIdenticalExpressions() {
        // x - x simplifies to 0
        Expression exp = new Sub(x, x);
        assertEquals("0", exp.funcSimple().print());

        // (5*x) - (5*x) simplifies to 0
        Expression term1 = new Mul(new Number(5), new Variable("x"));
        Expression term2 = new Mul(new Number(5), new Variable("x"));

        Expression complexExp = new Sub(term1, term2);
        Expression newExp = complexExp.funcSimple();
        assertEquals("0", newExp.print());
    }

    @Test
    void testConstantFolding() {
        Expression exp = new Sub(new Number(10), new Number(3));
        Expression simplified = exp.funcSimple();
        assertEquals(Number.class, simplified.getClass());
        assertEquals(7.0, simplified.eval(null));
    }

    @Test
    void testToString() {
        Expression exp = new Sub(x, five);
        assertEquals("(x-5)", exp.print());
    }
}