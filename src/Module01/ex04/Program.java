import java.util.UUID;

public class Program {
    public static void main(String[] args) {
        try {
            UsersList usersList = new UsersArrayList();
            TransactionsService service = new TransactionsService(usersList);

            // Add users
            int AliceId = service.addUser("Alice", 1000);
            int BobId = service.addUser("Bob", 500);
            int CharlieId = service.addUser("Charlie", 700);

            // initial balances
            System.out.println("=== Initial Balances ===");
            for (int i = 0; i < usersList.getUsersNumber(); i++) {
                User u = usersList.retrieveByIndex(i);
                System.out.println("User " + u.getID() + " balance: " + u.getBalance());
            }

            // Perform some transfers
            System.out.println("\n=== Performing Transfers ===");

            service.transfer(AliceId, BobId, 200); // Alice → Bob
            service.transfer(BobId, CharlieId, 100); // Bob → Charlie
            service.transfer(CharlieId, AliceId, 50);  // Charlie → Alice

            // balances after transfers
            System.out.println("\n=== Balances After Transfers ===");
            for (int i = 0; i < usersList.getUsersNumber(); i++) {
                User u = usersList.retrieveByIndex(i);
                System.out.println("User " + u.getID() + " balance: " + u.getBalance());
            }

            // all transactions for Alice
            System.out.println("\n=== Alice's Transactions ===");
            Transaction[] aliceTx = service.getUserTransactions(1);
            for (Transaction t : aliceTx) {
                System.out.println(t + "\n");
            }

            // Remove one of Alice's transactions
            if (aliceTx.length > 0) {
                UUID idToRemove = aliceTx[0].getIdentifier();
                service.removeTransaction(1, idToRemove);
                System.out.println("\nRemoved transaction with ID " + idToRemove + " from Alice.");
            }

            // Check for unpaired transactions
            System.out.println("\n=== Checking for Unpaired Transactions ===");
            Transaction[] unpaired = service.checkTransactions();
            if (unpaired.length == 0) {
                System.out.println("All transactions are correctly paired ✅");
            } else {
                System.out.println("Unpaired transactions found:");
                for (Transaction t : unpaired) {
                    System.out.println(t);
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
