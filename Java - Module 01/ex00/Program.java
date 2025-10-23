package ex00;

public class Program {
    public static void main(String[] args) {
        User alice = new User("Alice", 1000, 1);
        User bob = new User("Bob", 2000, 2);
        User aliceClone = new User("Alice", 300, 3); // same name

        // DEBIT: Alice sends 200 to Bob
        Transaction t1 = new Transaction(
                bob,
                alice,
                -200,
                Transaction.TransferCategory.DEBIT
        );

        // CREDIT: Bob sends 150 to AliceClone
        Transaction t2 = new Transaction(
                aliceClone,
                bob,
                150,
                Transaction.TransferCategory.CREDIT
        );

        System.out.println("Final balances:");
        System.out.println(alice.getName() + " (ID: " + alice.getID() + ") → " + alice.getBalance());
        System.out.println(bob.getName() + " (ID: " + bob.getID() + ") → " + bob.getBalance());
        System.out.println(aliceClone.getName() + " (ID: " + aliceClone.getID() + ") → " + aliceClone.getBalance());

        System.out.println("\nTransactions:");
        System.out.println("Transaction ID: " + t1.getIdentifier());
        System.out.println("Type: " + t1.getTransferCategory());
        System.out.println("Amount: " + t1.getTransferAmount());
        System.out.println("From: " + t1.getSender().getName());
        System.out.println("To: " + t1.getRecipient().getName());

        System.out.println("\nTransaction ID: " + t2.getIdentifier());
        System.out.println("Type: " + t2.getTransferCategory());
        System.out.println("Amount: " + t2.getTransferAmount());
        System.out.println("From: " + t2.getSender().getName());
        System.out.println("To: " + t2.getRecipient().getName());
    }
}
