package HW3;

import java.util.Random;

public class SequenceGenerator {
    public static Sequence generate(int length){
        Random random = new Random();
        Sequence seq = new Sequence();

        for (int i = 0; i < length; i++){
            seq.add(
                random.nextInt(100)
            );
        }
        return seq;
    }
    
}
