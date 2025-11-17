package ru.itis.primitive;

import ru.itis.Map;

import java.util.Map.Entry;

/**
 * 24.03.2021
 * 25. Associative Arrays
 *
 * @author Sidikov Marsel (First Software Engineering Platform)
 * @version v1.0
 */
public class PrimitiveMap<K, V> implements Map<K, V> {

    private static final int MAX_SIZE = 10;

    private static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private Entry<K, V> entries[];

    private int count;

    public PrimitiveMap() {
        this.entries = new Entry[MAX_SIZE];
    }

    @Override
    public void put(K key, V value) {
        for (int i = 0; i < count; i++) {
            // если у нас уже есть такой ключ
            if (entries[i].key.equals(key)) {
                // заменяем значение
                entries[i].value = value;
                return;
            }
        }
        this.entries[count] = new Entry<>(key, value);
        count++;
    }

    @Override
    public V get(K key) {
        for (int i = 0; i < count; i++) {
            // находим совпадающий ключ
            if (entries[i].key.equals(key)) {
                // возвращаем значение
                return entries[i].value;
            }
        }
        return null;
    }
}
