package ru.nsu.ryzhneva.primalitytester;

import java.io.Serializable;

/**
 * Ответ от Worker к Master с результатом проверки чанка.
 */
public class TaskResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private final long taskId;
    private final boolean hasCompositeNumber;


    /**
     * Конструктор.
     *
     * @param taskId идентификатор исходной задачи
     * @param hasCompositeNumber признак наличия составного числа в чанке
     */
    public TaskResponse (long taskId, boolean hasCompositeNumber) {
        this.taskId = taskId;
        this.hasCompositeNumber = hasCompositeNumber;
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
     * Проверка есть ли в массиве составное число.
     *
     * @return true, если в чанке найдено составное число
     */
    public boolean hasCompositeNumber() {
        return hasCompositeNumber;
    }
}
