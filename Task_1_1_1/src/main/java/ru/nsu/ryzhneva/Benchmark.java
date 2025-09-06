package ru.nsu.ryzhneva;
import java.util.Random;

public class Benchmark {

    private Benchmark() {};

    public static void bench() {
        int[] sizes = {1000, 2000, 4000, 8000, 16000};
        Random random = new Random();

        for (int n : sizes) {
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = random.nextInt();
            }

            long start = System.nanoTime();
            HeapSort.heapsort(arr);
            long end = System.nanoTime();

            System.out.printf("при n = %d, time = %.3f ms%n", n, (end - start) / 1e6);
        }
    }
}

