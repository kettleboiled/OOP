package ru.nsu.ryzhneva.gradebook;

/**
 * Класс, представляющий запись об одной дисциплине
 * в зачетной книжке.
 */
public class DisciplineData {
    private String name;
    private int semester;
    private TypeOfControl controlType;
    private Grade grade;

    /**
     * Создает новую запись о дисциплине.
     *
     * @param name        название дисциплины.
     * @param semester    номер семестра.
     * @param controlType вид контроля.
     * @param grade       полученная оценка.
     */
    public DisciplineData(String name, int semester, TypeOfControl controlType, Grade grade) {
        this.name = name;
        this.semester = semester;
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
     * Получить номер семестра, в котором сдавался предмет.
     *
     * @return номер семестра.
     */
    public int getSemester() {
        return semester;
    }
}
