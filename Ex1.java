import java.util.Scanner;

class Ex1 {
    public static boolean isPrime(int num){
        int flag = 1;
        for (int i = 2; i*i <= num; i++){
            if (num%i == 0) {
                flag = 0;
                break;
            }
        }
        
        return flag == 1;
    }
    public static int sumDigs(int num){
        int sum = 0;
        while (num > 0){
            sum += num%10;
            num = num/10;
        }
        return sum;
    }
    public static void main (String[] args) {
        Scanner scanner = new Scanner(System.in);
        int num = scanner.nextInt();
        int sum = 0;

        int minNum = num;
        int maxNum = num;

        while(num != -1){
            if (num < minNum){
                minNum = num;
            } else if (num > maxNum) {
                maxNum = num;
            }

            boolean isPrimeSum = isPrime(
                sumDigs(num)
            );

            if (isPrimeSum){
                sum += 1;
            }
            num = scanner.nextInt();
        }
        
        System.out.println("Кол-во чисел с суммой цифр, являющейся простым числом: " + sum);
        System.out.println("Минимум: " + minNum);
        System.out.println("Максимум: " + maxNum);

        scanner.close();
    }
}