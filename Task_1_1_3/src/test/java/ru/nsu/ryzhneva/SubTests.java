package ru.nsu.ryzhneva;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.operation.types.Mul;
import ru.nsu.ryzhneva.operation.types.Sub;
import ru.nsu.ryzhneva.values.Number;
import ru.nsu.ryzhneva.values.Variable;

/**
 * Тесты Sub.
 */
public class SubTests {

    private final Variable variableX = new Variable("x");
    private final Expression five = new Number(5);
    private final Expression zero = new Number(0);

    @Test
    void testEvaluation() {
        Expression exp = new Sub(variableX, five); // x - 5
        assertEquals(5.0, exp.eval(Map.of("x", 10.0)));
    }

    @Test
    void testDifferentiation() {
        Expression exp = new Sub(variableX, five);
        assertEquals(1.0, exp.derivative("x").eval(Collections.emptyMap()));
    }

    @Test
    void testSimplificationOfSubtractingZero() {
        Expression exp = new Sub(variableX, zero);
        assertEquals("x", exp.funcSimple().print());
    }

    @Test
    void testSimplificationOfSubtractingIdenticalExpressions() {
        Expression exp = new Sub(variableX, variableX);
        assertEquals("0", exp.funcSimple().print());
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
        assertEquals(7.0, simplified.eval(Collections.emptyMap()));
    }

    @Test
    void testToString() {
        Expression exp = new Sub(variableX, five);
        assertEquals("( x - 5 )", exp.print());
    }
}