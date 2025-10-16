package ru.nsu.ryzhneva;

import java.util.Set;

/**
 * Главный класс для демонстрации работы с реализациями графа.
 */
public final class Main {

    /**
     * Приватный конструктор для предотвращения инстанцирования утилитного класса.
     */
    private Main() {
    }

    /**
     * Точка входа в программу.
     *
     * @param args аргументы командной строки (не используются).
     */
    public static void main(String[] args) {
        System.out.println("### 1. Create graph ###");
        Graph<String> graph = new IncidenceMatrixGraph<>();
        System.out.println("Empty graph is created.\n");

        System.out.println("### 2. Add in graph ###");
        graph.addVer("A");
        graph.addVer("B");
        graph.addVer("C");
        graph.addVer("D");

        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("B", "C");
        graph.addEdge("C", "D");

        System.out.println("Vertices added: A, B, C, D and edges A->B, A->C, B->C, C->D.");
        System.out.println(graph);

        System.out.println("### 3. Info from the graph ###");
        Set<String> vertices = graph.getVertices();
        System.out.println("All vertices in the graph: " + vertices);

        Set<String> neighborsOfA = graph.getNeighbors("A");
        System.out.println("Neighbors of A: " + neighborsOfA);

        Set<String> neighborsOfC = graph.getNeighbors("C");
        System.out.println("Neighbors of C: " + neighborsOfC + "\n");

        System.out.println("### 4. Remove edge A -> C ###");
        graph.removeEdge("A", "C");
        System.out.println("Graph with removed edge:");
        System.out.println(graph);

        System.out.println("### 5. Remove B ###");
        System.out.println("Removing vertex B should also remove edge A -> B.");
        graph.removeVer("B");
        System.out.println("Graph with removed vertex:");
        System.out.println(graph);
    }
}