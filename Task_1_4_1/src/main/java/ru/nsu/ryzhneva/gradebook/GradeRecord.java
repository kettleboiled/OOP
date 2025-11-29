package ru.nsu.ryzhneva.gradebook;

import java.time.LocalDate;
import ru.nsu.ryzhneva.gradebook.typesandgrades.Grade;

/**
 * Запись для списка оценок с датами.
 */
public class GradeRecord {
    private LocalDate date;
    private Grade grade;

    /**
     * Конструктор записи.
     *
     * @param grade оценка.
     * @param date дата.
     */
    public GradeRecord(Grade grade, LocalDate date) {
        this.date = date;
        this.grade = grade;
    }

    /**
     * Получить оценку.
     *
     * @return оценка.
     */
    public Grade getGrade() {
        return grade;
    }

    /**
     * Возвращает дату.
     *
     * @return дата.
     */
    public LocalDate getDate() {
        return date;
    }
}
