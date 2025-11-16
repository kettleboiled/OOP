package ru.nsu.ryzhneva;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Тесты для класса FindSubstring.
 */
class FindSubstringTest {

    @TempDir
    Path tempDir;

    @Test
    void testExampleFind() throws IOException {
        Path testFile = tempDir.resolve("input.txt");
        Files.writeString(testFile, "абракадабра", StandardCharsets.UTF_8);
        List<Long> expected = List.of(1L, 8L);

        FindSubstring finder = new FindSubstring("бра");
        List<Long> actual = finder.find(testFile.toString());

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testEmptyAndNullSubstring() throws IOException {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new FindSubstring(null);
        }, "Конструктор должен выбрасывать исключение для null");

        Path testFile = tempDir.resolve("empty_needle_test.txt");
        Files.writeString(testFile, "Этот файл будет прочитан", StandardCharsets.UTF_8);

        FindSubstring finder = new FindSubstring("");

        List<Long> expected = List.of();
        List<Long> actual = finder.find(testFile.toString());

        Assertions.assertEquals(expected, actual,
                "Поиск пустой строки должен возвращать пустой список");
    }

    @Test
    void testLargeFile() throws IOException {
        Path largeFile = tempDir.resolve("file.txt");
        String substring = "STRING";
        long approxSizeMB = 500;

        System.out.println("Начало теста на большом файле (500MB)...");
        long startTime = System.currentTimeMillis();

        generateLargeTestFile(largeFile, approxSizeMB, substring);

        long generationTime = System.currentTimeMillis();
        System.out.println("Файл (500МБ) сгенерирован за " + (generationTime - startTime) + " мс.");

        String pattern = "abcdefghijklmnopqrstuvwxyz_0123456789_АБВГДЕЖЗИКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ_";
        long targetSize = approxSizeMB * 1024 * 1024;
        long chunkSize = (pattern.length() * 1000L + substring.length());
        long written = 0;
        long expectedCount = 0;
        while(written < targetSize) {
            written += chunkSize;
            expectedCount++;
        }

        System.out.println("Ожидаемое кол-во вхождений: " + expectedCount);
        System.out.println("Начало поиска в файле...");
        long findStartTime = System.currentTimeMillis();

        FindSubstring finder = new FindSubstring(substring);
        List<Long> actual = finder.find(largeFile.toString());

        long findEndTime = System.currentTimeMillis();
        System.out.println("Поиск завершен за "
                + (findEndTime - findStartTime) + " мс.");

        Assertions.assertEquals(expectedCount, actual.size());
        System.out.println("Тест на большом файле (Рабин-Карп): Найдено "
                + actual.size() + " вхождений.");
        System.out.println("Общее время теста: "
                + (findEndTime - startTime) + " мс.");
    }

    /**
     * Метод для генерации тестовых данных.
     */
    private static void generateLargeTestFile(Path path,
                                              long approxSizeMB, String substring) throws IOException {
        System.out.println("Генерация тестового файла " + path + " (~" + approxSizeMB + "MB)...");
        String pattern = "abcdefghijklmnopqrstuvwxyz_0123456789_АБВГДЕЖЗИКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ_";
        long targetSize = approxSizeMB * 1024 * 1024;
        long written = 0;
        int patternLength = pattern.length();
        int targetLength = substring.length();

        try (var writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(path.toFile()), StandardCharsets.UTF_8))) {

            while (written < targetSize) {
                for (int i = 0; i < 1000; i++) {
                    writer.write(pattern);
                }
                writer.write(substring);
                written += (patternLength * 1000L + targetLength);
            }
        }
        System.out.println("Генерация завершена. Размер: "
                + (written / (1024 * 1024)) + "MB");
    }

    @Test
    void testMainRuns() {
        Main.main(new String[]{});
    }
}