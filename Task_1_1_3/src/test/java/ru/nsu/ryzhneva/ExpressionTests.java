package ru.nsu.ryzhneva;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.operation.types.Add;
import ru.nsu.ryzhneva.operation.types.Div;
import ru.nsu.ryzhneva.operation.types.Mul;
import ru.nsu.ryzhneva.values.Number;
import ru.nsu.ryzhneva.values.Variable;

/**
 * Тесты Expression.
 */
public class ExpressionTests {

    @Test
    void testEvaluationWithMap() {
        Expression expression = new Add(new Variable("x"), new Variable("y"));
        Map<String, Double> assignments = Map.of(
                "x", 10.5,
                "y", -2.0
        );
        double result = expression.eval(assignments);
        assertEquals(8.5, result);
    }

    @Test
    void testThrowsExceptionWhenVariableIsNotDefined() {
        Expression expression = new Variable("x");
        assertThrows(IllegalArgumentException.class, () -> {
            expression.eval(Collections.emptyMap());
        });
    }

    @Test
    void testEvaluationIgnoresExtraVariablesInMap() {
        Expression expression = new Add(new Variable("x"), new Variable("z"));
        Map<String, Double> assignments = Map.of(
                "x", 5.0,
                "y", 99.0,
                "z", 10.0
        );
        double result = expression.eval(assignments);
        assertEquals(15.0, result);
    }

    @Test
    void testChainRuleDifferentiation() {
        Variable x = new Variable("x");
        Expression inner = new Add(
                new Mul(x, x),
                new Mul(new Number(3), x)
        );
        Expression f = new Mul(inner, new Mul(inner, inner)); // f = inner^3
        Expression derivative = f.derivative("x");
        double result = derivative.eval(Map.of("x", 2.0));
        assertEquals(2100.0, result, 0.001);
    }

    @Test
    void testEvalStringFeatures() {
        Expression e = new Add(
                new Mul(new Variable("x"), new Variable("y")),
                new Div(new Variable("z"), new Number(2))
        );
        assertEquals(17.0, e.eval("x=3; y=4; z=10"));
        assertEquals(4.0, e.eval("x=0; y=100; z=8"));
        assertEquals(17.0, e.eval("z=10; x=3; y=4"));
        assertEquals(17.0, e.eval("x=3; y=4; z=10; w=100"));
        assertEquals(17.0, e.eval(" x = 3 ;  y=4 ; z = 10;; "));
    }

    @Test
    void testEvalThrowsExceptionForMissingVariable() {
        Expression e = new Add(new Variable("x"), new Variable("y"));
        Map<String, Double> variablesWithMissingY = Map.of("x", 3.0);
        assertThrows(IllegalArgumentException.class, () -> {
            e.eval(variablesWithMissingY);
        });
    }

    @Test
    void testEvalWithEmptyAssignmentsOnConstantExpression() {
        // Пустое означивание для выражений без переменных
        assertEquals(5.0, new Number(5).eval(""));
        assertEquals(5.0, new Add(new Number(2), new Number(3)).eval(""));
    }
}