package ru.nsu.ryzhneva;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.nsu.ryzhneva.primalitytester.ParallelStreamPrimalityTester;
import ru.nsu.ryzhneva.primalitytester.ParallelThreadPrimalityTester;
import ru.nsu.ryzhneva.primalitytester.PrimalityTester;
import ru.nsu.ryzhneva.primalitytester.SequentialPrimalityTester;
/**
 * Тесты для интерфейса PrimalityTester
 * и наследуемых от него.
 */

class PrimalityTesterTest {

    static Stream<PrimalityTester> getTesters() {
        return Stream.of(
                new SequentialPrimalityTester(),
                new ParallelStreamPrimalityTester(),
                new ParallelThreadPrimalityTester(1),
                new ParallelThreadPrimalityTester(4)
        );
    }

    @ParameterizedTest
    @MethodSource("getTesters")
    void testPromptExample1_True(PrimalityTester tester) {
        int[] input = {6, 8, 7, 13, 5, 9, 4};
        Assertions.assertTrue(tester.hasComposite(input),
                "Should return true for array with composites "
                        + tester.getClass().getSimpleName());
    }

    @ParameterizedTest
    @MethodSource("getTesters")
    void testPromptExample2_False(PrimalityTester tester) {
        int[] input = {
                20319251, 6997901, 6997927, 6997937, 17858849, 6997967,
                6998009, 6998029, 6998039, 20165149, 6998051, 6998053
        };
        Assertions.assertFalse(tester.hasComposite(input),
                "Should return false for array of primes " + tester.getClass().getSimpleName());
    }

    @ParameterizedTest
    @MethodSource("getTesters")
    void testSquareOfPrime(PrimalityTester tester) {
        int[] input = {3, 5, 9, 11};
        Assertions.assertTrue(tester.hasComposite(input), "Should detect 9 as composite");
    }

    @ParameterizedTest
    @MethodSource("getTesters")
    void testEmptyArray(PrimalityTester tester) {
        Assertions.assertFalse(tester.hasComposite(new int[]{}), "Empty array has no composites");
    }

    @ParameterizedTest
    @MethodSource("getTesters")
    void testSingleComposite(PrimalityTester tester) {
        Assertions.assertTrue(tester.hasComposite(new int[]{4}), "4 is composite");
    }
}