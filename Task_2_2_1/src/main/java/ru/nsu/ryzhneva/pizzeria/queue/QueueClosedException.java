package ru.nsu.ryzhneva.pizzeria.queue;

/**
 * Исключение, выбрасываемое при попытке извлечь элемент
 * из пустой и закрытой очереди.
 */
public class QueueClosedException extends Exception {

    /**
     * Конструктор.
     */
    public QueueClosedException() {
        super("Queue is closed and empty.");
    }
}