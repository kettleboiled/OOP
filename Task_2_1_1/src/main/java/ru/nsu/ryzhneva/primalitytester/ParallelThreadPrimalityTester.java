package ru.nsu.ryzhneva.primalitytester;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Реализация интерфейса {@link PrimalityTester}, использующая явное управление потоками
 * через {@link Thread}.
 */
public class ParallelThreadPrimalityTester implements PrimalityTester {
    private int countThreads;

    /**
     * Конструктор класса.
     *
     * @param countThreads количество потоков.
     * @throws IllegalArgumentException если количество потоков меньше 1.
     */
    public ParallelThreadPrimalityTester(int countThreads) {
        if (countThreads < 1) {
            throw new IllegalArgumentException("Incorrect numerical threads");
        }
        this.countThreads = countThreads;
    }

    /**
     * Запускает указанное количество потоков.
     * Если какой-то из них находит составное число,
     * то все потоки прекращают работу.
     *
     * @param arr массив.
     * @return true, если найдено составное число, иначе false.
     * @throws RuntimeException если выполнение потоков было прервано (InterruptedException).
     */
    @Override
    public boolean hasComposite(int[] arr) {
        if (arr.length == 0) {
            return false;
        }

        List<Thread> threads = new ArrayList<>(countThreads);
        AtomicBoolean foundComposite = new AtomicBoolean(false);

        int blockSize = (arr.length + countThreads - 1) / countThreads;

        for (int i = 0; i < countThreads; i++) {
            int start = blockSize * i;
            int end = Math.min(arr.length, start + blockSize);

            if (arr.length <= start) {
                break;
            }

            Runnable task = () -> {
                for (int j = start; j <  end; j++) {
                    if (foundComposite.get()) {
                        return;
                    }

                    if (!isPrime(arr[j])) {
                        foundComposite.set(true);
                        return;
                    }
                }
            };

            Thread t = new Thread(task);
            threads.add(t);
            t.start();
        }


        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread execution interrupted", ex);
            }
        }
        return foundComposite.get();
    }
}
