package ru.nsu.ryzhneva;

import java.io.IOException;
import java.util.Set;

/**
 * Интерфейс, представляющий граф.
 *
 * @param <V> Тип данных, хранящийся в вершинах.
 */
interface Graph<V> {

    /**
     * Добавляет вершину в граф.
     *
     * @param ver Вершина.
     */
    void addVer(V ver);

    /**
     * Удаляет вершину и все связанные с ней ребра из графа.
     *
     * @param ver Вершина.
     */
    void removeVer(V ver);

    /**
     * Добавляет ориентированное ребро в граф.
     *
     * @param start Начальная вершина.
     * @param fin Конечная вершина.
     */
    void addEdge(V start, V fin);

    /**
     * Удаляет ребро.
     *
     * @param start Начальная вершина.
     * @param fin Конечная вершина.
     */
    void removeEdge(V start, V fin);

    /**
     * Возващает всех соседей вершины.
     *
     * @param ver Вершина.
     * @return Множество соседей.
     */
    Set<V> getNeighbors(V ver);

    /**
     * Считывает граф из файла.
     * Каждая строка представляет собой ребро "start fin".
     * Вершины, не имеющие ребер, должны быть добавлены отдельно.
     *
     * @param filePath Путь до файла.
     * @throws IOException если возникает ошибка чтения файла.
     */
    void readFromFile(String filePath) throws IOException;

    /**
     * Возвращает множество вершин графа.
     *
     * @return Множество вершин графа.
     */
    Set<V> getVertices();
}
