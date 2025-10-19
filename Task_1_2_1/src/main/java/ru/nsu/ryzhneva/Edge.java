package ru.nsu.ryzhneva;

import java.util.Objects;

/**
 * Представление направленного ребра графа.
 * Ребро определяется начальной и конечной вершиной.
 *
 * @param <V> Тип данных, хранящихся в вершинах.
 */
public class Edge<V> {
    public final V start;
    public final V finish;

    /**
     * Создает экземпляр ребра.
     *
     * @param start  начальная вершина.
     * @param finish конечная вершина.
     */
    public Edge(V start, V finish) {
        this.start = start;
        this.finish = finish;
    }

    /**
     * Возвращает начальную вершину.
     *
     * @return начальная вершина.
     */
    public V getStart() {
        return start;
    }

    /**
     * Возвращает конечную вершину.
     *
     * @return конечная вершина.
     */
    public V getFinish() {
        return finish;
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
        return Objects.equals(start, edge.start) && Objects.equals(finish, edge.finish);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, finish);
    }
}