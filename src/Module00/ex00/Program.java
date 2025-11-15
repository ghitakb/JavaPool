public class Program {

    private static int SumOfDigits (int i, int sum){
        sum += i % 10;
        if (i / 10 == 0)
            return sum;
        return SumOfDigits(i / 10, sum);
    }

    public static void main (String[] args)
    {
        int Num;
        int Sum;

        Sum = 0;
        Num = 479598;

        Sum = SumOfDigits(Num, Sum);
        System.out.println(Sum);
    }
}