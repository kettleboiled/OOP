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

    private final Map<V, Set<V>> adjacencyList = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void addVer(V ver) {
        adjacencyList.putIfAbsent(ver, new LinkedHashSet<>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeVer(V ver) {
        adjacencyList.remove(ver);
        adjacencyList.values().forEach(neighbors -> neighbors.remove(ver));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addEdge(V start, V fin) {
        if (!adjacencyList.containsKey(start) || !adjacencyList.containsKey(fin)) {
            throw new IllegalArgumentException("The edge vertices are not found in the graph.");
        }
        adjacencyList.get(start).add(fin);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeEdge(V start, V fin) {
        if (adjacencyList.containsKey(start)) {
            adjacencyList.get(start).remove(fin);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<V> getNeighbors(V ver) {
        return Collections.unmodifiableSet(adjacencyList.getOrDefault(ver, Collections.emptySet()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<V> getVertices() {
        return Collections.unmodifiableSet(adjacencyList.keySet());
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
        StringBuilder sb = new StringBuilder();
        sb.append("AdjacencyListGraph: \n");
        for (Map.Entry<V, Set<V>> entry : adjacencyList.entrySet()) {
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
        return Objects.equals(adjacencyList, that.adjacencyList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adjacencyList);
    }
}