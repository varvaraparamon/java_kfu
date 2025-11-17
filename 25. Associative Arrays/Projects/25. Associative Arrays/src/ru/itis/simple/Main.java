package ru.itis.simple;

/**
 * 24.03.2021
 * 25. Associative Arrays
 *
 * @author Sidikov Marsel (First Software Engineering Platform)
 * @version v1.0
 */
public class Main {
    public static void main(String[] args) {
        IntegerKeyMap<String> map = new ArrayMap<>();
        map.put(6, "Hello!");
        map.put(3, "Marsel!");

        System.out.println(map.get(6));
        System.out.println(map.get(3));
    }
}
