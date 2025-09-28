package ru.nsu.ryzhneva;

import java.util.Collections;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.operation.Div;
import ru.nsu.ryzhneva.values.Number;
import ru.nsu.ryzhneva.values.Variable;

/**
 * Тесты Div.
 */
public class DivTests {

    private final Variable variableX = new Variable("x");
    private final Expression ten = new Number(10);
    private final Expression one = new Number(1);
    private final Expression zero = new Number(0);

    @Test
    void testEvaluation() {
        Expression exp = new Div(variableX, ten); // x / 10
        assertEquals(2.0, exp.eval(Map.of("x", 20.0)));
    }

    @Test
    void testDivisionByZeroThrowsException() {
        Expression exp = new Div(ten, zero); // 10 / 0
        assertThrows(ArithmeticException.class, () -> {
            exp.eval(Collections.emptyMap());
        });
    }

    @Test
    void testDifferentiationQuotientRule() {
        // (x / 10)' = (x'*10 - x*10') / 10^2 = (1*10 - x*0) / 100 = 10 / 100 = 0.1
        Expression exp = new Div(variableX, ten);
        Expression derivative = exp.derivative("x");
        assertEquals("(((1*10)-(x*0))/(10*10))", derivative.print());
        assertEquals("0.1", derivative.funcSimple().print());
    }

    @Test
    void testSimplificationWithOne() {
        // x / 1 simplifies to x
        Expression exp = new Div(variableX, one);
        assertEquals("x", exp.funcSimple().print());
    }

    @Test
    void testSimplificationOfDividingIdenticalExpressions() {
        // x / x simplifies to 1
        Expression exp = new Div(variableX, variableX);
        assertEquals("1", exp.funcSimple().print());
    }

    @Test
    void testSimplificationOfZeroNumerator() {
        // 0 / x simplifies to 0
        Expression exp = new Div(zero, variableX);
        assertEquals("0", exp.funcSimple().print());
    }

    @Test
    void testToString() {
        Expression exp = new Div(variableX, ten);
        assertEquals("(x/10)", exp.print());
    }
}