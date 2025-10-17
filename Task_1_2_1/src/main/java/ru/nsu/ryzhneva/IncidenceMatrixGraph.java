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

    private final Map<V, Integer> verToIndex;
    private final List<V> indexInVer;
    private final Map<Edge<V>, Integer> edgeToIndex;
    private final List<Edge<V>> indexToEdge;
    private int[][] matrix;
    private int countVer;
    private int countEdge;

    /**
     * Внутренний класс для представления ребра.
     * Необходим, так как столбцы матрицы соответствуют рёбрам.
     *
     * @param <V> Тип вершин.
     */
    private static class Edge<V> {
        final V start;
        final V fin;

        Edge(V start, V fin) {
            this.start = start;
            this.fin = fin;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Edge<?> edge = (Edge<?>) o;
            return start.equals(edge.start) && fin.equals(edge.fin);
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, fin);
        }
    }

    /**
     * Создает пустой граф с начальной вместимостью.
     */
    public IncidenceMatrixGraph() {
        verToIndex = new HashMap<>();
        indexInVer = new ArrayList<>();
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
    public void addVer(V ver) {
        if (!verToIndex.containsKey(ver)) {
            if (countVer == matrix.length) {
                resizeMatrix(matrix.length * 2, matrix.length > 0 ? matrix[0].length : 10);
            }
            verToIndex.put(ver, countVer);
            indexInVer.add(ver);
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
    public void addEdge(V source, V destination) {
        if (!verToIndex.containsKey(source) || !verToIndex.containsKey(destination)) {
            throw new IllegalArgumentException("The edge vertices are not found in the graph.");
        }
        Edge<V> newEdge = new Edge<>(source, destination);
        if (edgeToIndex.containsKey(newEdge)) {
            return;
        }
        if (countEdge == (matrix.length > 0 ? matrix[0].length : 0)) {
            resizeMatrix(matrix.length, matrix.length > 0 ? matrix[0].length * 2 : 10);
        }
        int sourceIndex = verToIndex.get(source);
        int destIndex = verToIndex.get(destination);
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
                neighbors.add(edge.fin);
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
    public void removeVer(V ver) {
        Integer verIndexToRemove = verToIndex.get(ver);
        if (verIndexToRemove == null) {
            return;
        }

        List<Edge<V>> edgesToRemove = new ArrayList<>();
        for (Edge<V> edge : indexToEdge) {
            if (edge.start.equals(ver) || edge.fin.equals(ver)) {
                edgesToRemove.add(edge);
            }
        }
        for (Edge<V> edge : edgesToRemove) {
            removeEdge(edge.start, edge.fin);
        }

        verToIndex.remove(ver);
        indexInVer.remove(ver);

        for (int i = verIndexToRemove; i < countVer - 1; i++) {
            matrix[i] = matrix[i + 1];
        }
        countVer--;

        for (int i = verIndexToRemove; i < countVer; i++) {
            verToIndex.put(indexInVer.get(i), i);
        }
    }

    /**
     * {@inheritDoc}
     * Реализация O(V + E): столбец удаляемого ребра затирается
     * сдвигом последующих столбцов.
     */
    @Override
    public void removeEdge(V start, V fin) {
        Edge<V> edge = new Edge<>(start, fin);
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
            sb.append("  ").append(indexInVer.get(i)).append(" -> ");
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