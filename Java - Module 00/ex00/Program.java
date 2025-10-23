public class Program {

    private static int SumOfDigits (int i, int sum){
        sum += i % 10;
        if (i / 10 == 0) {
//            sum += i % 10;
            return sum;
        }
        return SumOfDigits(i / 10, sum);

    }
    public static void main (String[] args){
        int Num;
        int Sum;

        Sum = 0;
        Num = 479598;

        Sum = SumOfDigits(479598, Sum);
        System.out.println("final sum = " + Sum);
    }
}