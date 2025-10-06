package ru.nsu.ryzhneva;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.operation.types.Mul;
import ru.nsu.ryzhneva.values.Number;
import ru.nsu.ryzhneva.values.Variable;

/**
 * Тесты Mul.
 */
public class MulTests {

    private final Variable variableX = new Variable("x");
    private final Expression five = new Number(5);
    private final Expression one = new Number(1);
    private final Expression zero = new Number(0);

    @Test
    void testEvaluation() {
        Expression exp = new Mul(variableX, five);
        assertEquals(50.0, exp.eval(Map.of("x", 10.0)));
    }

    @Test
    void testDifferentiationProductRule() {
        Expression exp = new Mul(variableX, five);
        Expression derivative = exp.derivative("x");
        assertEquals("( ( 1 * 5 ) + ( x * 0 ) )", derivative.print());
        assertEquals("5", derivative.funcSimple().print());
    }

    @Test
    void testSimplificationWithOne() {
        Expression exp1 = new Mul(variableX, one);
        assertEquals("x", exp1.funcSimple().print());
        Expression exp2 = new Mul(one, variableX);
        assertEquals("x", exp2.funcSimple().print());
    }

    @Test
    void testSimplificationWithZero() {
        Expression exp1 = new Mul(variableX, zero);
        assertEquals("0", exp1.funcSimple().print());
        Expression exp2 = new Mul(zero, variableX);
        assertEquals("0", exp2.funcSimple().print());
    }

    @Test
    void testToString() {
        Expression exp = new Mul(variableX, five);
        assertEquals("( x * 5 )", exp.print());
    }
}