package ru.nsu.ryzhneva.primalitytester;

import java.io.Serializable;

/**
 * Запрос от Master к Worker, содержащий идентификатор задачи и чанк чисел.
 */
public class TaskRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private final long taskId;
    private final int[] numberChunk;

    /**
     * Конструктор.
     *
     * @param taskId уникальный идентификатор задачи
     * @param numberChunk массив чисел для проверки
     */
    public TaskRequest (long taskId, int[] numberChunk) {
        this.taskId = taskId;
        this.numberChunk = numberChunk;
    }

    /**
     * Возвращаеь id задачи.
     *
     * @return идентификатор задачи
     */
    public long getTaskId() {
        return taskId;
    }

    /**
     * Возвращает чанк чисел для проверки.
     *
     * @return чанк чисел для проверки
     */
    public int[] getNumberChunk() {
        return numberChunk;
    }
}
