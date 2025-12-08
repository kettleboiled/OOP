package ru.nsu.ryzhneva.gradebook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ru.nsu.ryzhneva.gradebook.typesandgrades.Grade;
import ru.nsu.ryzhneva.gradebook.typesandgrades.TypeOfControl;

/**
 * Класс, представляющий один семестр обучения.
 * Хранит список дисциплин и методы анализа успеваемости за этот период.
 */
public class Semester {
    private int number;
    private List<Discipline> disciplines = new ArrayList<>();

    /**
     * Конструктор семестра.
     *
     * @param number номер семестра (начиная с 1).
     */
    public Semester(int number) {
        this.number = number;
    }

    /**
     * Добавляет дисциплину в семестр.
     *
     * @param discipline объект дисциплины.
     */
    public void addDiscipline(Discipline discipline) {
        disciplines.add(discipline);
    }

    /**
     * Получить номер семестра.
     *
     * @return номер семестра.
     */
    public int getNumber() {
        return number;
    }

    /**
     * Получить список всех дисциплин семестра.
     *
     * @return список дисциплин.
     */
    public List<Discipline> getDisciplines() {
        return Collections.unmodifiableList(disciplines);
    }

    /**
     * Проверяет, есть ли в этом семестре оценки "Удовлетворительно" или хуже.
     *
     * @param onlyExams если {@code true}, проверяет только дисциплины с типом EXAM.
     * @return {@code true}, если найдена плохая оценка.
     */
    public boolean hasBadGrades(boolean onlyExams) {
        for (Discipline discipline : disciplines) {
            boolean disciplineExam = false;
            if (!onlyExams || discipline.getControlType() == TypeOfControl.EXAM) {
                disciplineExam = true;
            }

            if (disciplineExam && discipline.getGrade().isBadGrade()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Проверяет, закрыт ли семестр только на "Отлично".
     * Используется для определения повышенной стипендии.
     *
     * @return {@code true}, если все дифференцированные оценки — EXCELLENT.
     */
    public boolean isExcellent() {
        if (disciplines.isEmpty()) {
            return false;
        }

        for (Discipline discipline : disciplines) {
            if (discipline.getGrade().isGraded() && discipline.getGrade() != Grade.EXCELLENT) {
                return false;
            }
        }
        return true;
    }

    /**
     * Проверяет, были ли в этом семестре экзамены.
     *
     * @return {@code true}, если есть хотя бы один экзамен.
     */
    public boolean hasExams() {
        for (Discipline discipline : disciplines) {
            if (discipline.getControlType() == TypeOfControl.EXAM) {
                return true;
            }
        }
        return false;
    }
}