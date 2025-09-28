package ru.nsu.ryzhneva;


import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import ru.nsu.ryzhneva.operation.Mul;
import ru.nsu.ryzhneva.values.Number;
import ru.nsu.ryzhneva.values.Variable;

public class MulTests {

    private final Variable variableX = new Variable("x");
    private final Expression five = new Number(5);
    private final Expression one = new Number(1);
    private final Expression zero = new Number(0);

    @Test
    void testEvaluation() {
        Expression exp = new Mul(variableX, five); // x * 5
        assertEquals(50.0, exp.eval(Map.of("x", 10.0)));
    }

    @Test
    void testDifferentiationProductRule() {
        // (x * 5)' = (x' * 5) + (x * 5') = (1 * 5) + (x * 0) = 5
        Expression exp = new Mul(variableX, five);
        Expression derivative = exp.derivative("x");
        assertEquals("((1*5)+(x*0))", derivative.print());
        assertEquals("5", derivative.funcSimple().print());
    }

    @Test
    void testSimplificationWithOne() {
        // x * 1 simplifies to x
        Expression exp1 = new Mul(variableX, one);
        assertEquals("x", exp1.funcSimple().print());

        // 1 * x simplifies to x
        Expression exp2 = new Mul(one, variableX);
        assertEquals("x", exp2.funcSimple().print());
    }

    @Test
    void testSimplificationWithZero() {
        // x * 0 simplifies to 0
        Expression exp1 = new Mul(variableX, zero);
        assertEquals("0", exp1.funcSimple().print());

        // 0 * x simplifies to 0
        Expression exp2 = new Mul(zero, variableX);
        assertEquals("0", exp2.funcSimple().print());
    }

    @Test
    void testToString() {
        Expression exp = new Mul(variableX, five);
        assertEquals("(x*5)", exp.print());
    }
}