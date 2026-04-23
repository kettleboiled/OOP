package ru.nsu.ryzhneva.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Объектная модель конфигурации задания на проверку.
 * Ограничивает область действия (scope) тестов, определяя списки целевых задач 
 * (taskIds) и целевых сущностей (студентов лично, либо групп целиком).
 */
public class CheckAssignment {
    private List<String> taskIds = new ArrayList<>();
    private List<String> targetIdentifiers = new ArrayList<>();

    /**
     *
     * @return список идентификаторов задач для проверки
     */
    public List<String> getTaskIds() {
        return taskIds;
    }

    /**
     * Добавляет идентификатор задачи в пул проверки.
     *
     * @param taskId уникальный идентификатор задачи, существующий в конфигурации
     */
    public void addTask(String taskId) {
        this.taskIds.add(taskId);
    }

    /**
     *
     * @return список целевых идентификаторов для проверки
     */
    public List<String> getTargetIdentifiers() {
        return targetIdentifiers;
    }

    /**
     * Добавляет идентификационный маркер к цели проверки (может быть номером группы
     * или никнеймом студента из GitHub профиля).
     *
     * @param target название группы или логин
     */
    public void addTarget(String target) {
        this.targetIdentifiers.add(target);
    }
}