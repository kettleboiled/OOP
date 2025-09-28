package ru.nsu.ryzhneva;

import java.util.Map;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import ru.nsu.ryzhneva.values.Variable;


public class VariableTests {
    @Test
    void testVariableEvaluation() {
        Expression x = new Variable("x");
        assertEquals(10.0, x.eval(Map.of("x", 10.0)));
    }

    @Test
    void testThrowsExceptionWhenVariableIsMissing() {
        Expression x = new Variable("x");
        assertThrows(IllegalArgumentException.class, () -> x.eval(Map.of("y", 10.0)));
    }

    @Test
    void testVariableDifferentiation() {
        Expression x = new Variable("x");
        Expression y = new Variable("y");

        // Derivative of x with respect to x is 1
        assertEquals(1.0, x.derivative("x").eval(null));

        // Derivative of y with respect to x is 0
        assertEquals(0.0, y.derivative("x").eval(null));
    }

    @Test
    void testVariableToString() {
        Expression x = new Variable("longVariableName");
        assertEquals("longVariableName", x.print());
    }

}
