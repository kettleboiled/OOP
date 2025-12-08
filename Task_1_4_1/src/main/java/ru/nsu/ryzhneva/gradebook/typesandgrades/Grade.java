package ru.nsu.ryzhneva.gradebook.typesandgrades;

/**
 * Перечисление возможных оценок.
 * PASS - зачет, FAIL - незачет.
 * UNDEFINED - нет оценки.
 * UNSATISFACTORY - 2 / не сдал.
 */
public enum Grade {
    EXCELLENT(5),
    GOOD(4),
    SATISFACTORY(3),
    UNSATISFACTORY(2),
    PASS(1),
    FAIL(-1),
    UNDEFINED(0);

    private final int score;

    /**
     * Конструктор оценки.
     *
     * @param score числовой вес оценки.
     */
    Grade(int score) {
        this.score = score;
    }

    /**
     * Возвращает числовое представление оценки.
     *
     * @return целое число, соответствующее оценке.
     */
    public int getScore() {
        return score;
    }

    /**
     * Проверяет, является ли оценка дифференцированной.
     *
     * @return {@code true}, если оценка участвует в расчете среднего балла;
     * {@code false} в противном случае.
     */
    public boolean isGraded() {
        if (this == EXCELLENT || this == GOOD
                || this == SATISFACTORY || this == UNSATISFACTORY) {
            return true;
        }
        return false;
    }

    /**
     * Проверяет, является ли оценка неудовлетворительной.
     *
     * @return {@code true}, если оценка 3, 2 или Незачет.
     */
    public boolean isBadGrade() {
        if (this == SATISFACTORY
                || this == UNSATISFACTORY || this == FAIL) {
            return true;
        }
        return false;
    }
}
