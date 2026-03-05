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
        PizzeriaConfig config = new PizzeriaConfig(
                2,
                new int[]{50, 100},
                2,
                new int[]{2, 3},
                10,
                2000
        );


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