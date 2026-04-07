package ru.nsu.ryzhneva.pizzeria.queue;

/**
 * Интерфейс для очередей, требующих явного закрытия.
 */
public interface CloseableQueue {

    /**
     * Метод закрытия очереди.
     */
    void close();
}