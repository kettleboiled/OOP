package ru.nsu.ryzhneva.primalitytester;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Координатор распределенной проверки массива на наличие составных чисел.
 * Делит входные данные на чанки и отправляет их воркерам по сети.
 */
public class Master implements PrimalityTester {

    private static final int CONNECT_TIMEOUT_MS = 2000;
    private static final int READ_TIMEOUT_MS = 5000;
    private static final int MAX_CONSECUTIVE_FAILURES = 3;
    private static final int IDLE_SLEEP_MS = 10;

    private final List<InetSocketAddress> workerNodes;

    /**
     * Конструктор.
     *
     * @param workerNodes список вычислительных узлов.
     */
    public Master(List<InetSocketAddress> workerNodes) {
        if (workerNodes == null || workerNodes.isEmpty()) {
            throw new IllegalArgumentException("Список вычислительных узлов не может быть пустым");
        }
        this.workerNodes = workerNodes;
    }

    /**
     * Выполняет распределенную проверку массива.
     *
     * @param arr входной массив целых чисел
     * @return true, если найдено составное число; false, если все числа простые
     * @throws IllegalStateException если все воркеры недоступны
     */
    @Override
    public boolean hasComposite(int[] arr) {
        if (arr == null || arr.length == 0) {
            return false;
        }

        int chunksCount = Math.max(workerNodes.size() * 2, 2);
        int chunkSize = (int) Math.ceil((double) arr.length / chunksCount);

        Queue<TaskRequest> taskQueue = new ConcurrentLinkedQueue<>();
        long taskIdCounter = 0;

        for (int i = 0; i < arr.length; i += chunkSize) {
            int end = Math.min(arr.length, i + chunkSize);
            int[] chunk = Arrays.copyOfRange(arr, i, end);
            taskQueue.add(new TaskRequest(++taskIdCounter, chunk));
        }

        AtomicBoolean foundComposite = new AtomicBoolean(false);
        AtomicInteger pendingTasks = new AtomicInteger(taskQueue.size());
        AtomicInteger activeWorkers = new AtomicInteger(workerNodes.size());

        ExecutorService executorService = Executors.newFixedThreadPool(workerNodes.size());

        for (InetSocketAddress nodeAddress : workerNodes) {
            executorService.submit(() -> {
                int consecutiveFailures = 0;

                while (pendingTasks.get() > 0 && !foundComposite.get()) {
                    TaskRequest task = taskQueue.poll();

                    if (task == null) {
                        try {
                            Thread.sleep(IDLE_SLEEP_MS);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        continue;
                    }

                    try (Socket socket = new Socket()) {
                        socket.connect(nodeAddress, CONNECT_TIMEOUT_MS);
                        socket.setSoTimeout(READ_TIMEOUT_MS);

                        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                        out.flush();

                        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                        out.writeObject(task);
                        out.flush();

                        TaskResponse response = (TaskResponse) in.readObject();

                        if (response.hasCompositeNumber()) {
                            foundComposite.set(true);
                        }

                        pendingTasks.decrementAndGet();
                        consecutiveFailures = 0;

                    } catch (Exception e) {
                        System.err.printf("Узел %s недоступен. Переназначаем задачу ID: %d%n",
                                nodeAddress, task.getTaskId());
                        taskQueue.offer(task);
                        consecutiveFailures++;

                        if (consecutiveFailures >= MAX_CONSECUTIVE_FAILURES) {
                            activeWorkers.decrementAndGet();
                            return;
                        }

                        try {
                            Thread.sleep(IDLE_SLEEP_MS);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            });
        }

        try {
            while (pendingTasks.get() > 0 && !foundComposite.get()) {
                if (activeWorkers.get() == 0) {
                    executorService.shutdownNow();
                    throw new IllegalStateException(
                            "Критический сбой: Все вычислительные узлы недоступны.");
                }
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            executorService.shutdownNow();
        }

        return foundComposite.get();
    }
}
