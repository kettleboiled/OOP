package ru.nsu.ryzhneva;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import ru.nsu.ryzhneva.values.Number;

public class NumberTests {
    @Test
    void testNumberEvaluation() {
        Expression num = new ru.nsu.ryzhneva.values.Number(42.5);
        assertEquals(42.5, num.eval(null));
    }

    @Test
    void testNumberDifferentiation() {
        Expression num = new ru.nsu.ryzhneva.values.Number(100);
        // The derivative of any constant is 0
        Expression derivative = num.derivative("x");
        assertEquals(ru.nsu.ryzhneva.values.Number.class, derivative.getClass());
        assertEquals(0.0, derivative.eval(null));
    }

    @Test
    void testNumberSimplification() {
        Expression num = new ru.nsu.ryzhneva.values.Number(15.0);
        // A number is already simple, so it should return itself
        assertEquals(num, num.funcSimple());
    }

    @Test
    void testNumberToStringFormatting() {
        assertEquals("5", new ru.nsu.ryzhneva.values.Number(5.0).print());
        assertEquals("-10", new ru.nsu.ryzhneva.values.Number(-10).print());
        assertEquals("5.5", new Number(5.5).print());
    }
}
