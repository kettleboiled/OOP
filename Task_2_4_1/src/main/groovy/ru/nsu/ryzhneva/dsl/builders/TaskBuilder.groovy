package ru.nsu.ryzhneva.dsl.builders

import ru.nsu.ryzhneva.dsl.ClosureProcessor
import ru.nsu.ryzhneva.domain.Task

import java.time.LocalDate

/**
 * Строитель (Builder) для создания учебных задач (Task) в рамках DSL.
 * Предоставляет методы для установки параметров задачи (имя, баллы, дедлайны).
 */
class TaskBuilder {
    private final Task task

    private TaskBuilder(String id) {
        this.task = new Task()
        this.task.setId(id)
    }

    /**
     * Создает новую задачу и настраивает ее через переданное замыкание.
     * @param id уникальный идентификатор задачи
     * @param closure Groovy-замыкание для настройки деталей задачи
     * @return настроенный объект Task
     */
    static Task build(String id, Closure closure) {
        TaskBuilder builder = new TaskBuilder(id)
        ClosureProcessor.apply(builder, closure)
        return builder.task
    }

    /**
     * Устанавливает название задачи.
     * @param name имя задачи
     */
    void setName(String name) {
        this.task.setName(name)
    }

    /**
     * Устанавливает максимальное количество баллов за задачу.
     * @param maxPoints число баллов
     */
    void setMaxPoints(int maxPoints) {
        this.task.setMaxPoints(maxPoints)
    }

    /**
     * Устанавливает мягкий дедлайн.
     * @param dateStr дата в формате YYYY-MM-DD
     */
    void setSoftDeadline(String dateStr) {
        this.task.setSoftDeadline(LocalDate.parse(dateStr))
    }

    /**
     * Устанавливает жесткий дедлайн.
     * @param dateStr дата в формате YYYY-MM-DD
     */
    void setHardDeadline(String dateStr) {
        this.task.setHardDeadline(LocalDate.parse(dateStr))
    }
}
