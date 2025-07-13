public class Program {
    public static boolean isPrime (int number){
        if (number <= 1 || (number % 2 == 0 && number != 2)) {
            System.out.println ("not prime divide by 2 " + number);
            return true;
        }
        for (int i = 3; i <= Math.sqrt(number); i += 2)
        {
            // System.out.println (number);
            if (number % i == 0)
            {
                System.out.println ("not prime divide by i " + i);
                return true ;
            }
        }
        System.out.println ("prime only divide by itself " + number);
        return false;
    }

    public static void main (String[] args){
        isPrime(23);
    }
}
// 2 - 1 ( not prime )