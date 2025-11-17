package ru.nsu.ryzhneva;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.ryzhneva.hashtable.Entry;
import ru.nsu.ryzhneva.hashtable.HashTable;

/**
 * Тесты для класса HashTable.
 */
class HashTableTest {

    private HashTable<String, Integer> table;

    @BeforeEach
    void setUp() {
        table = new HashTable<>();
    }

    @Test
    void testPutGet() {
        table.put("one", 1);
        table.put("two", 2);

        assertEquals(1, table.get("one"));
        assertEquals(2, table.get("two"));
    }

    @Test
    void testPutReturnsNullOnNew() {
        assertNull(table.put("one", 1));
    }

    @Test
    void testPutUpdatesExistingAndNonexistentKey() {
        table.put("one", 1);
        Integer prevValue = table.put("one", 100);

        assertEquals(1, prevValue, "put() should return prevValue");
        assertEquals(100, table.get("one"), "The table must contain the new value");
        assertNull(table.get("non-existent"));
    }

    @Test
    void testCheck() {
        table.put("exists", 1);
        assertTrue(table.check("exists"));
        assertFalse(table.check("non-existent"));
    }

    @Test
    void testUpdate() {
        table.put("key", 10);
        Integer prevValue = table.update("key", 20);
        assertEquals(10, prevValue, "Return previous value.");
        assertEquals(20, table.get("key"), "Value is update.");
    }

    @Test
    void testUpdateNonExistent() {
        Integer prevValue = table.update("key", 20);
        assertNull(prevValue, "It should return null if the key is not found.");
        assertFalse(table.check("key"), "The key should not be added.");
    }

    @Test
    void testRemove() {
        table.put("one", 1);
        Integer removedValue = table.remove("one");
        assertEquals(1, removedValue, "It should return the deleted value.");
        assertFalse(table.check("one"), "The key must be deleted.");
        assertNull(table.get("one"), "get() should return null after deletion..");
    }

    @Test
    void testRemoveNonExistent() {
        assertNull(table.remove("non-existent"), "Should return null.");
    }

    @Test
    void testNullKey() {
        assertNull(table.put(null, 100), "put(null) - null");
        assertTrue(table.check(null), "check(null) - true");
        assertEquals(100, table.get(null), "get(null) - 100");

        Integer oldValue = table.put(null, 400);
        assertEquals(100, oldValue, "put(null) - prevValue");
        assertEquals(400, table.get(null), "get(null) - 400");

        Integer removedValue = table.remove(null);
        assertEquals(400, removedValue, "remove(null) - 400");
        assertFalse(table.check(null), "check(null) - false");
    }

    @Test
    void testNullValue() {
        table.put("key", null);

        assertTrue(table.check("key"));
        assertNull(table.get("key"), "get() - null");
    }

    @Test
    void testResize() {
        for (int i = 0; i < 20; i++) {
            table.put("key" + i, i);
        }

        for (int i = 0; i < 20; i++) {
            assertEquals(i, table.get("key" + i), "Element " + i + " lost after resize.");
        }
    }

    @Test
    void testCollisions() {
        // "Aa".hashCode() == 2112
        // "BB".hashCode() == 2112
        String key1 = "Aa";
        String key2 = "BB";

        assertEquals(key1.hashCode(), key2.hashCode(), "The keys must have the same hash code.");
        assertNotEquals(key1, key2, "The keys do not have to be equalю.");
        table.put(key1, 100);
        table.put(key2, 200);
        assertEquals(100, table.get(key1));
        assertEquals(200, table.get(key2));
        table.remove(key1);
        assertNull(table.get(key1), "The key1 key must be deleted.");
        assertTrue(table.check(key2), "The key2 key must remain.");
        assertEquals(200, table.get(key2), "The key2 value must remain");
    }

    @Test
    void testEmptyIterator() {
        Iterator<Entry<String, Integer>> it = table.iterator();
        assertFalse(it.hasNext(), "hasNext() - false");
        assertThrows(NoSuchElementException.class, it::next,
                "NoSuchElementException");
    }

    @Test
    void testIterator() {
        table.put("one", 1);
        table.put("two", 2);
        table.put("three", 3);
        Set<String> keysFound = new HashSet<>();
        Set<Integer> valuesFound = new HashSet<>();

        for (Entry<String, Integer> entry : table) {
            keysFound.add(entry.getKey());
            valuesFound.add(entry.getValue());
        }

        assertEquals(Set.of("one", "two", "three"), keysFound);
        assertEquals(Set.of(1, 2, 3), valuesFound);
    }

    @Test
    void testConcurrentModificationPut() {
        table.put("one", 1);
        Iterator<Entry<String, Integer>> it = table.iterator();
        assertThrows(ConcurrentModificationException.class, () -> {
            it.next();
            table.put("two", 2);
            it.next();
        }, "The iterator should fall if the table is modified via put");
    }

    @Test
    void testConcurrentModificationRemove() {
        table.put("one", 1);
        table.put("two", 2);

        assertThrows(ConcurrentModificationException.class, () -> {
            for (Entry<String, Integer> entry : table) {
                if (entry.getKey().equals("one")) {
                    table.remove("two");
                }
            }
        }, "The iterator should fall if the table is modified via remove");
    }

    @Test
    void testEquals() {
        HashTable<String, Integer> table2 = new HashTable<>();
        assertEquals(table, table, "The table must be equal to itself.");
        assertEquals(table, table2, "Two empty tables should be equal.");
        assertNotEquals(table, null, "The table must not be null.");
        assertNotEquals(table, new Object(), "The table must not be other type.");

        table.put("one", 1);
        table2.put("one", 1);
        table2.put("two", 2);
        table.put("two", 2);
        assertEquals(table, table2, "Tables with the same content should be equal.");

        HashTable<String, Integer> table3 = new HashTable<>();
        table3.put("two", 2);
        table3.put("one", 1);
        assertEquals(table, table3, "The order of addition should not affect equality.");
        table3.put("three", 3);
        assertNotEquals(table, table3, "Tables of different sizes are not equal.");
        table2.update("one", 100);
        assertNotEquals(table, table2, "Tables with different contents are not equal.");
    }

    @Test
    void testToString() {
        assertEquals("{}", table.toString(), "The empty table should be {}.");

        table.put("one", 1);
        assertEquals("{one=1}", table.toString(), "One element.");

        table.put("two", 2);
        String s = table.toString();
        assertTrue(s.equals("{one=1, two=2}") || s.equals("{two=2, one=1}"),
                "Wrong format toString for a few elements");
    }

    @Test
    void testMainRuns() {
        Main.main(new String[]{});
    }
}