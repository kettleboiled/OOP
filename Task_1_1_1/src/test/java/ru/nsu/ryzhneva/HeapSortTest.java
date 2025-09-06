package ru.nsu.ryzhneva;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HeapSortTest {
    /**
     * Проверка на пустой массив.
     */
    @Test
    public void testEmpty() {
        assertArrayEquals(new int[]{}, HeapSort.heapsort(new int[]{}));
    }

    /**
     * Проверка на отсортированный массив.
     */

    @Test
    public void testSorted() {
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, HeapSort.heapsort(new int[]{1, 2, 3, 4, 5}));
    }

    /**
     * Проверка массива, если он в обратном порядке.
     */
    @Test
    public void testReverse() {
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, HeapSort.heapsort(new int[]{5, 4, 3, 2, 1}));
    }

    /**
     * Проверка, есть ли в массиве дубликаты.
     */
    @Test
    public void testSame() {
        assertArrayEquals(new int[]{1, 1, 2, 2, 3}, HeapSort.heapsort(new int[]{2, 1, 2, 3, 1}));
    }

    /**
     * Проверка на корректность сортировки на большом случайном массиве (10 000 элементов).
     */
    @Test
    public void testManyElements() {
        int[] arr = new int[10000];
        java.util.Random a = new java.util.Random(0);
        for (int i = 0; i < arr.length; i++) {
            arr[i] = a.nextInt(10000);
        }
        int[] sorted = HeapSort.heapsort(arr);
        for (int i = 1; i < sorted.length; i++) {
            assertTrue(sorted[i - 1] <= sorted[i]);
        }
    }

    /**
     * Проверка, если в массиве один элемент.
     */
    @Test
    public void testSingle() {
        assertArrayEquals(new int[]{42}, HeapSort.heapsort(new int[]{42}));
    }

    /**
     * Запускается ли Main.
     */
    @Test
    void testMainRuns() {
        Main.main(new String[]{});
    }

    /**
     * Запускается ли Banchmark.
     */
    @Test
    void testBenchmarkRuns() {
        Benchmark.bench();
    }
}