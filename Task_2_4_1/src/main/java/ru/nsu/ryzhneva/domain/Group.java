package ru.nsu.ryzhneva.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Объектная модель студенческой группы.
 * Хранит список зарегистрированных студентов в рамках одной группы.
 */
public class Group {
    private String name;
    private List<Student> students = new ArrayList<>();

    /**
     * Создает новую группу с заданным названием.
     *
     * @param name Название группы
     */
    public Group(String name) {
        this.name = name;
    }

    /**
     * Возвращает название этой группы.
     *
     * @return название
     */
    public String getName() {
        return name;
    }

    /**
     * Задает название этой группы.
     *
     * @param name новое название группы
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Добавляет студента в список этой группы.
     *
     * @param student объект {@link Student}
     */
    public void addStudent(Student student) {
        this.students.add(student);
    }

    /**
     * Возвращает список всех студентов, состоящих в данной группе.
     *
     * @return список студентов
     */
    public List<Student> getStudents() {
        return students;
    }
}
