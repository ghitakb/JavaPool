import  java.util.UUID;


public class Program {
    public static void main (String[] args) {
        try {
            User alice = new User("Alice", 1000);
            User bob = new User("Bob", 2000);

            // Create several transactions
            Transaction t1 = new Transaction(alice, bob, -200, Transaction.TransferCategory.DEBIT);
            Transaction t2 = new Transaction(bob, alice, 150, Transaction.TransferCategory.CREDIT);
            Transaction t3 = new Transaction(alice, bob, -50, Transaction.TransferCategory.DEBIT);
            Transaction t4 = new Transaction(bob, alice, 100, Transaction.TransferCategory.CREDIT);

            // Add them to Alice’s transaction list
            alice.getTransactionslist().addTransaction(t1);
            alice.getTransactionslist().addTransaction(t2);
            alice.getTransactionslist().addTransaction(t3);
            alice.getTransactionslist().addTransaction(t4);

            //  Print all transactions
            System.out.println("=== Alice’s Transactions (Before removal) ===");
            for (Transaction t : alice.getTransactionslist().toArray()) {
                System.out.println(t);
            }

            //  Remove one transaction
            UUID idToRemove = t2.getIdentifier();
            System.out.println("\nRemoving transaction with ID: " + idToRemove + "\n");
            alice.getTransactionslist().removeTransaction(idToRemove);

            //  Print after removal
            System.out.println("=== Alice’s Transactions (After removal) ===");
            for (Transaction t : alice.getTransactionslist().toArray()) {
                System.out.println(t);
            }

            // Try remove a non-existent ID
            alice.getTransactionslist().removeTransaction(UUID.randomUUID());
        }
        catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}
