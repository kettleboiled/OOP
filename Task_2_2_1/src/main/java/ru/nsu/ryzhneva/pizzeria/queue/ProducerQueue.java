package ru.nsu.ryzhneva.pizzeria.queue;


/**
 * Очередь для производителя.
 *
 * @param <T> тип элемента.
 */
public interface ProducerQueue<T> {

    /**
     * Добавить элемент в очередь.
     *
     * @param item элемент для добавления.
     * @throws InterruptedException если поток был прерван
     *     во время ожидания.
     * @throws QueueClosedException если очередь закрыта.
     */
    void put(T item) throws InterruptedException, QueueClosedException;
}