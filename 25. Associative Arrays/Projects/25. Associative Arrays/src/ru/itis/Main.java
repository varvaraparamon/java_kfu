package ru.itis;

import java.net.Socket;

import ru.itis.primitive.PrimitiveMap;
import ru.itis.result.HashMap;

/**
 * 24.03.2021
 * 25. Associative Arrays
 *
 * @author Sidikov Marsel (First Software Engineering Platform)
 * @version v1.0
 */
public class Main {
    public static void main(String[] args) {
        // TODO: с помощью Map посчитать количество вхождений букв текста
        // TODO: AAABBBA, A - 4, B - 3
        Map<String, Integer> map = new HashMap<>();
        map.put("Марсель", 27);
        map.put("Даниил", 21);
        map.put("Виктор", 24);
        map.put("Виктор", 28);
        map.put("Алия", 20);
        map.put("Айрат", 22);
        map.put("Ильгам", 21);
        map.put("Максим", 23);
        map.put("Human1", 33);
        map.put("Human2", 34);
        map.put("Human3", 35);
        map.put("Human4", 36);
        map.put("Human5", 37);
        map.put("Human6", 38);
        map.put("Human7", 39);
        map.put("Human8", 40);

        System.out.println(map.get("Марсель")); // 27
        System.out.println(map.get("Даниил")); // 21
        
        System.out.println(map.remove("Марсель")); //27
        System.out.println(map.containsValue(27)); //false

        System.out.println(map.containsValue(40)); //true
        System.out.println(map.containsValue(2)); //false

        System.out.println(map.remove("Максим", 23)); //true
        System.out.println(map.remove("Human8", 1239021)); //false

        System.out.println(map.putIfAbsent("Human8", 41)); //40
        System.out.println(map.putIfAbsent("Human9", 41)); //null

        System.out.println(map.replace("Human9", 101)); //41
        System.out.println(map.replace("Human10", 12093820)); //null

        System.out.println(map.replace("Human9", 101, 41)); //true
        System.out.println(map.replace("Human9", 12093820, 41)); //false

        System.out.println(map.KeySet().toString());
        System.out.println(map.values().toString());
        System.out.println(map.entrySet().toString());

        System.out.println("Marsel".hashCode());


        System.out.println(map.size()); 
        System.out.println(map.isEmpty()); //false
        map.clear(); 
        System.out.println(map.isEmpty()); //true

    }
}
