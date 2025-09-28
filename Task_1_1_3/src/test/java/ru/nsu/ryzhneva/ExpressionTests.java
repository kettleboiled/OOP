package ru.nsu.ryzhneva;

import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import ru.nsu.ryzhneva.operation.Add;
import ru.nsu.ryzhneva.values.Variable;

public class ExpressionTests {

    @Test
    void testEvaluationWithMap() {
        // Arrange: Создаём конкретное выражение, которое зависит от переменных x и y.
        Expression expression = new Add(new Variable("x"), new Variable("y"));

        // ИСПРАВЛЕНО: Вот правильный синтаксис для создания Map в Java
        Map<String, Double> assignments = Map.of(
                "x", 10.5,
                "y", -2.0
        );

        // Act: Вызываем метод evaluate(Map), который принимает готовую карту.
        double result = expression.eval(assignments);

        // Assert: Проверяем результат вычисления.
        assertEquals(8.5, result);
    }

    @Test
    void testThrowsExceptionWhenVariableIsNotDefined() {
        // Arrange: Выражение, которому нужна переменная 'x'.
        Expression expression = new Variable("x");

        // Act & Assert: Проверяем, что программа выдаст ошибку,
        // если мы попытаемся вычислить выражение с пустой картой переменных.
        assertThrows(IllegalArgumentException.class, () -> {
            // ИСПРАВЛЕНО: Передаём пустую карту вместо вызова без аргументов.
            expression.eval(Collections.emptyMap());
        });
    }

    @Test
    void testEvaluationIgnoresExtraVariablesInMap() {
        // Arrange: Выражение (x + z)
        Expression expression = new Add(new Variable("x"), new Variable("z"));

        // ИСПРАВЛЕНО: Создаём Map. Тест теперь проверяет, что лишняя переменная 'y' игнорируется.
        Map<String, Double> assignments = Map.of(
                "x", 5.0,
                "y", 99.0, // Эта переменная не используется в выражении
                "z", 10.0
        );

        // Act: Выполняем вычисление
        double result = expression.eval(assignments);

        // Assert: Проверяем, что результат верный (5 + 10 = 15)
        assertEquals(15.0, result);
    }
}