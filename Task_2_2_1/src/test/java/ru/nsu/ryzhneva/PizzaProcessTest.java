package ru.nsu.ryzhneva;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.pizzeria.PizzaProcess;
import ru.nsu.ryzhneva.pizzeria.PizzeriaConfig;


/**
 * Тесты для класса PizzaProcess.
 */
class PizzaProcessTest {

    @Test
    void testGracefulShutdownPipeline() {
        PizzeriaConfig config = new PizzeriaConfig();
        config.bakersCount = 2;
        config.bakerSpeeds = new int[]{50, 100};
        config.couriersCount = 2;
        config.couriersTrunkVolume = new int[]{2, 3};
        config.warehouseSize = 10;

        PizzaProcess process = new PizzaProcess(config);

        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            assertDoesNotThrow(process::work,
                    "Метод work() не должен выбрасывать исключений");

            Thread.sleep(800);

            assertDoesNotThrow(process::close,
                    "Сбой при выполнении Graceful Shutdown");
        }, "Обнаружена взаимная блокировка (Deadlock) при завершении потоков");
    }
}