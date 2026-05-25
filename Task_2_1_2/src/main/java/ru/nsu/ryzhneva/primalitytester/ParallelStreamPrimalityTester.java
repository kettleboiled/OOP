package ru.nsu.ryzhneva.primalitytester;

import java.util.Arrays;

/**
 * Реализация интерфейса {@link PrimalityTester}, использующая parallelStream
 * для параллельной обработки данных.
 */
public class ParallelStreamPrimalityTester implements PrimalityTester {
    /**
     * Проверяет массив параллельно с использованием {@code parallelStream()}.
     *
     * @param arr массив.
     * @return true - найдено составное число, иначе false.
     */
    @Override
    public boolean hasComposite(int[] arr) {
        return !Arrays.stream(arr).parallel()
                .allMatch(number -> isPrime(number));
    }
}
