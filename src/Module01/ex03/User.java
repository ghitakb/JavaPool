public class User {
    private final Integer           Identifier;
    private String                  Name = "";
    private Integer                 Balance = 0;
    private TransactionsLinkedList  transactionslist;

    public User(String name, Integer balance) {
        this.Identifier = UserIdsGenerator.getInstance().generateId();
        this.Name = name;
        this.Balance = balance;
        this.transactionslist = new TransactionsLinkedList();

        if (Balance < 0)
            throw new IllegalArgumentException("Balance cannot be negative");
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public Integer getID() {
        return this.Identifier;
    }

    public Integer getBalance() {
        return this.Balance;
    }

    public void setBalance(Integer balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        this.Balance = balance;
    }

    public TransactionsLinkedList getTransactionslist() {
        return transactionslist;
    }

    public void setTransactionslist(TransactionsLinkedList transactionslist) {
        this.transactionslist = transactionslist;
    }

    @Override
    public String toString()
    {
        return (this.getName() + " (ID: " + this.getID() + ") → " + this.getBalance());
    }
}