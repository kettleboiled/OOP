package ru.nsu.ryzhneva.gradebook;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Класс, представляющий студента.
 * Хранит информацию о форме обучения, текущем семестре и зачетной книжке.
 */
public class Student {
    private String name;
    private boolean isBudget;
    private GradeBook gradeBook;
    private int currentSemester;

    /**
     * Создает студента.
     *
     * @param name ФИО
     * @param isBudget платная форма обучения?
     * @param gradeBookId номер зачетки
     */
    public Student(String name, boolean isBudget, int gradeBookId) {
        this.name = name;
        this.isBudget = isBudget;
        this.gradeBook = new GradeBook(gradeBookId);
        this.currentSemester = 1;
    }

    /**
     * Получить зачетную книжку студента.
     *
     * @return объект зачетной книжки.
     */
    public GradeBook getGradeBook() {
        return gradeBook;
    }

    /**
     * Установить форму обучения.
     *
     * @param isBudget {@code true} для бюджета, {@code false} для платного.
     */
    public void setIsBudget(boolean isBudget) {
        this.isBudget = isBudget;
    }

    /**
     * Получить номер текущего семестра обучения.
     *
     * @return номер семестра.
     */
    public int getCurrentSemester() {
        return currentSemester;
    }

    /**
     * Метод для перевода студента на следующий семестр.
     * Увеличивает счетчик текущего семестра.
     */
    public void moveToNextSemester() {
        this.currentSemester++;
    }

    /**
     * Проверяет право студента на получение
     * повышенной стипендии в текущем семестре.
     *
     * @return {@code true}, если стипендия положена.
     */
    public boolean hasIncreasedScholarship() {
        if (!isBudget) {
            return false;
        }

        Semester semesterObj = null;
        for (Semester s : gradeBook.getSemesters()) {
            if (s.getNumber() == this.currentSemester) {
                semesterObj = s;
                break;
            }
        }

        if (semesterObj == null) {
            return false;
        }

        return semesterObj.isExcellent();
    }

    /**
     * Проверяет возможность перевода
     * с платной формы обучения на бюджетную.
     *
     * @return {@code true}, если перевод возможен.
     */
    public boolean transferToBudget() {
        if (isBudget) {
            return false;
        }

        List<Semester> examSemesters = new ArrayList<>();
        for (Semester semester : gradeBook.getSemesters()) {
            if (semester.getNumber() <= currentSemester && semester.hasExams()) {
                examSemesters.add(semester);
            }
        }

        examSemesters.sort(Comparator.comparingInt(Semester::getNumber).reversed());

        if (examSemesters.size() < 2) {
            return false;
        }

        if (!examSemesters.get(0).hasBadGrades(true)
                && !examSemesters.get(1).hasBadGrades(true)) {
            return true;
        }
        return false;
    }
}

