package ru.nsu.ryzhneva.results;

import java.util.HashMap;
import java.util.Map;
import ru.nsu.ryzhneva.domain.Student;

/**
 * Хранит суммарную аналитику по одному студенту, собранную за время
 * работы приложения: данные о его задачах {@link TaskResult}
 * и посчитанный процент учебной активности на основе истории коммитов.
 */
public class StudentResult {
    private final Student student;
    private final Map<String, TaskResult> taskResults = new HashMap<>();
    private double activityPercentage = 0.0;

    /**
     * Конструктор.
     *
     * @param student объект студента
     */
    public StudentResult(Student student) {
        this.student = student;
    }

    /**
     * @return модель {@link Student}, хранящая имя и GitHub логин
     */
    public Student getStudent() {
        return student;
    }

    /**
     * @return ассоциативный массив или словарь-отображение (Map) результатов
     * прохождения задач для текущего студента.
     * Ключом выступает идентификатор (id) задачи.
     */
    public Map<String, TaskResult> getTaskResults() {
        return taskResults;
    }

    /**
     * Запоминает и сохраняет данные проверки для отдельной задачи в словарь-карту.
     * @param taskId строковое представление ID задачи
     * @param result данные: сколько прошло тестов, checkstyle
     */
    public void addTaskResult(String taskId, TaskResult result) {
        taskResults.put(taskId, result);
    }

    /**
     * @return численное значение активности как десятичная дробь (от 0.0 до 1.0)
     */
    public double getActivityPercentage() {
        return activityPercentage;
    }

    /**
     * Устанавливает вычисленное из Git-репозитория значение "активности".
     * @param activityPercentage коэффициент от 0.0 до 1.0 включительно
     */
    public void setActivityPercentage(double activityPercentage) {
        this.activityPercentage = activityPercentage;
    }
}
