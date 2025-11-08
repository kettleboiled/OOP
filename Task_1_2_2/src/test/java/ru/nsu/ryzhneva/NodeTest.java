package ru.nsu.ryzhneva;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.hashtable.Node;

/**
 * Тесты для Node.
 */
class NodeTest {

    @Test
    void testConstructorAndGetters() {
        Node<String, Integer> node = new Node<>("testKey", 100, 9998, null);

        assertEquals("testKey", node.getKey(), "getKey() return key");
        assertEquals(100, node.getValue(), "getValue() return value");
    }

    @Test
    void testToString() {
        Node<String, Integer> node = new Node<>("key", 100, 0, null);
        assertEquals("key=100", node.toString(), "format of toString() is key=value");
    }

    @Test
    void testEquals() {
        Node<String, Integer> node1 = new Node<>("key1", 100, 1, null);
        Node<String, Integer> node1Copy = new Node<>("key1", 100, 1, null);
        Node<String, Integer> node2DiffKey = new Node<>("key2", 100, 2, null);
        Node<String, Integer> node3DiffValue = new Node<>("key1", 999, 1, null);
        assertEquals(node1, node1, "same");
        assertEquals(node1, node1Copy, "nodes with the same data are equal");
        assertNotEquals(node1, node2DiffKey, "nodes with the different data aren't equal");
        assertNotEquals(node1, node3DiffValue, "nodes with the different values aren't equal");
        assertNotEquals(node1, null, "node isn't equal null");
        assertNotEquals(node1, "key1=100", "node isn't equal to nodes of another class");
    }
}
