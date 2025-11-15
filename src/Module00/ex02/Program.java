import  java.util.Scanner;


public class Program{

    private static int SumOfDigits (int Number, int sum){
        if (Number / 10 == 0) {
            sum += Number % 10;
            return sum;
        }
        sum += Number % 10;
        return (SumOfDigits(Number / 10, sum));
    }

    public static boolean isPrime (int number){
        if (number <= 1 || (number % 2 == 0 && number != 2))
            return (false);
        for (int i = 3; i <= Math.sqrt(number); i += 2)
        {
            if (number % i == 0)
                return (false) ;
        }
        return (true);
    }

    public static void main (String[] args){
        int count;
        int Sum;
        int Delimiter;

        count = 0;
        Sum = 0;
        Delimiter = 42;

        Scanner ReadLine = new Scanner(System.in);
        while (true){
            int Number = ReadLine.nextInt();
            if (Delimiter == Number) {
                System.out.println("Count of coffee-request : " + count);
                break;
            }
            Sum = SumOfDigits(Number, Sum);
            if (isPrime(Sum)){
                count++;
            }
            Sum = 0;
        }
        ReadLine.close();
    }
}