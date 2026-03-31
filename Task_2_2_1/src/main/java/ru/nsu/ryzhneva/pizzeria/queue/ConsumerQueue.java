package ru.nsu.ryzhneva.pizzeria.queue;


/**
 * Очередь для потребителя
 * с возможностью получения единичного элемента.
 *
 * @param <T> тип элемента.
 */
public interface ConsumerQueue<T> {

    /**
     * Получить элемент (один) из очереди.
     *
     * @return извлеченный элемент, либо {@code null},
     * если очередь пуста и переведена в закрытое состояние.
     * @throws InterruptedException если поток был
     * прерван во время ожидания новых элементов.
     */
    T get() throws InterruptedException, QueueClosedException;
}