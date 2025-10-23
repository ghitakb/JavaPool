package ex00;


public class User {
    private String  name;
    private Integer id;
    private Integer balance;

    public User(String name, Integer balance, Integer id) {
        if (balance < 0)
            throw new IllegalArgumentException("Balance cannot be negative");

        this.name = name;
        this.id = id;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        name = name;
    }

    public Integer getID() {
        return id;
    }

    public void setID(Integer ID) {
        this.id = id;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        this.balance = balance;
    }
}