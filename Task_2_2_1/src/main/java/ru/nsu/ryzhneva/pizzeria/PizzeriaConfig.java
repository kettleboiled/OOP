package ru.nsu.ryzhneva.pizzeria;

/**
 * Data Transfer Object (DTO)
 * для хранения параметров конфигурации пиццерии.
 */
public class PizzeriaConfig {
    public int bakersCount;
    public int[] bakerSpeeds; // время готовки в мс
    public int couriersCount;
    public int[] couriersTrunkVolume;
    public int warehouseSize;
    public int workTimeMs;

    /**
     * Конструктор.
     */
    public PizzeriaConfig() {}
}
