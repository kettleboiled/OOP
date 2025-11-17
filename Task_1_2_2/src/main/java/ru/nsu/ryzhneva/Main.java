package ru.nsu.ryzhneva;

import ru.nsu.ryzhneva.hashtable.Entry;
import ru.nsu.ryzhneva.hashtable.HashTable;

import java.util.ConcurrentModificationException;

/**
 * Главный класс.
 */
public class Main {
    /**
     * Точка входа.
     */
    public static void main(String[] args) {
        HashTable<String, Number> hashTable = new HashTable<>();

        hashTable.put("one", 1);
        hashTable.update("one", 1.0);
        System.out.println(hashTable.get("one"));

        hashTable.put("nine", 9);
        hashTable.put("three", 3);

        System.out.println(hashTable);
        hashTable.remove("nine");
        System.out.println(hashTable);

        System.out.println("Check 'one': " + hashTable.check("one"));
        System.out.println("Check 'nine': " + hashTable.check("nine"));

        System.out.println("Iteration:");
        for (Entry<String, Number> entry : hashTable) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
        }

        try {
            for (Entry<String, Number> entry : hashTable) {
                hashTable.put("four", 4);
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}