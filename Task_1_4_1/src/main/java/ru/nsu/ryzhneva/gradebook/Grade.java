package ru.nsu.ryzhneva.gradebook;

public enum Grade {
    EXCELLENT(5),
    GOOD(4),
    SATISFACTORY(3),
    UNSATISFACTORY(2),
    PASS(1),
    FAIL(-1),
    UNDEFINED(0);

    private int grade;

    /**
     * Конструктор оценки.
     *
     * @param grade числовой вес оценки.
     */
    Grade(int grade){
        this.grade = grade;
    }

    /**
     * Возвращает числовое представление оценки.
     * @return целое число, соответствующее оценке.
     */
    public int getGrade() {
        return grade;
    }

    /**
     * Проверяет, является ли оценка дифференцированной.
     * @return {@code true}, если оценка участвует в расчете среднего балла;
     * {@code false} в противном случае.
     */
    public boolean isGraded() {
        if (this == EXCELLENT || this == GOOD || this == SATISFACTORY || this == UNSATISFACTORY) {
            return true;
        }
        return false;
    }
}
