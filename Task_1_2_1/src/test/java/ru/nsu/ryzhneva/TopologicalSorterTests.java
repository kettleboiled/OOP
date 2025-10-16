package ru.nsu.ryzhneva;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тесты для проверки работы топологической сортировки.
 */
class TopologicalSorterTests {

    private TopologicalSorter sorter;
    private Graph<String> graph;

    @BeforeEach
    void setUp() {
        sorter = new TopologicalSorter();
        graph = new AdjacencyListGraph<>();
    }

    @Test
    void testValidSort() {
        graph.addVer("A");
        graph.addVer("B");
        graph.addVer("C");
        graph.addVer("D");
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("B", "D");
        graph.addEdge("C", "D");

        List<String> sorted = sorter.sort(graph);

        // Проверяем, что зависимости соблюдены
        assertTrue(sorted.indexOf("A") < sorted.indexOf("B"));
        assertTrue(sorted.indexOf("A") < sorted.indexOf("C"));
        assertTrue(sorted.indexOf("B") < sorted.indexOf("D"));
        assertTrue(sorted.indexOf("C") < sorted.indexOf("D"));
        assertEquals(4, sorted.size());
    }

    @Test
    void testSortOnGraphWithCycleThrowsException() {
        graph.addVer("A");
        graph.addVer("B");
        graph.addVer("C");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("C", "A"); // Цикл

        assertThrows(IllegalStateException.class, () -> sorter.sort(graph));
    }

    @Test
    void testSortOnEmptyGraph() {
        List<String> sorted = sorter.sort(graph);
        assertTrue(sorted.isEmpty());
    }
}