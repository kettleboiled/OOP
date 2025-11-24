package ru.nsu.ryzhneva;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Класс для поиска вхождений подстроки в файле.
 */
public class FindSubstring {

    private static final long d = 257;
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    private static final long PRIME_NUMBER = 1_000_000_007;

    private final char[] substring;
    private final int substringLen;
    private final long substringHash;
    private final long highestPower;
    private final boolean isEmptySubstring;
    private char[] windowArr;
    private int windowPos = 0;

    /**
     * Конструктор класса с расчетом хэша
     * искомой подстроки.
     *
     * @param substring Искомая подстрока.
     */
    public FindSubstring(String substring) {
        if (substring == null) {
            throw new IllegalArgumentException("Substring cannot be null");
        }
        if (substring.isEmpty()) {
            this.isEmptySubstring = true;
            this.substring = new char[0];
            this.substringLen = 0;
            this.substringHash = 0;
            this.highestPower = 0;
            this.windowArr = new char[0];
            return;
        }

        this.isEmptySubstring = false;
        this.substring = substring.toCharArray();
        this.substringLen = substring.length();
        this.windowArr = new char[substringLen];

        this.substringHash = calculateHash(this.substring, this.substringLen);

        long power = 1;
        for (int i = 0; i < this.substringLen - 1; i++) {
            power = (power * d) % PRIME_NUMBER;
        }
        this.highestPower = power;
    }

    /**
     * Поиск подстроки в указанном файле.
     *
     * @param fileName Имя файла для поиска.
     * @return Список Long индексов начала каждого вхождения.
     * @throws IOException В случае ошибки чтения файла.
     */
    public List<Long> find(String fileName) throws IOException {
        List<Long> indices = new ArrayList<>();

        if (this.isEmptySubstring) {
            return indices;
        }

        char[] buffer = new char[DEFAULT_BUFFER_SIZE];
        long currentHash = 0;
        long globalIndex = 0;
        windowPos = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(fileName), StandardCharsets.UTF_8))) {
            int charRead;
            while ((charRead = reader.read(buffer)) != -1 ) {
                for (int i = 0; i < charRead; i++) {
                    char newSym = buffer[i];

                    if (globalIndex >= substringLen) {
                        char oldSym = windowArr[windowPos];
                        currentHash = rollHash(currentHash, oldSym, newSym);
                    } else {
                        currentHash = (d * currentHash + newSym) % PRIME_NUMBER;
                    }

                    windowArr[windowPos] = newSym;
                    windowPos++;
                    if (windowPos == substringLen) {
                        windowPos = 0;
                    }

                    globalIndex++;
                    if (globalIndex >= substringLen && currentHash == substringHash) {
                        if (checkEqualityPrimitive(windowArr, windowPos, substring)) {
                            indices.add(globalIndex - substringLen);
                        }
                    }
                }
            }
            return indices;
        }
    }

    /**
     * Посимвольная проверка содержимого кольцевого буфера и подстроки.
     * Работает без создания новых объектов.
     *
     * @param windowArr Массив кольцевого буфера
     * @param startPos  Индекс в массиве, который является логическим началом окна
     * @param substring Искомый паттерн
     */
    private boolean checkEqualityPrimitive(char[] windowArr, int startPos, char[] substring) {
        int len = substring.length;
        for (int i = 0; i < len; i++) {
            if (windowArr[(startPos + i) % len] != substring[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Первичный расчет хэша (O(M)).
     */
    private long calculateHash(char[] substring, int length) {
        long hash = 0;
        for (int i = 0; i < length; i++) {
            hash = (d * hash + substring[i]) % PRIME_NUMBER;
        }
        return hash;
    }

    /**
     * "Плавающий" хэш (O(1)).
     */
    private long rollHash(long oldHash, char oldSym, char newSym) {
        long hash = oldHash;

        long oldWeight = (oldSym * this.highestPower) % PRIME_NUMBER;
        hash = (hash - oldWeight + PRIME_NUMBER) % PRIME_NUMBER;

        hash = (d * hash) % PRIME_NUMBER;
        hash = (hash + newSym) % PRIME_NUMBER;

        return hash;
    }
}