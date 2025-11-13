public class Program {
    public static void main(String[] args) {
       try {
            User alice = new User("Alice", 1000, 1);
            User bob = new User("Bob", 2000, 2);

            // DEBIT: Alice sends 200 to Bob
            Transaction t1 = new Transaction(bob, alice, -200, Transaction.TransferCategory.DEBIT);
            // bob = 2200, alice = 800

            // CREDIT: Bob sends 150 to AliceClone
            Transaction t2 = new Transaction(alice, bob, 150, Transaction.TransferCategory.CREDIT);
            // alice = 950, bob = 2050

            System.out.println("Final balances:");
            System.out.println(alice);
            System.out.println(bob);

            System.out.println("\nTransactions:");
            System.out.println(t1 + "\n");
            System.out.println(t2);
       }
       catch (Exception e) {
           System.err.println("An unexpected error occurred: " + e.getMessage());
       }
    }
}
