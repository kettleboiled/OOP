package ru.nsu.ryzhneva.hashtable;

/**
 * Интерфейс для представления пары ключ-значение.
 *
 * @param <K> Тип ключа.
 * @param <V> Тип значения.
 */
public interface Entry<K, V> {
    K getKey();
    V getValue();
}