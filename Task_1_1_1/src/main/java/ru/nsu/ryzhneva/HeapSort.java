package ru.nsu.ryzhneva;

/**
 * Класс реализует классический алгоритм пирамидальной сортировки (heapsort).
 * Алгоритм имеет сложность O(n log n).
 */

public class HeapSort {

    // приватный конструктор, чтобы нельзя было создать объект
    private HeapSort() {}

    /**
     * Выполняет пирамидальную сортировку массива целых чисел.
     *
     * @param arr массив чисел для сортировки (изменяется на месте)
     * @return отсортированный массив (ссылка на тот же объект, что передан)
     */

    public static int[] heapsort(int[] arr) {
        int n = arr.length;

        // Построение кучи
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }

        // Извлечение элементов из кучи
        for (int i = n - 1; i > 0; i--) {
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            heapify(arr, i, 0);
        }
        return arr;
    }

    /**
     * Преобразует поддерево в кучу с корнем в индексе i.
     *
     * @param arr массив, представляющий кучу
     * @param n   размер кучи
     * @param i   индекс корня поддерева
     */
    private static void heapify(int[] arr, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && arr[left] > arr[largest]) {
            largest = left;
        }
        if (right < n && arr[right] > arr[largest]) {
            largest = right;
        }

        if (largest != i) {
            int temp = arr[i];
            arr[i] = arr[largest];
            arr[largest] = temp;

            heapify(arr, n, largest);
        }
    }
}
