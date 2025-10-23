import java.util.Scanner;

public class Program {
    public static boolean isPrime(int number) throws Exception {
        int Count = 0;

        if (number < 0)
            throw new Exception("IllegalArgument");

        Count++;
        if (number <= 1 || (number % 2 == 0 && number != 2)) {
            System.out.println("false " + Count);
            return false;
        }

        for (int i = 3; i <= Math.sqrt(number); i += 2) {
            Count++;
            if (number % i == 0) {
                System.out.println("false " + Count);
                return false;
            }
        }

        System.out.println("true " + Count);
        return true;
    }

    public static void main(String[] args) {
        Scanner ReadLine = new Scanner(System.in);
        String Input = ReadLine.nextLine();

        try {
            int Number = Integer.parseInt(Input);
            isPrime(Number);
        } catch (Exception e) {
            // Single error message for all exceptions
            System.err.println("IllegalArgument");
        }
        ReadLine.close();
    }
}
