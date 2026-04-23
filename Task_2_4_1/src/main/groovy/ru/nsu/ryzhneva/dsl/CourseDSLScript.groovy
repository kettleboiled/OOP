package ru.nsu.ryzhneva.dsl

import ru.nsu.ryzhneva.dsl.builders.TaskBuilder
import ru.nsu.ryzhneva.dsl.builders.GroupBuilder
import ru.nsu.ryzhneva.dsl.builders.CheckAssignmentBuilder
import ru.nsu.ryzhneva.dsl.builders.CheckpointBuilder

import ru.nsu.ryzhneva.domain.CourseConfig

/**
 * Базовый скрипт конфигурации DSL.
 */
abstract class CourseDSLScript extends Script {
    
    /**
     * Текущая объектная модель конфигурации, инициализирующаяся скриптом
     */
    CourseConfig config = new CourseConfig();

    /**
     * Создает или переопределяет учебную задачу.
     * @param id уникальный идентификатор задачи
     * @param closure Groovy-замыкание для настройки деталей задачи
     */
    void task(String id, Closure closure) {
        config.addTask(TaskBuilder.build(id, closure))
    }

    /**
     * Добавляет студенческую группу в общую конфигурацию.
     * @param name имя группы
     * @param closure замыкание для добавления студентов (метод `student`)
     */
    void group(String name, Closure closure) {
        config.addGroup(GroupBuilder.build(name, closure))
    }

    /**
     * Регистрация контрольной точки.
     * @param name Название контрольной точки
     * @param closure Замыкание для установки даты (метод `setDate`)
     */
    void checkpoint(String name, Closure closure) {
        config.addCheckpoint(CheckpointBuilder.build(name, closure))
    }

    /**
     * Создает задание на проверку.
     * @param closure замыкание для
     * конфигурации целей проверки: `tasks`, `students`, `groups`
     */
    void check(Closure closure) {
        config.setCheckAssignment(CheckAssignmentBuilder.build(closure))
    }

    /**
     * Устанавливает настраиваемые границы для
     * перевода баллов в итоговую оценку на курсе.
     *
     * @param excellent нижняя граница для оценки "Отлично"
     * @param good нижняя граница для оценки "Хорошо"
     * @param satisfactory нижняя граница для оценки "Удовлетворительно"
     */
    void gradeCriteria(int excellent, int good, int satisfactory) {
        config.setGradeCriteria(excellent, good, satisfactory)
    }

    /**
     * Импортирует конфигурацию из указанного файла.
     * Позволяет выносить медленно меняющиеся настройки в базовый скрипт.
     * @param filePath путь к импортируемому файлу конфигурации (в формате DSL)
     * @throws FileNotFoundException если указанный файл не найден
     */
    void includeConfig(String filePath) {
        File file = new File(filePath)
        if (!file.exists()) {
            throw new FileNotFoundException("Config file not found: ${file.absolutePath}")
        }
        evaluate(file)
    }

}
