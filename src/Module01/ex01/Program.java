public class Program {
    public static void main(String[] args) {
       try {
           UserIdsGenerator id1 = UserIdsGenerator.getInstance();
           UserIdsGenerator id2 = UserIdsGenerator.getInstance();
           System.out.println("id1 = " + id1 + "\nid2 = " + id2 + "\nid1 = id2: " + (id1 == id2));

           User alice = new User("Alice", 1000);
           User bob = new User("Bob", 2000);
           User john = new User("john", 300);
           User mike = new User("mike",700);

            // DEBIT: Alice sends 200 to Bob
            Transaction t1 = new Transaction(bob, alice, -200, Transaction.TransferCategory.DEBIT);

            // CREDIT: Bob sends 150 to AliceClone
            Transaction t2 = new Transaction(alice, bob, 150, Transaction.TransferCategory.CREDIT);

            System.out.println("Final balances:");
            System.out.println(alice);
            System.out.println(bob);
            System.out.println(john);
            System.out.println(mike);

            System.out.println("\nTransactions:");
            System.out.println(t1 + "\n");
            System.out.println(t2);
        }
       catch (Exception e) {
           System.err.println("An unexpected error occurred: " + e.getMessage());
       }
    }
}

