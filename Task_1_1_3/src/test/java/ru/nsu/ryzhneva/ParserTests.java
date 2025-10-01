package ru.nsu.ryzhneva;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.List;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.operation.types.Add;
import ru.nsu.ryzhneva.values.Variable;

/**
 * Тесты Parser.
 */
public class ParserTests {
    private final ExpressionParser parser = new ExpressionParser();

    @Test
    void testSimpleAddition() {
        Expression exp = parser.parse("3+5");
        assertEquals(8.0, exp.eval(Collections.emptyMap()));
        assertEquals("( 3 + 5 )", exp.print());
    }

    @Test
    void testOperatorPrecedence() {
        Expression exp = parser.parse("3+2*5");
        assertEquals(13.0, exp.eval(Collections.emptyMap()));
        assertEquals("( 3 + ( 2 * 5 ) )", exp.print());
    }

    @Test
    void testParenthesesOverridePrecedence() {
        Expression exp = parser.parse("(3+2)*5");
        assertEquals(25.0, exp.eval(Collections.emptyMap()));
        assertEquals("( ( 3 + 2 ) * 5 )", exp.print());
    }

    @Test
    void testComplexExpressionWithVariable() {
        Expression exp = parser.parse("(x*2)/4");
        java.util.Map<String, Double> vars = new java.util.HashMap<>();
        vars.put("x", 10.0);
        assertEquals(5.0, exp.eval(vars));
        assertEquals("( ( x * 2 ) / 4 )", exp.print());
    }

    @Test
    void testThrowsExceptionOnMismatchedParentheses() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse("( 3 + 5 ) )"));
    }

    @Test
    void testThrowsExceptionOnMissingOperand() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse("5*"));
    }

    @SuppressWarnings("unchecked")
    private List<String> invokeProcessUnaryMinus(List<String> tokens) throws Exception {
        Method method = ExpressionParser.class.getDeclaredMethod("processUnaryMinus", List.class);
        method.setAccessible(true);
        return (List<String>) method.invoke(parser, tokens);
    }

    @Test
    void testHandlesNegativeNumberAtStart() throws Exception {
        List<String> initialTokens = List.of("-", "5", "+", "10");
        List<String> processedTokens = invokeProcessUnaryMinus(initialTokens);
        assertEquals(List.of("-5", "+", "10"), processedTokens);
    }

    @Test
    void testHandlesMultipleNegativeNumbers() throws Exception {
        List<String> initialTokens = List.of("-", "x", "+", "(", "-", "y", ")");
        List<String> processedTokens = invokeProcessUnaryMinus(initialTokens);
        assertEquals(List.of("-x", "+", "(", "-y", ")"), processedTokens);
    }

    @Test
    void testParseThrowsOnUnmatchedParentheses() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse("(x+y"));
        assertThrows(IllegalArgumentException.class, () -> parser.parse("x+y)"));
    }

    @Test
    void testParseThrowsOnUnknownSymbols() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse("(x # y)"));
    }

    @Test
    void testEvalThrowsOnNonNumericValue() {
        Expression e = new Variable("x");
        assertThrows(IllegalArgumentException.class, () -> e.eval("x=abc"));
    }

    @Test
    void testParseThrowsOnInvalidSyntax() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse("()"));
        assertThrows(IllegalArgumentException.class, () -> parser.parse("(+)"));
        assertThrows(IllegalArgumentException.class, () -> parser.parse("(x++)"));
    }

    @Test
    void testEvalThrowsOnEmptyValue() {
        Expression e = new Variable("y");
        assertThrows(IllegalArgumentException.class, () -> e.eval("x=; y=5"));
    }

    @Test
    void testEvalThrowsOnMissingDelimiter() {
        Expression e = new Add(new Variable("x"), new Variable("y"));
        assertThrows(IllegalArgumentException.class, () -> e.eval("x=5 y=3"));
    }
}
