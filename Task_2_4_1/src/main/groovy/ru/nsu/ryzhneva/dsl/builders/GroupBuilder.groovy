package ru.nsu.ryzhneva.dsl.builders

import ru.nsu.ryzhneva.dsl.ClosureProcessor
import ru.nsu.ryzhneva.domain.Group
import ru.nsu.ryzhneva.domain.Student

/**
 * Строитель (Builder) для создания студенческих групп (Group) в рамках конфигурации DSL.
 * Предоставляет методы для добавления студентов в группу.
 */
class GroupBuilder {
    private final Group group

    private GroupBuilder(String name) {
        this.group = new Group(name)
    }

    /**
     * Создает новую группу и выполняет ее конфигурацию с помощью замыкания.
     * @param name название группы
     * @param closure Groovy-замыкание, содержащее определения студентов
     * @return заполненный объект Group
     */
    static Group build(String name, Closure closure) {
        GroupBuilder builder = new GroupBuilder(name)
        ClosureProcessor.apply(builder, closure)
        return builder.group
    }

    /**
     * Добавляет студента в текущую группу.
     * @param githubUsername логин студента на Github
     * @param fullName полное ФИО студента
     * @param repositoryUrl ссылка на Github-репозиторий студента
     */
    void student(String githubUsername, String fullName, String repositoryUrl) {
        Student student = new Student()
        student.setGithubUsername(githubUsername)
        student.setFullName(fullName)
        student.setRepositoryUrl(repositoryUrl)
        this.group.addStudent(student)
    }
}
