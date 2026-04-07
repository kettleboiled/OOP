package ru.nsu.ryzhneva.pizzeria.queue;

import java.util.List;

/**
 * Очередь для потребителя
 * с возможностью получения списка элементов.
 *
 * @param <T> тип элемента.
 */
public interface BatchConsumerQueue<T> {

    /**
     * Получить список элементов из очереди.
     *
     * @param size максимальный размер извлекаемого пакета.
     * @return список извлеченных элементов.
     * @throws InterruptedException если поток был прерван во время ожидания.
     */
    List<T> getBatch(int size) throws InterruptedException, QueueClosedException;
}