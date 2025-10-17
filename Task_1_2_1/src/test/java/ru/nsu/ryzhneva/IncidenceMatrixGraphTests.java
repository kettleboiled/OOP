package ru.nsu.ryzhneva;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Тесты для матрицы инцедентности.
 */
class IncidenceMatrixGraphTests {

    private Graph<String> graph;

    @BeforeEach
    void setUp() {
        graph = new IncidenceMatrixGraph<>();
    }

    @Test
    void testAddVerAndGetVertices() {
        graph.addVer("A");
        graph.addVer("B");
        assertEquals(2, graph.getVertices().size());
        assertTrue(graph.getVertices().containsAll(Set.of("A", "B")));
    }

    @Test
    void testAddEdgeAndGetNeighbors() {
        graph.addVer("A");
        graph.addVer("B");
        graph.addEdge("A", "B");
        assertTrue(graph.getNeighbors("A").contains("B"));
        assertTrue(graph.getNeighbors("B").isEmpty());
    }

    @Test
    void testRemoveEdge() {
        graph.addVer("A");
        graph.addVer("B");
        graph.addEdge("A", "B");
        graph.removeEdge("A", "B");
        assertFalse(graph.getNeighbors("A").contains("B"));
    }

    @Test
    void testRemoveVer() {
        graph.addVer("A");
        graph.addVer("B");
        graph.addVer("C");
        graph.addEdge("A", "B");
        graph.addEdge("C", "A");

        graph.removeVer("A");

        assertFalse(graph.getVertices().contains("A"));
        assertTrue(graph.getNeighbors("C").isEmpty());
        assertEquals(2, graph.getVertices().size());
    }

    @Test
    void testEqualsAndHashCodeContract() {
        Graph<String> graph1 = new IncidenceMatrixGraph<>();
        graph1.addVer("A");
        graph1.addVer("B");
        graph1.addEdge("A", "B");

        Graph<String> graph2 = new IncidenceMatrixGraph<>();
        graph2.addVer("A");
        graph2.addVer("B");
        graph2.addEdge("A", "B");

        Graph<String> graph3 = new IncidenceMatrixGraph<>();
        graph3.addVer("A");
        graph3.addVer("C");
        graph3.addEdge("A", "C");

        assertEquals(graph1, graph2,
                "Графы с одинаковой структурой должны быть равны");
        assertNotEquals(graph1, graph3,
                "Графы с разной структурой не должны быть равны");
        assertNotEquals(graph1, null,
                "Граф не должен быть равен null");
        assertNotEquals(graph1, new Object(),
                "Граф не должен быть равен объекту другого класса");

        assertEquals(graph1.hashCode(), graph2.hashCode(),
                "Хеш-коды равных графов должны совпадать");
    }

    @Test
    void testHashCode_isConsistentWithEquals() {
        Graph<String> graph1 = new IncidenceMatrixGraph<>();
        graph1.addVer("A");
        graph1.addVer("B");
        graph1.addEdge("A", "B");

        Graph<String> graph2 = new IncidenceMatrixGraph<>();
        graph2.addVer("A");
        graph2.addVer("B");
        graph2.addEdge("B", "A");
        assertNotEquals(graph1, graph2);
        assertNotEquals(graph1.hashCode(), graph2.hashCode(),
                "Хеш-коды разных по структуре графов не должны совпадать");
    }

    @Test
    void testToString() {
        graph.addVer("A");
        graph.addVer("B");
        graph.addEdge("A", "B");

        String result = graph.toString();

        assertTrue(result.contains("A -> [1]"));
        assertTrue(result.contains("B -> [-1]"));
    }

    @Test
    void testReadFromFile(@TempDir Path tempDir) throws IOException {
        Path filePath = tempDir.resolve("graph.txt");
        List<String> lines = List.of(
                "A B",
                "B C",
                "C A"
        );
        Files.write(filePath, lines);
        graph.readFromFile(filePath.toString());

        assertTrue(graph.getVertices().containsAll(Set.of("A", "B", "C")));
        assertEquals(3, graph.getVertices().size());
        assertTrue(graph.getNeighbors("A").contains("B"));
        assertTrue(graph.getNeighbors("B").contains("C"));
        assertTrue(graph.getNeighbors("C").contains("A"));
    }

    @Test
    void testReadFromFile_whenFileNotExists_throwsException() {
        String nonExistentFilePath = "path/to/non/existent/file.txt";
        assertThrows(IOException.class, () -> {
            graph.readFromFile(nonExistentFilePath);
        });
    }
}