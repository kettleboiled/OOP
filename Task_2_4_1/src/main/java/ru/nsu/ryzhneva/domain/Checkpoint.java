package ru.nsu.ryzhneva.domain;

import java.time.LocalDate;

/**
 * Объектная модель контрольной точки.
 */
public class Checkpoint {
    private String name;
    private LocalDate date;

    /**
     * @return название контрольной точки
     */
    public String getName() {
        return name;
    }

    /**
     * @param name название контрольной точки для установки
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return дата контрольной точки в формате {@link LocalDate}
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Устанавливает дату дедлайна чеклоинта как объект типа {@link LocalDate}.
     * @param date дата
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Парсит и устанавливает дату контрольной точки из строки.
     * @param date строка в формате ISO (например YYYY-MM-DD)
     */
    public void setDate(String date) {
        this.date = LocalDate.parse(date);
    }

}
