package ru.nsu.ryzhneva;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Реализация графа через списки смежности.
 * Эффективна по памяти для разреженных графов.
 *
 * @param <V> Тип вершин.
 */
public class AdjacencyListGraph<V> implements Graph<V> {

    private final Map<V, Set<V>> adjacencyMap = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void addVer(V vertex) {
        adjacencyMap.putIfAbsent(vertex, new LinkedHashSet<>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeVer(V vertex) {
        adjacencyMap.remove(vertex);
        adjacencyMap.values().forEach(neighbors -> neighbors.remove(vertex));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addEdge(V start, V finish) {
        if (!adjacencyMap.containsKey(start) || !adjacencyMap.containsKey(finish)) {
            throw new IllegalArgumentException("The edge vertices are not found in the graph.");
        }
        adjacencyMap.get(start).add(finish);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeEdge(V start, V finish) {
        if (adjacencyMap.containsKey(start)) {
            adjacencyMap.get(start).remove(finish);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<V> getNeighbors(V vertex) {
        return Collections.unmodifiableSet(adjacencyMap.getOrDefault(vertex, Collections.emptySet()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<V> getVertices() {
        return Collections.unmodifiableSet(adjacencyMap.keySet());
    }

    /**
     * {@inheritDoc}
     * Примечание: данный метод предполагает, что тип V является String
     * или имеет корректный конструктор из String.
     */
    @Override
    public void readFromFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length == 2) {
                    @SuppressWarnings("unchecked") V start = (V) parts[0];
                    @SuppressWarnings("unchecked") V finish = (V) parts[1];
                    addVer(start);
                    addVer(finish);
                    addEdge(start, finish);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AdjacencyListGraph: \n");
        for (Map.Entry<V, Set<V>> entry : adjacencyMap.entrySet()) {
            sb.append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");
        }
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
        AdjacencyListGraph<?> that = (AdjacencyListGraph<?>) o;
        return Objects.equals(adjacencyMap, that.adjacencyMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adjacencyMap);
    }
}