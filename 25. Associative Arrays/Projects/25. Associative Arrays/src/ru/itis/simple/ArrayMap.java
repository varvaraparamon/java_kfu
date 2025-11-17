package ru.itis.simple;

/**
 * 24.03.2021
 * 25. Associative Arrays
 *
 * @author Sidikov Marsel (First Software Engineering Platform)
 * @version v1.0
 */
public class ArrayMap<V> implements IntegerKeyMap<V> {
    private V array[] = (V[]) new Object[10];

    @Override
    public void put(int key, V value) {
        array[key] = value;
    }

    @Override
    public V get(int key) {
        return array[key];
    }
}
