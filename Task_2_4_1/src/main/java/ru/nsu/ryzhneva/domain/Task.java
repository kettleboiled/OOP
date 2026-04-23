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
     * @return строковый идентификатор задачи
     */
    public String getId() {
        return id;
    }

    /**
     * @param id уникальный идентификатор задачи
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return наименование задачи
     */
    public String getName() {
        return name;
    }

    /**
     * @param name имя задачи
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return максимальное количество баллов, которое можно заработать за эту задачу
     */
    public int getMaxPoints() {
        return maxPoints;
    }

    /**
     * Задает максимальное количество баллов за эту задачу.
     * @param maxPoints верхний порог баллов при успешной проверке
     */
    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    /**
     * @return отражающий мягкий дедлайн
     */
    public LocalDate getSoftDeadline() {
        return softDeadline;
    }

    /**
     * @param softDeadline дата мягкого дедлайна
     */
    public void setSoftDeadline(LocalDate softDeadline) {
        this.softDeadline = softDeadline;
    }
    public void setSoftDeadline(String softDeadline) {
        this.softDeadline = LocalDate.parse(softDeadline);
    }

    /**
     * @return объект {@link LocalDate}, отражающий жесткий дедлайн (хард)
     */
    public LocalDate getHardDeadline() {
        return hardDeadline;
    }

    /**
     * @param hardDeadline дата жесткого дедлайна
     */
    public void setHardDeadline(LocalDate hardDeadline) {
        this.hardDeadline = hardDeadline;
    }
    public void setHardDeadline(String date) {
        this.hardDeadline = LocalDate.parse(date);
    }
}
