package ru.nsu.ryzhneva.pizzeria.workers;

import java.util.ArrayList;
import java.util.List;

/**
 * Универсальная группа рабочих потоков.
 */
public class WorkerGroup {
    private final List<Thread> threads = new ArrayList<>();

    /**
     * Добавляет задачу и запускает в новом потоке.
     *
     * @param worker выполняемая задача.
     */
    public void addAndStart(Runnable worker) {
        Thread thread = new Thread(worker);
        thread.start();
        threads.add(thread);
    }

    /**
     * Блокирует выполнение до тех пор,
     * пока все потоки в группе не завершат работу.
     */
    public void awaitTermination() {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}