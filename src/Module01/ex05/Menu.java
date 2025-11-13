import java.util.Scanner;
import java.util.UUID;


public class Menu {
    UsersList           users;
    TransactionsService transactions;
    private boolean     devMode = false;

    public Menu(boolean devMode){
        this.users = new UsersArrayList();
        this.transactions = new TransactionsService(users);
        this.devMode = devMode;
    }

    public void printMenu(){
        System.out.println("1. Add a user");
        System.out.println("2. View user balances");
        System.out.println("3. Perform a transfer");
        System.out.println("4. View all transactions for a specific user");
        if (devMode) {
            System.out.println("5. DEV - remove a transfer by ID");
            System.out.println("6. DEV - check transfer validity");
        }
        System.out.println((devMode ? "7" : "5") + ". Finish execution");
        System.out.print("-> ");

    }

    private void handleAddUser(Scanner input) {
        System.out.println("Enter a user name and a balance");
        System.out.print("-> ");
        String[] data = input.nextLine().trim().split("\\s+");

        if (data.length != 2) {
            System.err.println("Invalid input. Expect user name (String) and a balance (Number)");
            return;
        }

        String name = data[0];
        try {
            int balance = Integer.parseInt(data[1]);
            int id = transactions.addUser(name, balance);
            System.out.println("User with id = " + id + " is added");
            System.out.println("---------------------------------------------------------");
        }
        catch (NumberFormatException e) {
            System.err.println("Balance must be a valid number!");
        }
        catch (IllegalArgumentException e) {
            // This covers negative balances
            System.err.println(e.getMessage());
        }
        catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void handleViewBalance(Scanner input) {
        System.out.println("Enter a user ID");
        System.out.print("-> ");
        String line = input.nextLine().trim();

        try {
            int id = Integer.parseInt(line);
            User user = users.retrieveById(id);
            System.out.println(user.getName() + " - " + user.getBalance());
            System.out.println("---------------------------------------------------------");
        }
        catch (NumberFormatException e) {
            System.err.println("Invalid ID format. Please enter a number.");
        }
        catch (UserNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
        catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void handleTransfer(Scanner input) {
        System.out.println("Enter sender ID, recipient ID, and transfer amount");
        System.out.print("-> ");
        String line = input.nextLine().trim();
        String[] data = line.split("\\s+");

        if (data.length != 3) {
            System.err.println("Invalid input. Please enter: sender_id, recipient_id and amount");
            return;
        }

        try {
            int senderId = Integer.parseInt(data[0]);
            int recipientId = Integer.parseInt(data[1]);
            int amount = Integer.parseInt(data[2]);

            transactions.transfer(senderId, recipientId, amount);

//            System.out.println("Transfer completed: " + amount + " units from User " + senderId + " to User " + recipientId);
            System.out.println("The transfer is completed");
            System.out.println("---------------------------------------------------------");

        }
        catch (NumberFormatException e) {
            System.err.println("Error: Invalid input. IDs and amount must be numbers.");
        }
        catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void handleViewTransactions(Scanner input) {
        System.out.println("Enter a user ID");
        System.out.print("-> ");
        int userId;

        try {
            userId = Integer.parseInt(input.nextLine().trim());
            Transaction[] userTx = transactions.getUserTransactions(userId);

            for (Transaction t : userTx) {
                if (t.getTransferCategory() == Transaction.TransferCategory.DEBIT) {
                    System.out.println("To " + t.getRecipient().getName() + "(id = " + t.getRecipient().getID() + ") " + t.getTransferAmount() + " with id = " + t.getIdentifier());
                } else {
                    System.out.println("From " + t.getSender().getName() + "(id = " + t.getSender().getID() + ") " + t.getTransferAmount() + " with id = " + t.getIdentifier());
                }
            }
            System.out.println("---------------------------------------------------------");

        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid user ID format.");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void handleRemoveTransaction(Scanner input) {
        System.out.println("Enter a user ID and a transfer ID");
        System.out.print("-> ");
        String[] parts = input.nextLine().trim().split("\\s+");

        if (parts.length != 2) {
            System.err.println("Invalid input. Expect user ID and a transfer ID");
            return;
        }

        try {
            int userId = Integer.parseInt(parts[0]);
            UUID transactionId = UUID.fromString(parts[1]);

            // We get the user to display transaction info before removal
            User user = transactions.getUsersList().retrieveById(userId);
            Transaction[] userTx = user.getTransactionslist().toArray();

            // Find the transaction
            for (Transaction t : userTx) {
                if (t.getIdentifier().equals(transactionId)) {
                    System.out.println("Transfer " +
                            (t.getTransferCategory() == Transaction.TransferCategory.DEBIT ? "To " : "From ") +
                            (t.getTransferCategory() == Transaction.TransferCategory.DEBIT
                                    ? t.getRecipient().getName()
                                    : t.getSender().getName()) +
                            "(id = " + (t.getTransferCategory() == Transaction.TransferCategory.DEBIT
                            ? t.getRecipient().getID()
                            : t.getSender().getID()) + ") " +
                            Math.abs(t.getTransferAmount()) + " removed");
                    break;
                }
            }
            transactions.removeTransaction(userId, transactionId);
            System.out.println("---------------------------------------------------------");
        }
        catch (NumberFormatException e) {
            System.err.println("Error: Invalid user ID format.");
        }
        catch (IllegalArgumentException e) {
            System.err.println("Error: Invalid UUID format.");
        }
        catch (UserNotFoundException | TransactionNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
        catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void handleCheckValidity() {
        System.out.println("Check results:");
        try {
            Transaction[] unpaired = transactions.checkTransactions();

            if (unpaired.length == 0) {
                System.out.println("All transactions are valid.");
                return;
            }

            for (Transaction t : unpaired) {
                User recipient = t.getRecipient();
                User sender = t.getSender();
                int amount = Math.abs(t.getTransferAmount());
                UUID id = t.getIdentifier();

                if (t.getTransferCategory() == Transaction.TransferCategory.CREDIT) {
                    System.out.println(recipient.getName() + "(id = " + recipient.getID() + ") has an unacknowledged transfer id = " + id + " from " + sender.getName() + "(id = " + sender.getID() + ") for " + amount);
                } else {
                    System.out.println(sender.getName() + "(id = " + sender.getID() +") has an unacknowledged transfer id = " + id +" to " + recipient.getName() + "(id = " + recipient.getID() +") for " + amount);
                }
            }
            System.out.println("---------------------------------------------------------");
        } catch (UserNotFoundException e) {
            System.err.println("Error checking transactions: " + e.getMessage());
        }
        catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void handleMenuChoice(String choice, Scanner input) {
        try {
            int option = Integer.parseInt(choice.trim());

            if (!devMode && option > 5) {
                System.out.println("Invalid option. Please try again.");
            }

            switch (option) {
                case 1 -> handleAddUser(input);
                case 2 -> handleViewBalance(input);
                case 3 -> handleTransfer(input);
                case 4 -> handleViewTransactions(input);
                case 5 -> {
                    if (devMode)
                        handleRemoveTransaction(input);
                    else {
                        System.out.println("Finish execution...");
                        System.exit(0);
                    }
                }
                case 6 -> {
                    if (devMode)
                        handleCheckValidity();
                }
                case 7 -> {
                    if (devMode) {
                        System.out.println("Finish execution...");
                        System.exit(0);
                    }
                }
                default -> System.out.println("Invalid option. Please try again.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
        catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
