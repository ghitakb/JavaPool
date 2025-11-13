public class User {
    private Integer Identifier = 0;
    private String  Name = "";
    private Integer Balance = 0;

    public User(String name, Integer balance, Integer id) {
        this.Identifier = id;
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

    public void setID(Integer id) {
        this.Identifier = id;
    }

    public Integer getBalance() {
        return this.Balance;
    }

    public void setBalance(Integer balance) {
        if (Balance < 0) {
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