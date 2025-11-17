package ru.itis.hashes;

/**
 * 24.03.2021
 * 25. Associative Arrays
 *
 * @author Sidikov Marsel (First Software Engineering Platform)
 * @version v1.0
 */
public class Main {

    public static int hashCode(String text) {
        char codes[] = text.toCharArray();
        int hash = 0;
        for (int i = 0; i < codes.length; i++) {
            hash += codes[i] * (i + 1);
        }
        return hash;
    }

    public static int hashCodeInternal(String text) {
        int hash = 0;
        char codes[] = text.toCharArray();

        for (int i = 0; i < codes.length; i++) {
            hash = 31 * hash + codes[i];
        }

        return hash;
    }

    // 72 101 108 108 111 33

    // 1) hash = 31 * 0 + 72
    // 2) hash = 31 * (31 * 0 + 72) + 101
    // 3) hash = 31 * (31 * (31 * 0 + 72) + 101) + 108
    // 4) hash = 31 * (31 * (31 * (31 * 0 + 72) + 101) + 108) + 108
    // 5) hash = 31 * (31 * (31 * (31 * (31 * 0 + 72) + 101) + 108) + 108) + 111
    // 6) hash = 31 * (31 * (31 * (31 * (31 * (31 * 0 + 72) + 101) + 108) + 108) + 111) + 33
    // hash = 31 * 31 * 31 * 31 * 31 * 72 + 31 * 31 * 31 * 31 * 101 + 31 * 31 * 31 * 108 + 31 * 31 * 108 + 31 * 111 + 33
    // hash = 31^5 * 72 + 31^4 * 101 + 31^3 * 108 + 31^2 * 108 + 31^1 * 111 + 31^0 * 33
    // hash = 31^[n - 1 - i] * codes[i]
    // в String учитывается -> позиция символа, код символа, множитель 31
    // почему 31? потому что это простое число, и оно не раскладывается на множители, следовательно, коллизий будет меньше
    // 200 -> 10 * 20 -> 5 * 2 * 5 * 2 -> 50 * 2 * 2


    public static void main(String[] args) {
        String text = "Hello!";
        String text2 = "Hlelo!";

        // 72 101 108 108 111 33

        System.out.println(hashCode(text));
        System.out.println(hashCode(text2));
        System.out.println(hashCodeInternal(text));
        System.out.println(hashCodeInternal(text2));

        Human daniil = new Human("Даниил", 21, true, 180);
        Human marsel = new Human("Марсель", 27, false, 185);

        System.out.println("Даниил - " + daniil.hashCode());
        System.out.println("Марсель - " + marsel.hashCode());

    }
}
