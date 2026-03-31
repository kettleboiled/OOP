package ru.nsu.ryzhneva.pizzeria;

/**
 * Data Transfer Object (DTO)
 * для хранения параметров конфигурации пиццерии.
 */
public record PizzeriaConfig(
        int bakersCount,
        int[] bakerSpeeds,
        int couriersCount,
        int[] couriersTrunkVolume,
        int warehouseSize,
        int workTimeMs,
        int courierSpeed
) {
}