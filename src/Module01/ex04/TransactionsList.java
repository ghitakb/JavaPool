import  java.util.UUID;

public interface   TransactionsList {
    void            addTransaction(Transaction transaction);
    void            removeTransaction(UUID identifier) throws TransactionNotFoundException;
    Transaction[]   toArray();
}