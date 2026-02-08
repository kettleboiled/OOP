package ru.nsu.ryzhneva;

import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.primalitytester.ParallelStreamPrimalityTester;
import ru.nsu.ryzhneva.primalitytester.ParallelThreadPrimalityTester;
import ru.nsu.ryzhneva.primalitytester.PrimalityTester;
import ru.nsu.ryzhneva.primalitytester.SequentialPrimalityTester;

import java.io.IOException;
import java.util.Arrays;

/**
 * Основной тест для сравнения производительности
 * разных методов проверки массива
 * на наличие составного числа.
 */
public class BenchmarkTest {

    @Test
    void runBenchmark() throws IOException {
        System.out.println("Preparing data...");
        int arraySize = 5000;
        int[] data = new int[arraySize];
        Arrays.fill(data, 20319251);
        data[arraySize - 1] = 20165149;

        System.out.println("Data size: " + arraySize);
        System.out.println("Available processors: " + Runtime.getRuntime().availableProcessors());
        System.out.println("Starting benchmark...\n");
        warmUp(data);

        measure("Sequential", new SequentialPrimalityTester(), data);

        int cores = Runtime.getRuntime().availableProcessors();
        for (int i = 2; i <= cores; i++) {
            measure("Threads (" + i + ")", new ParallelThreadPrimalityTester(i), data);
        }

        measure("ParallelStream", new ParallelStreamPrimalityTester(), data);
    }

    /**
     * Замеряет время выполнения.
     *
     * @param label метка для вывода в консоль.
     * @param tester экземпляр тестируемой стратегии.
     * @param data тестовые данные.
     */
    private void measure(String label, PrimalityTester tester, int[] data) {
        System.gc();

        long start = System.nanoTime();
        boolean result = tester.hasComposite(data);
        long end = System.nanoTime();

        double ms = (end - start) / 1_000_000.0;

        System.out.printf("%-20s | Time: %8.2f ms | Result: %b%n", label, ms, result);

        if (result) {
            throw new RuntimeException("Test failed for "
                    + label
                    + ": found composite number where none expected!");
        }
    }

    /**
     * Выполняет предварительный прогон кода для JIT-компиляции.
     *
     * @param data данные для прогрева.
     */
    private void warmUp(int[] data) {
        System.out.print("Warming up JVM... ");
        PrimalityTester t1 = new SequentialPrimalityTester();
        PrimalityTester t2 = new ParallelStreamPrimalityTester();
        int[] warmUpData = Arrays.copyOf(data, 100);
        for (int i = 0; i < 50; i++) {
            t1.hasComposite(warmUpData);
            t2.hasComposite(warmUpData);
        }
        System.out.println("Done.\n");
    }
}