package ru.nsu.ryzhneva.gradebook;

/**
 * Перечисление видов контроля.
 */
public enum TypeOfControl {
    TASK("Задание"),
    CONTROL_WORK("Контрольная"),
    COLLOQUIUM("Коллоквиум"),
    EXAM("Экзамен"),
    DIFF_CREDIT("Дифференцируемый зачет"),
    CREDIT("Зачет"),
    PRACTICE_REPORT("Защита отчета по практике"),
    THESIS_DEFENSE("Защита ВКР");

    private final String label;

    /**
     * Конструктор элемента перечисления.
     *
     * @param label текстовое представление вида контроля.
     */
    TypeOfControl(String label) {
        this.label = label;
    }
}
