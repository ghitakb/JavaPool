import  java.util.Scanner;


public class Program{

    private static int SumOfDigits (int i, int sum){
//        since sum += i%10; is called in the condition and outside should it be called just once here ?
//        System.out.println("i = " + i);
        if (i / 10 == 0) {
//            System.out.println("here");
            sum += i%10;
            System.out.println("if sum = " + sum);
            return sum;
        }
        sum += i%10;
//        System.out.println("sum = " + sum);
        return SumOfDigits(i/10, sum);

    }

    public static boolean isPrime (int number){
        if (number <= 1 || (number % 2 == 0 && number != 2)) {
            System.out.println ("not prime divide by 2 " + number);
            return false;
        }
        for (int i = 3; i <= Math.sqrt(number); i += 2)
        {
            // System.out.println (number);
            if (number % i == 0)
            {
                System.out.println ("not prime divide by i " + i);
                return false ;
            }
        }
        System.out.println ("prime only divide by itself " + number);
        return true;
    }


    public static void main (String[] args){
        int count;
//        int[] number = {101};
        int Sum;
        int Delimiter;

        count = 0;
        Sum = 0;
        Delimiter = 42;
//        if (isPrime(sum)){
//            count++;
//        }

        Scanner ReadLine = new Scanner(System.in);
        while (true){
            System.out.println("Enter Number : ");
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

    }
}