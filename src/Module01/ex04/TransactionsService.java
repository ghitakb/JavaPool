import java.util.UUID;
import java.util.Arrays;


public class TransactionsService {
    private UsersList   usersList;

    public TransactionsService(UsersList usersList) {
        this.usersList = usersList;
    }

    public UsersList getUsersList() {
        return usersList;
    }

    public void setUsersList(UsersList usersList) {
        this.usersList = usersList;
    }

    public int  addUser(String name, int balance){
        User newUser = new User(name, balance);
        usersList.addUser(newUser);
        return (newUser.getID());
    }

    public int getUsersBalance (Integer id) {
        try {
            return usersList.retrieveById(id).getBalance();
        }
        catch (UserNotFoundException e) {
            throw new RuntimeException("User with ID " + id + " not found");
        }
    }

    public void transfer(int senderId, int recipientId, int amount) {
        try {
            User sender = usersList.retrieveById(senderId);
            User recipient = usersList.retrieveById(recipientId);

            if (sender.getBalance() < amount) {
                throw new IllegalArgumentException("Sender " + sender.getName() + " has insufficient balance.");
            }

            UUID transferId = UUID.randomUUID(); // same ID for both transactions

            Transaction debit = new Transaction(recipient, sender, -amount, Transaction.TransferCategory.DEBIT, transferId);
            Transaction credit = new Transaction(recipient, sender, amount, Transaction.TransferCategory.CREDIT, transferId);

            // Update balances because creating 2 Transaction undo the transaction
            sender.setBalance(sender.getBalance() - amount);
            recipient.setBalance(recipient.getBalance() + amount);

            // Add transactions to each user's list
            sender.getTransactionslist().addTransaction(debit);
            recipient.getTransactionslist().addTransaction(credit);

        } catch (UserNotFoundException e) {
            throw new RuntimeException("Transfer failed: " + e.getMessage());
        }
        catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }


    public Transaction[] getUserTransactions(int userId) {
        try {
            User user = usersList.retrieveById(userId);
            return (user.getTransactionslist().toArray());
        }
        catch (UserNotFoundException e) {
            throw new RuntimeException("User with ID " + userId + " not found");
        }
        catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void removeTransaction(int userId, UUID transactionId) {
        try {
            User user = usersList.retrieveById(userId);
            user.getTransactionslist().removeTransaction(transactionId);

        }
        catch (UserNotFoundException e) {
            throw new RuntimeException("User with ID " + userId + " not found");
        }
        catch (TransactionNotFoundException e) {
            throw new RuntimeException("Transaction with ID " + transactionId + " not found for user " + userId);
        }
        catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public Transaction[] checkTransactions() throws UserNotFoundException{
            int totalCount = 0;
            for (int i = 0; i < usersList.getUsersNumber(); i++) {
                User user = usersList.retrieveByIndex(i);
                totalCount += user.getTransactionslist().toArray().length;
            }

            Transaction[] allTransactions = new Transaction[totalCount];
            int index = 0;
            for (int i = 0; i < usersList.getUsersNumber(); i++) {
                User user = usersList.retrieveByIndex(i);
                Transaction[] userTransaction = user.getTransactionslist().toArray();
                for (Transaction t : userTransaction) {
                    allTransactions[index++] = t;
                }
            }

            Arrays.sort(allTransactions, (a, b) -> a.getIdentifier().compareTo(b.getIdentifier()));

            Transaction[] unpaired = new Transaction[totalCount];
            int count = 0;

            for (int i = 0; i < totalCount; i++) {
                if (i == totalCount - 1 || !allTransactions[i].getIdentifier().equals(allTransactions[i + 1].getIdentifier())) {
                    unpaired[count++] = allTransactions[i];
                }
                else
                    i++;
            }

            Transaction[] result = new Transaction[count];
            System.arraycopy(unpaired, 0, result, 0, count);
            return result;
    }
}