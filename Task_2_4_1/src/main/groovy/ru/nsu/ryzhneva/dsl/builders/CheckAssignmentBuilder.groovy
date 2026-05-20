package ru.nsu.ryzhneva.dsl.builders

import ru.nsu.ryzhneva.dsl.ClosureProcessor
import ru.nsu.ryzhneva.domain.CheckAssignment

/**
 * Строитель (Builder) заданий на проверку (CheckAssignment) в рамках DSL.
 * Позволяет указать, какие именно задачи,
 * у каких студентов и групп необходимо запустить и проверить.
 */
class CheckAssignmentBuilder {
    private final CheckAssignment assignment

    private CheckAssignmentBuilder() {
        this.assignment = new CheckAssignment()
    }

    /**
     * Создает новое задание на проверку и инициализирует его через замыкание.
     * @param closure Groovy-замыкание, содержащее правила для проверки
     * @return настроенный объект CheckAssignment
     */
    static CheckAssignment build(Closure closure) {
        CheckAssignmentBuilder builder = new CheckAssignmentBuilder()
        ClosureProcessor.apply(builder, closure)
        return builder.assignment
    }

    /**
     * Добавляет идентификаторы задач для проверки.
     *
     * @param taskIds массив или список идентификаторов задач для проверки
     */
    void tasks(String... taskIds) {
        taskIds.each { String id ->
            this.assignment.addTask(id)
        }
    }

    /**
     * Указывает логины студентов на Github,
     * чьи работы необходимо проверить.
     * 
     * @param studentLogins массив или список логинов пользователей
     */
    void students(String... studentLogins) {
        studentLogins.each { String login ->
            this.assignment.addTarget(login)
        }
    }

    /**
     * Указывает номера (или названия) групп для
     * массовой проверки всех студентов этих групп.
     * 
     * @param groupNames массив или список целевых групп
     */
    void groups(String... groupNames) {
        groupNames.each { String groupName ->
            this.assignment.addTarget(groupName)
        }
    }

}
