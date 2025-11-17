import java.util.Scanner;
import java.util.Arrays; 

public class Ex2 {
    public static void bubble_sort(int[] numbers, int n){
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n - i - 1; j++){
                if (numbers[j] > numbers[j+1]){
                    int tmp = numbers[j];
                    numbers[j] = numbers[j+1];
                    numbers[j+1] = tmp;
                }

            }
        }
    }


    public static void main (String[] args) {
        int numbers[] = new int[5];
        Scanner scanner = new Scanner(System.in);
        int arrayElement = scanner.nextInt();
        int real_size = 0;

        while (arrayElement != -1){
            if (numbers.length < real_size + 1){
                int numbers_new[] = new int[numbers.length * 2];
                for (int i = 0; i < real_size; i++){
                    numbers_new[i] = numbers[i];
                }
                numbers = numbers_new;
            }
            numbers[real_size] = arrayElement;
            real_size += 1;
            arrayElement = scanner.nextInt();
        }
        bubble_sort(numbers, real_size);

        for (int i = 0; i < real_size; i++){
            System.out.print(numbers[i] + " ");
        }
        if (real_size > 0)
            System.out.println("\nmax: " + numbers[real_size-1] + "\n" + "min: " + numbers[0]);
    }
}
