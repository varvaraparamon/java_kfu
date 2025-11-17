package ru.itis.result;

import ru.itis.Map;

import java.util.Map.Entry;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * 24.03.2021
 * 25. Associative Arrays
 *
 * @author Sidikov Marsel (First Software Engineering Platform)
 * @version v1.0
 */
public class HashMap<K, V> implements Map<K, V> {
    private final static int DEFAULT_SIZE = 10;

    private static class EntryNode<K, V> implements Entry<K, V> {
        K key;
        V value;
        EntryNode<K, V> next;

        EntryNode(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V newValue) {
            V old = value;
            value = newValue;
            return old;
        }

        @Override 
        public String toString() {
            return key.toString() + " " + value.toString();
        }
    }

    private EntryNode<K, V> table[];

    public HashMap() {
        table = new EntryNode[DEFAULT_SIZE];
    }

    @Override
    public V put(K key, V value) {
        // получаем хеш-код ключа, и обрезаем его до размеров массива, получаем индекс
        int index = Math.abs(key.hashCode() % DEFAULT_SIZE);
        // если в данном bucket-е ничего нет
        if (table[index] == null) {
            // кладем туда узел (он первый)
            table[index] = new EntryNode<>(key, value);
        } else {
            // необходимо пройти по всему bucket-у и найти ключ
            EntryNode<K, V> current = table[index]; // запоминаем первый элемент bucket-а
            // идем по всему текущему списку
            while (current != null) {
                // чтобы не тратить время на сравнение по equals, проверим hashCode - он быстрее

                // возможные ситуации -> если хеш-коды равны, не факт, что там один и тот же ключ
                // если хеш-коды разные, то объекты точно разные
                if (current.key.hashCode() != key.hashCode()) {
                    // переходим сразу к следующему узлу
                    current = current.next;
                } else {
                    // если совпали по хеш-коду, нужно проверить, а не тот ли ключ, который нужен?
                    // уточняем по equals
                    if (current.key.equals(key)) {
                        // если этот тот же самый ключ - делаем замену
                        V old_value = current.value;
                        current.value = value;
                        // если ключ заменили - дальше что-то делать нет смысла
                        return old_value;
                    } else {
                        // если ключ не тот - идем дальше
                        current = current.next;
                    }
                }
            }
            // если мы оказались здесь - значит ключ не нашли, то нужно добавить его в bucket
            EntryNode<K,V> newNode = new EntryNode<>(key, value);
            // у нового узла - следующий после него -> первый узел bucket-а
            newNode.next = table[index];
            // теперь новый узел - первый в bucket
            table[index] = newNode;
        }
        return null;
    }

    @Override
    public V get(K key) {
        int index = Math.abs(key.hashCode() % DEFAULT_SIZE);
        EntryNode<K, V> current = table[index];
        while (current != null) {
            if (current.key.hashCode() == key.hashCode() && current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        int index = Math.abs(key.hashCode() % DEFAULT_SIZE);
        EntryNode<K, V> current = table[index];
        while (current != null){
            if (current.key.equals(key)){
                return true;
            }
            current = current.next;
        }
        return false;
    }


    @Override
    public boolean containsValue(V value) {
        for (int i = 0; i < DEFAULT_SIZE; i++){
            if (table[i] == null) {
                continue;
            }
            EntryNode<K, V> current = table[i];
            while (current != null){
                if (current.value.equals(value)){
                    return true;
                }
                current = current.next;
            }
        }

        return false;
    }

    @Override
    public Set<K> KeySet(){
        Set<K> res = new HashSet<>();
        for (int i = 0; i < DEFAULT_SIZE; i++){
            if (table[i] == null) {
                continue;
            }
            EntryNode<K, V> current = table[i];
            while (current != null){
                res.add(current.key);
                current = current.next;
            }
        }

        return res;
    }

    @Override
    public V remove(K key){
        int index = Math.abs(key.hashCode() % DEFAULT_SIZE);
        EntryNode<K, V> current = table[index];
        if ((current != null) & current.key.equals(key)){
            V deleted_value = current.value;
            table[index] = current.next;
            return deleted_value;
        }
        while (current.next != null){
            if (current.next.key.equals(key)){
                V deleted_value = current.next.value;
                current.next = current.next.next;
                return deleted_value;
                
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public boolean remove(K key, V value) {
        int index = Math.abs(key.hashCode() % DEFAULT_SIZE);
        EntryNode<K, V> current = table[index];
        if ((current != null) & current.key.equals(key) & current.value.equals(value)){
            table[index] = current.next;
            return true;
        }
        while (current.next != null){
            if (current.next.key.equals(key) & current.next.value.equals(value)){
                current.next = current.next.next;
                return true;
                
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public V putIfAbsent(K key, V value) {
       int index = Math.abs(key.hashCode() % DEFAULT_SIZE);
        if (table[index] == null) {
            table[index] = new EntryNode<>(key, value);

        } else {
            EntryNode<K, V> current = table[index]; 
            while (current != null) {
                if (current.key.hashCode() != key.hashCode()) {
                    current = current.next;

                } else {
                    if (current.key.equals(key)) {
                        return current.value;

                    } else {
                        current = current.next;
                    }
                }
            }

            EntryNode<K,V> newNode = new EntryNode<>(key, value);

            newNode.next = table[index];

            table[index] = newNode;
        }
        return null;
    }

    @Override
    public V replace(K key, V value) {
       int index = Math.abs(key.hashCode() % DEFAULT_SIZE);
        if (table[index] == null) {
            return null;

        } else {
            EntryNode<K, V> current = table[index]; 
            while (current != null) {
                if (current.key.hashCode() != key.hashCode()) {
                    current = current.next;

                } else {
                    if (current.key.equals(key)) {
                        V old_value = current.value;
                        current.value = value;
                        return old_value;

                    } else {
                        current = current.next;
                    }
                }
            }

        }
        return null;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
       int index = Math.abs(key.hashCode() % DEFAULT_SIZE);
        if (table[index] == null) {
            return false;

        } else {
            EntryNode<K, V> current = table[index]; 
            while (current != null) {
                if (current.key.hashCode() != key.hashCode()) {
                    current = current.next;

                } else {
                    if (current.key.equals(key) & current.value.equals(oldValue)) {
                        current.value = newValue;
                        return true;

                    } else {
                        current = current.next;
                    }
                }
            }

        }
        return false;
    }

    @Override
    public int size() {
        int size = 0;
        for (int i = 0; i < DEFAULT_SIZE; i++){
            if (table[i] == null) {
                continue;
            }
            EntryNode<K, V> current = table[i];
            while (current != null){
                current = current.next;
                size += 1;
            }
        }

        return size;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < DEFAULT_SIZE; i++){
            if (table[i] != null) {
                return false;
            } 
        }

        return true;
    }

    @Override
    public void clear() {
        for (int i = 0; i < DEFAULT_SIZE; i++){
            table[i] = null;
        }
    }

    @Override
    public Collection<V> values() {
        Collection<V> values = new ArrayList<>();
 
        for (int i = 0; i < DEFAULT_SIZE; i++){
            if (table[i] == null) {
                continue;
            }
            EntryNode<K, V> current = table[i];
            while (current != null){
                values.add(current.value);
                current = current.next;
            }
        }

        return values;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> entries = new HashSet<>();

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            EntryNode<K, V> current = table[i];
            while (current != null) {
                entries.add(current);
                current = current.next;
            }
        }

        return entries;
    }
}
