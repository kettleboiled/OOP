package ru.nsu.ryzhneva.domain;

import java.time.LocalDate;

/**
 * Объектная модель для задачи.
 * Хранит максимальный доступный балл за нее, а также данные по дедлайнам.
 */
public class Task {
    private String id;
    private String name;
    private int maxPoints;
    private LocalDate softDeadline;
    private LocalDate hardDeadline;

    /**
     * Возвращает идентификатор задачи.
     *
     * @return строковый идентификатор задачи
     */
    public String getId() {
        return id;
    }

    /**
     * Устанавливает идентификатор задачи.
     *
     * @param id уникальный идентификатор задачи
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Возвращает наименование задачи.
     *
     * @return наименование задачи
     */
    public String getName() {
        return name;
    }

    /**
     * Устанавливает наименование задачи.
     *
     * @param name имя задачи
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Возвращает максимальное количество баллов.
     *
     * @return максимальное количество баллов, которое можно заработать за эту задачу
     */
    public int getMaxPoints() {
        return maxPoints;
    }

    /**
     * Задает максимальное количество баллов за эту задачу.
     *
     * @param maxPoints верхний порог баллов при успешной проверке
     */
    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    /**
     * Возвращает дату мягкого дедлайна.
     *
     * @return отражающий мягкий дедлайн
     */
    public LocalDate getSoftDeadline() {
        return softDeadline;
    }

    /**
     * Устанавливает дату мягкого дедлайна.
     *
     * @param softDeadline дата мягкого дедлайна
     */
    public void setSoftDeadline(LocalDate softDeadline) {
        this.softDeadline = softDeadline;
    }

    /**
     * Парсит и устанавливает мягкий дедлайн из строки.
     *
     * @param date строка в формате ISO (например YYYY-MM-DD)
     */
    public void setSoftDeadline(String date) {
        this.softDeadline = LocalDate.parse(date);
    }

    /**
     * Возвращает дату жесткого дедлайна.
     *
     * @return объект {@link LocalDate}, отражающий жесткий дедлайн (хард)
     */
    public LocalDate getHardDeadline() {
        return hardDeadline;
    }

    /**
     * Устанавливает дату жесткого дедлайна.
     *
     * @param hardDeadline дата жесткого дедлайна
     */
    public void setHardDeadline(LocalDate hardDeadline) {
        this.hardDeadline = hardDeadline;
    }

    /**
     * Парсит и устанавливает жесткий дедлайн из строки.
     *
     * @param date строка в формате ISO (например YYYY-MM-DD)
     */
    public void setHardDeadline(String date) {
        this.hardDeadline = LocalDate.parse(date);
    }
}
