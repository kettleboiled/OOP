package ru.nsu.ryzhneva;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Реализация графа через матрицу смежности.
 * Эффективна для плотных графов.
 *
 * @param <V> Тип вершин.
 */
public class AdjacencyMatrixGraph<V> implements Graph<V> {

    private static final int DEFAULT_CAPACITY = 10;

    private final Map<V, Integer> verToIndex;
    private final List<V> indexInVer;
    private int[][] matrix;
    private int countVer;

    /**
     * Создает пустой граф с начальной вместимостью.
     */
    public AdjacencyMatrixGraph() {
        verToIndex = new HashMap<>();
        indexInVer = new ArrayList<>();
        matrix = new int[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
        countVer = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addVer(V vertex) {
        if (!verToIndex.containsKey(vertex)) {
            if (countVer == matrix.length) {
                resizeMatrix();
            }
            verToIndex.put(vertex, countVer);
            indexInVer.add(vertex);
            countVer++;
        }
    }

    /**
     * Увеличивает размер внутренней матрицы при необходимости.
     */
    private void resizeMatrix() {
        int newCapacity = matrix.length * 2;
        int[][] newMatrix = new int[newCapacity][newCapacity];
        for (int i = 0; i < countVer; i++) {
            System.arraycopy(matrix[i], 0, newMatrix[i], 0, countVer);
        }
        matrix = newMatrix;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeVer(V vertex) {
        Integer indexToRemove = verToIndex.remove(vertex);
        if (indexToRemove == null) {
            return;
        }

        indexInVer.remove((int) indexToRemove);
        countVer--;

        for (int i = indexToRemove; i < countVer; i++) {
            matrix[i] = matrix[i + 1];
        }

        for (int i = 0; i < countVer; i++) {
            for (int j = indexToRemove; j < countVer; j++) {
                matrix[i][j] = matrix[i][j + 1];
            }
        }

        for (int i = indexToRemove; i < countVer; i++) {
            verToIndex.put(indexInVer.get(i), i);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addEdge(V start, V finish) {
        Integer startIndex = verToIndex.get(start);
        Integer finIndex = verToIndex.get(finish);
        if (startIndex == null || finIndex == null) {
            throw new IllegalArgumentException("The edge vertices are not found in the graph.");
        }
        matrix[startIndex][finIndex] = 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeEdge(V start, V finish) {
        Integer startIndex = verToIndex.get(start);
        Integer finIndex = verToIndex.get(finish);
        if (startIndex != null && finIndex != null) {
            matrix[startIndex][finIndex] = 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<V> getNeighbors(V vertex) {
        Integer verIndex = verToIndex.get(vertex);
        if (verIndex == null) {
            return Collections.emptySet();
        }

        Set<V> neighbors = new HashSet<>();
        for (int i = 0; i < countVer; i++) {
            if (matrix[verIndex][i] == 1) {
                neighbors.add(indexInVer.get(i));
            }
        }
        return neighbors;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<V> getVertices() {
        return Collections.unmodifiableSet(verToIndex.keySet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readFromFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length == 2) {
                    @SuppressWarnings("unchecked") V start = (V) parts[0];
                    @SuppressWarnings("unchecked") V fin = (V) parts[1];
                    addVer(start);
                    addVer(fin);
                    addEdge(start, fin);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("AdjacencyMatrixGraph: {\n");
        for (int i = 0; i < countVer; i++) {
            sb.append("  ").append(indexInVer.get(i)).append(" -> ");
            sb.append(Arrays.toString(Arrays.copyOf(matrix[i], countVer))).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AdjacencyMatrixGraph<V> that = (AdjacencyMatrixGraph<V>) o;

        if (!this.getVertices().equals(that.getVertices())) {
            return false;
        }
        for (V vertex : getVertices()) {
            if (!this.getNeighbors(vertex).equals(that.getNeighbors(vertex))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVertices());
    }
}