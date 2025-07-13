public class Program {

    private static int SumOfDigits (int i, int sum){
//        since sum += i%10; is called in the condition and outside should it be called just once here ?
        System.out.println("i = " + i);
        if (i / 10 == 0) {
            System.out.println("here");
            sum += i%10;
            System.out.println("if sum = " + sum);
            return sum;
        }
        sum += i%10;
        System.out.println("sum = " + sum);
        return SumOfDigits(i/10, sum);

    }
    public static void main (String[] args){
//        System.out.println("Hello, let's begin");
//        int sum = 0;
//        for (int i = 479598; i > 0; i=(i/10)) {
//            sum += i%10;
//            System.out.println("i = " + i);
//            System.out.println("sum = " + sum);
//
//        }
        int sum = 0;
        sum = SumOfDigits(479598, sum);
//        System.out.println("hello");
        System.out.println("final sum = " + sum);

    }
}