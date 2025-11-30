package ru.nsu.ryzhneva.gradebook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import ru.nsu.ryzhneva.gradebook.typesandgrades.Grade;
import ru.nsu.ryzhneva.gradebook.typesandgrades.TypeOfControl;

/**
 * Класс электронной зачетной книжки студента ФИТ.
 */
public class GradeBook {
    private List<Semester> semesters = new ArrayList<>();
    private int id;

    /**
     * Создает новую зачетную книжку.
     *
     * @param id номер зачетной книжки.
     */
    public GradeBook(int id) {
        this.id = id;
    }

    /**
     * Добавляет данные о семестре в зачетную книжку.
     *
     * @param semester объект семестра.
     */
    public void addSemester(Semester semester) {
        semesters.add(semester);
    }

    /**
     * Получить список всех семестров.
     *
     * @return список семестров.
     */
    public List<Semester> getSemesters() {
        return Collections.unmodifiableList(semesters);
    }

    /**
     * Проверяет, закрыт ли конкретный семестр на "Отлично".
     *
     * @param semesterNumber номер семестра.
     * @return true, если семестр найден и он отличный.
     */
    public boolean isSemesterExcellent(int semesterNumber) {
        for (Semester s : semesters) {
            if (s.getNumber() == semesterNumber) {
                return s.isExcellent();
            }
        }
        return false;
    }

    /**
     * Проверяет критерии оценок для перевода на бюджет.
     *
     * @param currentSemester текущий семестр.
     * @return true, если последние две сессии сданы без троек.
     */
    public boolean checkBudgetEligibility(int currentSemester) {
        List<Semester> examSemesters = new ArrayList<>();
        for (Semester semester : semesters) {
            if (semester.getNumber() <= currentSemester && semester.hasExams()) {
                examSemesters.add(semester);
            }
        }

        examSemesters.sort(Comparator.comparingInt(Semester::getNumber).reversed());
        if (examSemesters.size() < 2) {
            return false;
        }
        return !examSemesters.get(0).hasBadGrades(true)
                && !examSemesters.get(1).hasBadGrades(true);
    }

    /**
     * Вычисляет текущий средний балл.
     *
     * @return среднее арифметическое всех оценок.
     */
    public double getAverageScore() {
        int count = 0;
        double sum = 0;


        for (Semester semester : semesters) {
            for (Discipline discipline : semester.getDisciplines()) {
                if (discipline.getGrade().isGraded()) {
                    sum += discipline.getGrade().getScore();
                    count++;
                }
            }
        }

        if (count == 0) {
            return 0.0;
        }
        return sum / count;
    }

    /**
     * Проверяет возможность получения красного диплома.
     *
     * @return {@code true}, если все критерии выполнены.
     */
    public boolean canGetRedDiploma() {
        if (semesters.isEmpty()) {
            return false;
        }

        int countExcellentGrades = 0;
        int countAll = 0;

        for (Semester semester : semesters) {
            if (semester.hasBadGrades(false)) {
                return false;
            }

            for (Discipline discipline : semester.getDisciplines()) {
                if (discipline.getControlType() == TypeOfControl.THESIS_DEFENSE) {
                    if (discipline.getGrade() != Grade.EXCELLENT) {
                        return false;
                    }
                }

                if (discipline.getGrade().isGraded()) {
                    countAll++;
                    if (discipline.getGrade() == Grade.EXCELLENT) {
                        countExcellentGrades++;
                    }
                }
            }
        }

        if (countAll == 0) {
            return false;
        }

        double res = (double) countExcellentGrades / countAll;
        return res >= 0.75;
    }
}