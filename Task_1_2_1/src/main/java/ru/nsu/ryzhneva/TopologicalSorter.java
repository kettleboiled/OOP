package ru.nsu.ryzhneva;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Утилитный класс для выполнения топологической сортировки графа.
 */
public class TopologicalSorter {

    /**
     * Выполняет топологическую сортировку вершин графа, используя алгоритм Кана.
     *
     * @param graph граф для сортировки.
     * @param <V> тип данных вершин.
     * @return список вершин в топологическом порядке.
     * @throws IllegalStateException если граф содержит цикл.
     */
    public <V> List<V> sort(Graph<V> graph) {
        Map<V, Integer> inDegree = new HashMap<>();

        for (V vertex : graph.getVertices()) {
            inDegree.put(vertex, 0);
        }

        for (V vertex : graph.getVertices()) {
            for (V neighbor : graph.getNeighbors(vertex)) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }
        }

        Queue<V> queue = new LinkedList<>();
        for (Map.Entry<V, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        List<V> sortRes = new ArrayList<>();
        while (!queue.isEmpty()) {
            V current = queue.poll();
            sortRes.add(current);

            for (V neighbor : graph.getNeighbors(current)) {
                int newInDegree = inDegree.get(neighbor) - 1;
                inDegree.put(neighbor, newInDegree);
                if (newInDegree == 0) {
                    queue.add(neighbor);
                }
            }
        }

        if (sortRes.size() != graph.getVertices().size()) {
            throw new IllegalStateException("The graph contains a cycle.");
        }

        return sortRes;
    }
}