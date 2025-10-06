package ru.nsu.ryzhneva;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.values.Variable;

/**
 * Тесты Variable.
 */
public class VariableTests {
    @Test
    void testVariableEvaluation() {
        Expression x = new Variable("x");
        assertEquals(10.0, x.eval(Map.of("x", 10.0)));
    }

    @Test
    void testThrowsExceptionWhenVariableIsMissing() {
        Expression x = new Variable("x");
        assertThrows(IllegalArgumentException.class,
                () -> x.eval(Map.of("y", 10.0)));
    }

    @Test
    void testVariableDifferentiation() {
        Expression x = new Variable("x");
        Expression y = new Variable("y");
        assertEquals(1.0, x.derivative("x").eval(Collections.emptyMap()));
        assertEquals(0.0, y.derivative("x").eval(Collections.emptyMap()));
    }

    @Test
    void testVariableToString() {
        Expression x = new Variable("longVariableName");
        assertEquals("longVariableName", x.print());
    }

    @Test
    void testVariableDerivative() {
        Expression xVar = new Variable("x");
        Expression derivative1 = xVar.derivative("x");
        assertEquals(1.0, derivative1.eval(Collections.emptyMap()));
        Expression derivative2 = xVar.derivative("y");
        assertEquals(0.0, derivative2.eval(Collections.emptyMap()));
    }

    @Test
    void testMultipleDifferentiation() {
        Expression e = new Variable("x");
        Expression de1 = e.derivative("x");
        assertEquals(1.0, de1.eval(Collections.emptyMap()));
        Expression de2 = de1.derivative("x");
        assertEquals(0.0, de2.eval(Collections.emptyMap()));
    }
}