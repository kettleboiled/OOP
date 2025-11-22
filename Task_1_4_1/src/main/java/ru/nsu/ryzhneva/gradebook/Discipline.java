package ru.nsu.ryzhneva.gradebook;

import ru.nsu.ryzhneva.gradebook.typesandgrades.Grade;
import ru.nsu.ryzhneva.gradebook.typesandgrades.TypeOfControl;

/**
 * Класс, представляющий запись об одной дисциплине
 * в зачетной книжке.
 */
public class Discipline {
    private String name;
    private TypeOfControl controlType;
    private Grade grade;

    /**
     * Создает новую запись о дисциплине.
     *
     * @param name        название дисциплины.
     * @param controlType вид контроля.
     * @param grade       полученная оценка.
     */
    public Discipline(String name, TypeOfControl controlType, Grade grade) {
        this.name = name;
        this.controlType = controlType;
        this.grade = grade;
    }

    /**
     * Получить оценку за предмет.
     *
     * @return объект перечисления.
     */
    public Grade getGrade() {
        return grade;
    }

    /**
     * Получить тип контроля по предмету.
     *
     * @return объект перечисления.
     */
    public TypeOfControl getControlType() {
        return controlType;
    }

    /**
     * Получить название дисциплины.
     *
     * @return строковое название дисциплины.
     */
    public String getName() {
        return name;
    }
}
