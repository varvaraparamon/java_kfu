import java.util.Scanner;

public class Ex3 {

    public static int[] readNumbers(Scanner scanner){
        System.out.print("Введите массив: ");

        int realSize = 0;
        int currentSize = 10;

        int numbers[] = new int[currentSize];
        int element = scanner.nextInt();

        
        while (element != -1){
            if (realSize + 1 > currentSize){
                int tmpNumbers[] = new int[currentSize*2];
                for (int i = 0; i < realSize; i++){
                    tmpNumbers[i] = numbers[i];
                }

                numbers = tmpNumbers;
                currentSize = currentSize*2;
            }
            numbers[realSize] = element;
            realSize += 1;

            element = scanner.nextInt();
        }

        int tmpNumbers[] = new int[realSize];
        for (int i = 0; i < realSize; i++){
            tmpNumbers[i] = numbers[i];
        }
        numbers = tmpNumbers;

        return numbers;
    }

    public static void printNumbers(int[] numbers){
        int size = numbers.length;
        for (int i = 0; i < size; i++){
            System.out.print(numbers[i] + " ");
        }

        System.out.println();
    }

    public static void reverseNumbers(int[] numbers){
        int size = numbers.length;
        
        for (int i = 0; i < size/2; i++){
            int tmp = numbers[i];
            numbers[i] = numbers[size - i - 1];
            numbers[size - i - 1] = tmp;
        }
        printNumbers(numbers);

    }

    public static void sortAscending(int[] numbers){
        int size = numbers.length;

        for (int i = 0; i < size; i++){
            for (int j = 0; j < size - i - 1; j++){
                if (numbers[j] > numbers[j+1]){
                    int tmp = numbers[j];
                    numbers[j] = numbers[j+1];
                    numbers[j+1] = tmp;
                }
            }
        } 
        printNumbers(numbers);
    }

    public static void sortDescending(int[] numbers){
        int size = numbers.length;

        for (int i = 0; i < size; i++){
            for (int j = 0; j < size - i - 1; j++){
                if (numbers[j] < numbers[j+1]){
                    int tmp = numbers[j];
                    numbers[j] = numbers[j+1];
                    numbers[j+1] = tmp;
                }
            }
        } 
        printNumbers(numbers);
    }

    public static void neighboursSum(int[] numbers){
        int size = numbers.length;
        int newSize = size/2 + size%2;
        int neighbours[] = new int[newSize];

        for (int i = 0; i < size/2; i++){
            neighbours[i] = numbers[i*2] + numbers[i*2 + 1];
        }
        if (size%2 != 0){
            neighbours[newSize-1] = numbers[size-1];
        }

        printNumbers(neighbours);
    }

    public static void choose(int[] numbers, Scanner scanner){

        while(true) {
            System.out.print("Введите номер функции: ");
            int funcNumber = scanner.nextInt();

            switch(funcNumber){
                case 1:
                    reverseNumbers(numbers);
                    break;
                case 2:
                    sortAscending(numbers);
                    break;
                case 3:
                    sortDescending(numbers);
                    break;
                case 4:
                    neighboursSum(numbers);
                    break;
                case -1:
                    return;
                default:
                    System.out.println("Вы ввели неправильное число");
                    break;

            }
        }

    }
    
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        int numbers[] = readNumbers(scanner);
        choose(numbers, scanner);
        scanner.close();
    }
}
