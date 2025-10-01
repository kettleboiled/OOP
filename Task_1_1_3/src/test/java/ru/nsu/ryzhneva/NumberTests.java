package ru.nsu.ryzhneva;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.operation.types.Add;
import ru.nsu.ryzhneva.values.Number;

/**
 * Тесты Number.
 */
public class NumberTests {
    @Test
    void testNumberEvaluation() {
        Expression num = new ru.nsu.ryzhneva.values.Number(42.5);
        assertEquals(42.5, num.eval(Collections.emptyMap()));
    }

    @Test
    void testConstantDerivative() {
        Expression constant = new Number(5);
        Expression derivative = constant.derivative("x");
        assertTrue(derivative instanceof Number);
        assertEquals(0.0, derivative.eval(Collections.emptyMap()));
    }

    @Test
    void testNumberDifferentiation() {
        Expression num = new ru.nsu.ryzhneva.values.Number(100);
        Expression derivative = num.derivative("x");
        assertEquals(ru.nsu.ryzhneva.values.Number.class, derivative.getClass());
        assertEquals(0.0, derivative.eval(Collections.emptyMap()));
    }

    @Test
    void testNumberSimplification() {
        Expression num = new ru.nsu.ryzhneva.values.Number(15.0);
        assertEquals(num, num.funcSimple());
    }

    @Test
    void testNumberToStringFormatting() {
        assertEquals("5", new ru.nsu.ryzhneva.values.Number(5.0).print());
        assertEquals("-10", new ru.nsu.ryzhneva.values.Number(-10).print());
        assertEquals("5.5", new Number(5.5).print());
    }

    @Test
    void testExpressionWithoutVariableDerivative() {
        Expression e = new Add(new Number(3), new Number(5));
        Expression derivative = e.derivative("x");
        assertEquals("0", derivative.funcSimple().print());
    }
}
