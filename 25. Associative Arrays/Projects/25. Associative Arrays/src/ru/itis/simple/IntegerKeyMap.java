package ru.itis.simple;

/**
 * 24.03.2021
 * 25. Associative Arrays
 *
 * @author Sidikov Marsel (First Software Engineering Platform)
 * @version v1.0
 */
public interface IntegerKeyMap<V> {
    void put(int key, V value);
    V get(int key);
}
