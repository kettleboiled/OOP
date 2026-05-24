package ru.nsu.ryzhneva;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.primalitytester.Main;

/**
 * Тесты для точки входа.
 */
class MainTest {

    @Test
    void unknownModePrintsUsage() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));
        try {
            Main.main(new String[]{"bad"});
        } finally {
            System.setOut(originalOut);
        }
        Assertions.assertTrue(out.toString().contains("Неизвестный режим"));
    }

    @Test
    void masterMissingArgsPrintsUsage() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));
        try {
            Main.main(new String[]{"master"});
        } finally {
            System.setOut(originalOut);
        }
        Assertions.assertTrue(out.toString().contains("Недостаточно аргументов"));
    }

    @Test
    void parseWorkersAndNumbers() throws Exception {
        Method parseWorkers = Main.class.getDeclaredMethod("parseWorkers", String.class);
        parseWorkers.setAccessible(true);
        List<InetSocketAddress> workers = (List<InetSocketAddress>) parseWorkers.invoke(
                null, "127.0.0.1:8081,localhost:8082");
        Assertions.assertEquals(2, workers.size());

        Method parseNumbers = Main.class.getDeclaredMethod(
                "parseNumbers", String[].class, int.class);
        parseNumbers.setAccessible(true);
        int[] numbers = (int[]) parseNumbers.invoke(null, new Object[]{
                new String[]{"master", "127.0.0.1:8081", "6", "8"}, 2});
        Assertions.assertArrayEquals(new int[]{6, 8}, numbers);
    }

    @Test
    void parseWorkersRejectsInvalidAddress() throws Exception {
        Method parseWorkers = Main.class.getDeclaredMethod("parseWorkers", String.class);
        parseWorkers.setAccessible(true);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            try {
                parseWorkers.invoke(null, "bad-address");
            } catch (Exception ex) {
                throw ex.getCause();
            }
        });
    }
}
