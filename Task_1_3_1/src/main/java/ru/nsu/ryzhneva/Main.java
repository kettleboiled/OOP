package ru.nsu.ryzhneva;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Класс Main для ручной проверки StreamSubstringFinder.
 */
public class Main {
    /**
     * Точка входа.
     * @param args аргументы (не используются).
     */
    public static void main(String[] args) {

        String fileName = "input.txt";
        String substring = "бра";
        String content = "абракадабракадабра - абракадабракадабра абракадабракадабра абракадабракадабра";

        Path filePath = Paths.get(fileName);

        System.out.println("--- Демонстрация StreamSubstringFinder (ООП + Рабин-Карп) ---");
        System.out.println("Файл: " + fileName);
        System.out.println("Содержимое: \"" + content + "\"");
        System.out.println("Ищем: \"" + substring + "\"");

        try {
            Files.writeString(filePath, content, StandardCharsets.UTF_8);

            FindSubstring finder = new FindSubstring(substring);
            List<Long> indices = finder.find(fileName);
            System.out.println("\nРезультат (найденные индексы): " + indices);

            String expected = "[1, 8, 15, 22, 29, 36, 41, 48, 55, 60, 67, 74]";
            System.out.println("Ожидаемый результат:        " + expected);

            if (indices.toString().equals(expected)) {
                System.out.println("Успешно");
            } else {
                System.out.println("Ошибка: (ожидаемый и реальный результаты не совпадают)");
            }

        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка конфигурации: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("\nПроизошла ошибка во время выполнения:");
            e.printStackTrace();
        } finally {
            try {
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                    System.out.println("\n(Временный файл " + fileName + " удален)");
                }
            } catch (IOException e) {
                System.err.println("Не удалось удалить временный файл " + fileName);
            }
        }
    }
}