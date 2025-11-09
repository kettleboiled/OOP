package ru.nsu.ryzhneva;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Утилитарный класс для поиска вхождений подстроки в файле.
 */
public class FindSubstring {
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    /**
     * Находит все вхождения подстроки в файле, используя буферизованное чтение.
     *
     * @param fileName  Имя файла.
     * @param substring Подстрока.
     * @return Список индексов начала подсроки.
     */
    public static List<Long> find(String fileName, String substring) throws IOException {
        List<Long> indices = new ArrayList<>();
        int lenSubstring = substring.length();

        if (lenSubstring == 0) {
            return indices;
        }

        char[] buffer = new char[DEFAULT_BUFFER_SIZE];
        long globalCharIndex = 0;
        String tail = "";

        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(fileName), StandardCharsets.UTF_8))) {

            int readChars;

            while ((readChars = bufferedReader.read(buffer)) != -1) {
                String searchArea = tail + new String(buffer, 0, readChars);
                int localIndex = -1;

                int tailLen = tail.length();
                while ((localIndex =
                        searchArea.indexOf(substring, localIndex + 1)) != -1) {
                    long globalIndex = globalCharIndex - tailLen + localIndex;
                    indices.add(globalIndex);
                }

                globalCharIndex += readChars;

                int rawStart = searchArea.length() - (lenSubstring - 1);
                int startTail = (rawStart < 0) ? 0 : rawStart;

                tail = searchArea.substring(startTail);
            }
        }
        return indices;
    }
}