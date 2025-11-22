package ru.nsu.ryzhneva.gradebook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс электронной зачетной книжки студента ФИТ.
 * Реализует логику хранения оценок и вычисления
 * академических показателей:
 * среднего балла, возможности перевода на бюджет,
 * получения красного диплома
 * и повышенной стипендии.
 */
public class GradeBook {
    public List<DisciplineData> grades = new ArrayList<>();

    /**
     * Вычисляет текущий средний балл.
     * @return среднее арифметическое всех оценок.
     */
    public double getAverageScore() {
        int count = 0;
        double sum = 0;


        for (DisciplineData discipline : grades) {
            if (discipline.getGrade().isGraded()) {
                sum += discipline.getGrade().getGrade();
                count++;
            }
        }

        if (count == 0) {
            return 0.0;
        }

        return sum / count;
    }

    /**
     * Проверяет возможность перевода студента
     * с платной формы обучения на бюджетную.
     *
     * @return можно или нет.
     */
    public boolean TransferToBudget() {
        List<Integer> examSemesters = new ArrayList<>();
        for (DisciplineData discipline : grades) {
            if (discipline.getControlType() == TypeOfControl.EXAM) {
                if (!examSemesters.contains(discipline.getSemester())) {
                    examSemesters.add(discipline.getSemester());
                }
            }
        }

        examSemesters.sort(Collections.reverseOrder());

        if (examSemesters.size() < 2) {
            return false;
        }

        int lastSemester = examSemesters.get(0);
        int prevSemester = examSemesters.get(1);

        for (DisciplineData discipline : grades) {
            boolean suitableSemesters = false;
            if (discipline.getSemester() == lastSemester || discipline.getSemester() == prevSemester) {
                suitableSemesters = true;
            }

            if (suitableSemesters && discipline.getControlType() == TypeOfControl.EXAM) {
                if (discipline.getGrade() == Grade.SATISFACTORY
                || discipline.getGrade() == Grade.UNSATISFACTORY
                || discipline.getGrade() == Grade.FAIL) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Проверяет возможность получения красного диплома.
     * @return {@code true}, если все критерии выполнены, иначе {@code false}.
     */
    public boolean canGetRedDiploma() {
        if (grades.isEmpty()) {
            return false;
        }

        int countExcellentGrades = 0;
        int countAll = 0;

        for (DisciplineData discipline : grades) {

            if (discipline.getControlType() == TypeOfControl.THESIS_DEFENSE) {
                if (discipline.getGrade() != Grade.EXCELLENT) {
                    return false;
                }
            }

            if (discipline.getGrade().isGraded()) {
                if (discipline.getGrade() == Grade.SATISFACTORY) {
                    return false;
                }

                countAll++;
                if (discipline.getGrade() == Grade.EXCELLENT) {
                    countExcellentGrades++;
                }
            }
        }

        if (countAll == 0) {
            return false;
        }

        double res = (double) countExcellentGrades / countAll;
        return res >= 0.75;
    }

    /**
     * Проверяет возможность получения
     * повышенной государственной академической стипендии (ПГАС)
     * @return {@code true}, все оценки - "Отлично", иначе {@code false}.
     */
    public boolean canGetPGAS() {
        int currentSemester = 0;
        for (DisciplineData discipline : grades) {
            if (discipline.getSemester() > currentSemester) {
                currentSemester = discipline.getSemester();
            }
        }

        if (currentSemester == 0) {
            return false;
        }

        for (DisciplineData discipline : grades) {
            if (discipline.getSemester() == currentSemester) {
                if (discipline.getGrade() != Grade.EXCELLENT && discipline.getGrade().isGraded()) {
                    return false;
                }
            }
        }
        return true;
    }

}