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
        List<Long> actual = FindSubstring.find(testFile.toString(), "бра");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testEmptySubstring() throws IOException {
        Path testFile = tempDir.resolve("empty_substring.txt");
        Files.writeString(testFile, "abcde", StandardCharsets.UTF_8);
        List<Long> emptyList = List.of();
        List<Long> actual = FindSubstring.find(testFile.toString(), "");
        Assertions.assertEquals(emptyList, actual);
    }

    @Test
    void testFileNotFound() {
        String nonExistentFile = "file.txt";
        Assertions.assertThrows(IOException.class, () -> {
            FindSubstring.find(nonExistentFile, "abc");
        });
    }

    @Test
    void testBufferJunction() throws IOException {
        String substring = "abcdefi";
        int lenSubstring = substring.length();
        int sizeBuff = 8192;
        int tailStart = sizeBuff - (lenSubstring / 2);

        StringBuilder sb = new StringBuilder(sizeBuff + 100);
        for (int i = 0; i < tailStart; i++) {
            sb.append('a');
        }
        sb.append(substring);
        for (int i = 0; i < 100; i++) {
            sb.append('b');
        }
        Path testFile = tempDir.resolve("data.txt");
        Files.writeString(testFile, sb.toString(), StandardCharsets.UTF_8);
        List<Long> expected = List.of((long) tailStart);
        List<Long> actual = FindSubstring.find(testFile.toString(), substring);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testLargeFile() throws IOException {
        Path largeFile = tempDir.resolve("file.txt");
        String substring = "STRING";
        long approxSizeMB = 10;
        generateLargeTestFile(largeFile, approxSizeMB, substring);

        String pattern = "abcdefghijklmnopqrstuvwxyz_0123456789_АБВГДЕЖЗИКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ_";
        long targetSize = approxSizeMB * 1024 * 1024;
        long chunkSize = (pattern.length() * 1000L + substring.length());

        long written = 0;
        long expectedCount = 0;
        while(written < targetSize) {
            written += chunkSize;
            expectedCount++;
        }
        List<Long> actual = FindSubstring.find(largeFile.toString(), substring);

        Assertions.assertEquals(expectedCount, actual.size());
        System.out.println("Тест на большом файле: Найдено " + actual.size() + " вхождений.");
    }

    /**
     * Метод для генерации тестовых данных.
     *
     * @param path         Путь к файлу, который будет создан.
     * @param approxSizeMB Приблизительный размер файла в мегабайтах.
     * @param substring  Искомая подстрока, которая будет вставлена в файл.
     * @throws IOException Ошибка записи.
     */
    private static void generateLargeTestFile(Path path, long approxSizeMB, String substring) throws IOException {
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
        System.out.println("Генерация завершена. Размер: " + (written / (1024 * 1024)) + "MB");
    }

    @Test
    void testMainRuns() {
        Main.main(new String[]{});
    }
}