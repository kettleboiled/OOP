package ru.nsu.ryzhneva.pizzeria.queue;

/**
 * Исключение, выбрасываемое при попытке взаимодействия
 * с закрытой очередью.
 */
public class QueueClosedException extends Exception {

    /**
     * Конструктор.
     */
    public QueueClosedException() {
        super("Queue is closed.");
    }
}