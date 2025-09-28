package ru.nsu.ryzhneva;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

/**
 * Тесты Parser.
 */
public class ParserTests {
    private final ExpressionParser parser = new ExpressionParser();

    @Test
    void testSimpleAddition() {
        Expression exp = parser.parse("3+5");
        assertEquals(8.0, exp.eval(null));
        assertEquals("(3+5)", exp.print());
    }

    @Test
    void testOperatorPrecedence() {
        // Should parse as 3 + (2 * 5)
        Expression exp = parser.parse("3+2*5");
        assertEquals(13.0, exp.eval(null));
        assertEquals("(3+(2*5))", exp.print());
    }

    @Test
    void testParenthesesOverridePrecedence() {
        // Should parse as (3 + 2) * 5
        Expression exp = parser.parse("(3+2)*5");
        assertEquals(25.0, exp.eval(null));
        assertEquals("((3+2)*5)", exp.print());
    }

    @Test
    void testComplexExpressionWithVariable() {
        Expression exp = parser.parse("(x*2)/4");
        java.util.Map<String, Double> vars = new java.util.HashMap<>();
        vars.put("x", 10.0);
        assertEquals(5.0, exp.eval(vars));
        assertEquals("((x*2)/4)", exp.print());
    }

    @Test
    void testThrowsExceptionOnMismatchedParentheses() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse("(3+5))"));
    }

    @Test
    void testThrowsExceptionOnMissingOperand() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse("5*"));
    }
}
