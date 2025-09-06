package ru.nsu.ryzhneva;

import java.util.Arrays;

/**
 * Основной класс приложения.
 * Демонстрирует работу пирамидальной сортировки и запускает бенчмарки.
 */
public class Main {
    /**
     * Точка входа программы.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        int[] arr = new int[]{5, 4, 3, 2, 1};
        int[] res = HeapSort.heapsort(arr);
        System.out.println(Arrays.toString(res));

        System.out.println("\nRunning benchmarks:");
        Benchmark.bench();
    }
}
