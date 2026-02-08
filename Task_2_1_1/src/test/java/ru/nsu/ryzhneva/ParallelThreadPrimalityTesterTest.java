package ru.nsu.ryzhneva;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.primalitytester.ParallelThreadPrimalityTester;


/**
 * Тест для конструктора ParallelThreadPrimalityTester.
 */
class ParallelThreadPrimalityTesterTest {

    @Test
    void testConstructorThrowsExceptionOnInvalidThreads() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ParallelThreadPrimalityTester(0);
        }, "Constructor should throw exception for 0 threads");

        assertThrows(IllegalArgumentException.class, () -> {
            new ParallelThreadPrimalityTester(-5);
        }, "Constructor should throw exception for negative threads");
    }

    @Test
    void testMainRuns() {
        Main.main(new String[]{});
    }
}