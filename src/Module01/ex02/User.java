public class User {
    private final Integer   Identifier;
    private String          Name = "";
    private Integer         Balance = 0;

    public User(String name, Integer balance) {
        this.Identifier = UserIdsGenerator.getInstance().generateId();
        this.Name = name;
        this.Balance = balance;

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

    @Override
    public String toString()
    {
        return (this.getName() + " (ID: " + this.getID() + ") → " + this.getBalance());
    }
}