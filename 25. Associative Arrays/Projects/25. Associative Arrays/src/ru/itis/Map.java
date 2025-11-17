package ru.itis;

import java.util.Collection;
import java.util.Set;

/**
 * 24.03.2021
 * 25. Associative Arrays
 *
 * @author Sidikov Marsel (First Software Engineering Platform)
 * @version v1.0
 */
public interface Map<K, V> {
    /**
     * Сохранить значение по ключу.
     * Если равный ключ уже существует — заменить значение и вернуть предыдущее; иначе вернуть null.
     */
    V put(K key, V value);

    /**
     * Вернуть значение по ключу или null, если такого ключа нет.
     */
    V get(K key);

    /**
     * Проверить, есть ли в карте ключ, равный переданному.
     */
    boolean containsKey(K key);

    /**
     * Проверить, встречается ли переданное значение среди значений.
     */
    boolean containsValue(V value);

    /**
     * Удалить запись по ключу.
     * Вернуть удалённое значение или null, если записи не было.
     */
    V remove(K key);

    /**
     * Удалить запись только если одновременно:
     * — ключ равен переданному ключу,
     * — значение равно переданному значению.
     * Вернуть true, если удаление произошло.
     */
    boolean remove(K key, V value);

    /**
     * Сохранить значение только если равного ключа ещё нет.
     * Вернуть предыдущее значение (если было) или null.
     */
    V putIfAbsent(K key, V value);

    /**
     * Заменить значение по существующему ключу.
     * Вернуть предыдущее значение или null, если ключ отсутствует.
     */
    V replace(K key, V value);

    /**
     * Заменить значение только если текущее значение равно oldValue.
     * Вернуть true, если замена произошла.
     */
    boolean replace(K key, V oldValue, V newValue);

    /**
     * Количество пар ключ-значение.
     */
    int size();

    /**
     * Является ли карта пустой.
     */
    boolean isEmpty();

    /**
     * Удалить все пары ключ-значение.
     */
    void clear();

    /**
     * Набор уникальных ключей.
     */
    Set<K> KeySet();

    /**
     * Коллекция всех значений (допускаются повторы).
     */
    Collection<V> values();

    /**
     * Набор всех пар ключ-значение.
     */
    Set<Entry<K, V>> entrySet();

    /**
     * Пара ключ-значение.
     */
    interface Entry<K, V> {
        K getKey();
        V getValue();

        /**
         * Установить новое значение и вернуть предыдущее.
         */
        V setValue(V newValue);
    }
}
