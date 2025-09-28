package ru.nsu.ryzhneva;

import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.operation.Add;
import ru.nsu.ryzhneva.values.Number;
import ru.nsu.ryzhneva.values.Variable;

/**
 * Тесты Add.
 */
public class AddTests {
    private final Variable variableX = new Variable("x");
    private final Expression five = new Number(5);
    private final Expression zero = new Number(0);

    @Test
    void testEvaluation() {
        Expression exp = new Add(variableX, five); // x + 5
        assertEquals(15.0, exp.eval(Map.of("x", 10.0)));
    }

    @Test
    void testDifferentiation() {
        // (x + 5)' = 1 + 0 = 1
        Expression exp = new Add(variableX, five);
        assertEquals(1.0, exp.derivative("x").eval(null));
    }

    @Test
    void testSimplificationWithZero() {
        // x + 0 simplifies to x
        Expression exp1 = new Add(variableX, zero);
        assertEquals("x", exp1.funcSimple().print());

        // 0 + x simplifies to x
        Expression exp2 = new Add(zero, variableX);
        assertEquals("x", exp2.funcSimple().print());
    }

    @Test
    void testConstantFolding() {
        Expression exp = new Add(new Number(3), new Number(8));
        Expression simplified = exp.funcSimple();
        assertEquals(Number.class, simplified.getClass());
        assertEquals(11.0, simplified.eval(null));
    }

    @Test
    void testToString() {
        Expression exp = new Add(variableX, five);
        assertEquals("(x+5)", exp.print());
    }
}
