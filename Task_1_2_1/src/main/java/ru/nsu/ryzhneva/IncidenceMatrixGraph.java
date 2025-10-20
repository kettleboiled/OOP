package ru.nsu.ryzhneva;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Реализация графа через матрицу инцидентности.
 * Используется в основном в теоретических целях.
 *
 * @param <V> Тип вершин.
 */
public class IncidenceMatrixGraph<V> implements Graph<V> {

    private static final int DEFAULT_CAPACITY = 10;

    private final Map<V, Integer> verToIndex; // получение индекса вершины а матрице
    private final List<V> indexToVer; // получение вершины по индексу матрицы
    private final Map<Edge<V>, Integer> edgeToIndex; // получение индекса столбца определнного ребра в матрице
    private final List<Edge<V>> indexToEdge; // получение ребра по индексу столбца матрицы
    private int[][] matrix;
    private int countVer;
    private int countEdge;

    /**
     * Создает пустой граф с начальной вместимостью.
     */
    public IncidenceMatrixGraph() {
        verToIndex = new HashMap<>();
        indexToVer = new ArrayList<>();
        edgeToIndex = new HashMap<>();
        indexToEdge = new ArrayList<>();
        matrix = new int[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
        countVer = 0;
        countEdge = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addVer(V vertex) {
        if (!verToIndex.containsKey(vertex)) {
            if (countVer == matrix.length) {
                resizeMatrix(matrix.length * 2, matrix.length > 0 ? matrix[0].length : 10);
            }
            verToIndex.put(vertex, countVer);
            indexToVer.add(vertex);
            countVer++;
        }
    }

    /**
     * Увеличивает размер внутренней матрицы при необходимости.
     *
     * @param newVertexCap новая вместимость по вершинам.
     * @param newEdgeCap   новая вместимость по рёбрам.
     */
    private void resizeMatrix(int newVertexCap, int newEdgeCap) {
        int[][] newMatrix = new int[newVertexCap][newEdgeCap];
        for (int i = 0; i < countVer; i++) {
            System.arraycopy(matrix[i], 0, newMatrix[i], 0, countEdge);
        }
        matrix = newMatrix;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addEdge(V start, V finish) {
        if (!verToIndex.containsKey(start) || !verToIndex.containsKey(finish)) {
            throw new IllegalArgumentException("The edge vertices are not found in the graph.");
        }
        Edge<V> newEdge = new Edge<>(start, finish);
        if (edgeToIndex.containsKey(newEdge)) {
            return;
        }
        if (countEdge == (matrix.length > 0 ? matrix[0].length : 0)) {
            resizeMatrix(matrix.length, matrix.length > 0 ? matrix[0].length * 2 : 10);
        }
        int sourceIndex = verToIndex.get(start);
        int destIndex = verToIndex.get(finish);
        matrix[sourceIndex][countEdge] = 1;
        matrix[destIndex][countEdge] = -1;
        edgeToIndex.put(newEdge, countEdge);
        indexToEdge.add(newEdge);
        countEdge++;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<V> getNeighbors(V vertex) {
        Integer vertexIndex = verToIndex.get(vertex);
        if (vertexIndex == null) {
            return Collections.emptySet();
        }
        Set<V> neighbors = new LinkedHashSet<>();
        for (int j = 0; j < countEdge; j++) {
            if (matrix[vertexIndex][j] == 1) {
                Edge<V> edge = indexToEdge.get(j);
                neighbors.add(edge.finish);
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
     * Реализация O(E + V): сначала удаляются все инцидентные рёбра,
     * затем сама вершина.
     */
    @Override
    public void removeVer(V vertex) {
        Integer verIndexToRemove = verToIndex.get(vertex);
        if (verIndexToRemove == null) {
            return;
        }

        List<Edge<V>> edgesToRemove = new ArrayList<>();
        for (Edge<V> edge : indexToEdge) {
            if (edge.start.equals(vertex) || edge.finish.equals(vertex)) {
                edgesToRemove.add(edge);
            }
        }
        for (Edge<V> edge : edgesToRemove) {
            removeEdge(edge.start, edge.finish);
        }

        verToIndex.remove(vertex);
        indexToVer.remove((int) verIndexToRemove);

        for (int i = verIndexToRemove; i < countVer - 1; i++) {
            matrix[i] = matrix[i + 1];
        }
        countVer--;

        for (int i = verIndexToRemove; i < countVer; i++) {
            verToIndex.put(indexToVer.get(i), i);
        }
    }

    /**
     * {@inheritDoc}
     * Реализация O(V + E): столбец удаляемого ребра затирается
     * сдвигом последующих столбцов.
     */
    @Override
    public void removeEdge(V start, V finish) {
        Edge<V> edge = new Edge<>(start, finish);
        Integer edgeIndexToRemove = edgeToIndex.get(edge);

        if (edgeIndexToRemove == null) {
            return;
        }

        edgeToIndex.remove(edge);
        indexToEdge.remove((int) edgeIndexToRemove);

        for (int i = 0; i < countVer; i++) {
            for (int j = edgeIndexToRemove; j < countEdge - 1; j++) {
                matrix[i][j] = matrix[i][j + 1];
            }
        }
        countEdge--;

        for (int i = edgeIndexToRemove; i < countEdge; i++) {
            edgeToIndex.put(indexToEdge.get(i), i);
        }
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
        StringBuilder sb = new StringBuilder("IncidenceMatrixGraph: {\n");
        for (int i = 0; i < countVer; i++) {
            sb.append("  ").append(indexToVer.get(i)).append(" -> ");
            sb.append(Arrays.toString(Arrays.copyOf(matrix[i], countEdge))).append("\n");
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
        IncidenceMatrixGraph<V> that = (IncidenceMatrixGraph<V>) o;

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
        return Objects.hash(verToIndex.keySet(), edgeToIndex.keySet());
    }
}