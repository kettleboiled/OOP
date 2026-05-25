package ru.nsu.ryzhneva.primalitytester;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Точка входа.
 */
public class Main {

    /**
     * Запускает приложение в выбранном режиме.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            startWorker(8081, "sequential", 4);
            return;
        }

        String mode = args[0].toLowerCase();
        switch (mode) {
            case "worker":
                handleWorkerArgs(args);
                break;
            case "master":
                handleMasterArgs(args);
                break;
            default:
                System.out.println("Неизвестный режим. Используйте: worker|master");
                printUsage();
        }
    }

    /**
     * Обрабатывает аргументы режима worker и запускает узел.
     *
     * @param args аргументы командной строки
     */
    private static void handleWorkerArgs(String[] args) {
        int port = 8081;
        String strategy = "sequential";
        int threadsCount = 4;

        if (args.length > 1) {
            port = Integer.parseInt(args[1]);
        }
        if (args.length > 2) {
            strategy = args[2];
        }
        if (args.length > 3) {
            threadsCount = Integer.parseInt(args[3]);
        }

        startWorker(port, strategy, threadsCount);
    }

    /**
     * Обрабатывает аргументы режима master и запускает вычисление.
     *
     * @param args аргументы командной строки
     */
    private static void handleMasterArgs(String[] args) {
        if (args.length < 3) {
            System.out.println("Недостаточно аргументов для master.");
            printUsage();
            return;
        }

        List<InetSocketAddress> workers = parseWorkers(args[1]);
        int[] numbers = parseNumbers(args, 2);

        Master master = new Master(workers);
        boolean result = master.hasComposite(numbers);
        System.out.println("Результат: " + result);
    }

    /**
     * Запускает worker с выбранной стратегией проверки простоты.
     *
     * @param port порт для входящих соединений
     * @param strategy стратегия: sequential|stream|threads
     * @param threadsCount количество потоков для стратегии threads
     */
    private static void startWorker(int port, String strategy, int threadsCount) {
        PrimalityTester tester;

        switch (strategy.toLowerCase()) {
            case "sequential":
                tester = new SequentialPrimalityTester();
                break;
            case "stream":
                tester = new ParallelStreamPrimalityTester();
                break;
            case "threads":
                tester = new ParallelThreadPrimalityTester(threadsCount);
                break;
            default:
                System.out.println("Неизвестная стратегия. Используется sequential.");
                tester = new SequentialPrimalityTester();
        }

        Worker worker = new Worker(port, tester);
        worker.start();
    }

    /**
     * Парсит список узлов вида {@code host:port,host:port}.
     *
     * @param workersArg строка со списком узлов
     * @return список адресов воркеров
     */
    private static List<InetSocketAddress> parseWorkers(String workersArg) {
        String[] parts = workersArg.split(",");
        List<InetSocketAddress> workers = new ArrayList<>();
        for (String part : parts) {
            String[] hostPort = part.trim().split(":");
            if (hostPort.length != 2) {
                throw new IllegalArgumentException("Неверный адрес воркера: " + part);
            }
            String host = hostPort[0];
            int port = Integer.parseInt(hostPort[1]);
            workers.add(new InetSocketAddress(host, port));
        }
        return workers;
    }

    /**
     * Парсит массив чисел из аргументов командной строки.
     *
     * @param args массив аргументов
     * @param startIndex индекс первого числа
     * @return массив чисел
     */
    private static int[] parseNumbers(String[] args, int startIndex) {
        int[] numbers = new int[args.length - startIndex];
        for (int i = startIndex; i < args.length; i++) {
            numbers[i - startIndex] = Integer.parseInt(args[i]);
        }
        return numbers;
    }

    /**
     * Выводит справку по запуску приложения.
     */
    private static void printUsage() {
        System.out.println("Примеры:\n"
                + "  worker <port> [sequential|stream|threads] [threadsCount]\n"
                + "  master <host:port,host:port> <n1> <n2> ...");
    }
}