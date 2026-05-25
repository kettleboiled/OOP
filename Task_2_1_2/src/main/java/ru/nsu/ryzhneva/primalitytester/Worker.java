package ru.nsu.ryzhneva.primalitytester;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Сетевой воркер, принимающий задачи от Master и выполняющий проверку чанка.
 */
public class Worker {

    private final PrimalityTester tester;
    private final int port;

    /**
     * Конструктор.
     *
     * @param port порт для входящих соединений
     * @param tester стратегия проверки простоты
     */
    public Worker(int port, PrimalityTester tester) {
        this.port = port;
        this.tester = tester;
    }

    /**
     * Запускает цикл приема соединений и обработки задач.
     */
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Worker запущен на порту " + port + " и ожидает задач...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {

                    ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                    out.flush();

                    ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

                    TaskRequest request = (TaskRequest) in.readObject();
                    System.out.printf("Принята задача ID: %d, размер данных: %d элементов.%n",
                            request.getTaskId(), request.getNumberChunk().length);

                    boolean hasComposite = tester.hasComposite(request.getNumberChunk());

                    TaskResponse response = new TaskResponse(request.getTaskId(), hasComposite);
                    out.writeObject(response);
                    out.flush();

                    System.out.printf("Задача ID: %d выполнена. Результат: %b%n",
                            request.getTaskId(), hasComposite);
                } catch (ClassNotFoundException e) {
                    System.err.println("Ошибка сериализации: получен неизвестный класс. "
                            + e.getMessage());
                } catch (IOException e) {
                    System.err.println("Ошибка ввода-вывода при связи с Master-узлом: "
                            + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Критическая ошибка: невозможно запустить Worker на порту "
                    + port);
            e.printStackTrace();
        }
    }
}
