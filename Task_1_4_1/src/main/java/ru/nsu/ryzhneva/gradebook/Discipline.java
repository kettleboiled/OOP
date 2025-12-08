package ru.nsu.ryzhneva.gradebook;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import ru.nsu.ryzhneva.gradebook.typesandgrades.Grade;
import ru.nsu.ryzhneva.gradebook.typesandgrades.TypeOfControl;

/**
 * Класс, представляющий запись об одной дисциплине
 * в зачетной книжке.
 * Хранит историю сдач и текущую актуальную оценку.
 */
public class Discipline {
    private String name;
    private TypeOfControl controlType;
    private List<GradeRecord> history = new ArrayList<>();

    /**
     * Создает новую запись о дисциплине.
     *
     * @param name        название дисциплины.
     * @param controlType вид контроля.
     * @param initialGrade начальная оценка.
     */
    public Discipline(String name, TypeOfControl controlType, Grade initialGrade) {
        this.name = name;
        this.controlType = controlType;
        addRecord(initialGrade, LocalDate.now());
    }

    /**
     * Конструктор с указанием конкретной даты первой сдачи.
     *
     * @param name         название дисциплины.
     * @param controlType  вид контроля.
     * @param initialGrade начальная оценка.
     */
    public Discipline(String name, TypeOfControl controlType, Grade initialGrade, LocalDate date) {
        this.name = name;
        this.controlType = controlType;
        addRecord(initialGrade, date);
    }

    /**
     * Метод пересдачи. Добавляет новую оценку в историю.
     *
     * @param grade новая оценка.
     * @param date дата пересдачи.
     */
    public void retake(Grade grade, LocalDate date) {
        addRecord(grade, date);
    }

    /**
     * Получить оценку за предмет.
     * Возвращает результат последней сдачи или пересдачи.
     *
     * @return объект перечисления.
     */
    public Grade getGrade() {
        if (history.isEmpty()) {
            return Grade.UNDEFINED;
        }
        return history.get(history.size() - 1).getGrade();
    }

    /**
     * Получить историю всех оценок по предмету.
     *
     * @return список записей {@link GradeRecord}.
     */
    public List<GradeRecord> getHistory() {
        return new ArrayList<>(history);
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

    /**
     * Добавить запись.
     */
    private void addRecord(Grade grade, LocalDate date) {
        history.add(new GradeRecord(grade, date));
    }
}
