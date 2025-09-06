package ru.nsu.ryzhneva;

import java.util.Arrays;
public class Main {
    public static void main(String[] args) {
        int[] arr = new int[] {5, 4, 3, 2, 1};
        int[] res = HeapSort.heapsort(arr);

        System.out.println(Arrays.toString(res));

    }
}
