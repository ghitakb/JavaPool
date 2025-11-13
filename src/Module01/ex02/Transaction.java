import  java.util.UUID;


public class Transaction {

    private UUID                Identifier;
    private User                Recipient;
    private User                Sender;
    private Integer             TransferAmount;
    private TransferCategory    TransferCategory;
    enum TransferCategory {
        DEBIT,
        CREDIT
    }

    public Transaction(User recipient, User sender, Integer amount, TransferCategory category) {
        this.Identifier = UUID.randomUUID();
        this.Recipient = recipient;
        this.Sender = sender;
        this.TransferCategory = category;
        this.TransferAmount = amount;

        if ((category == TransferCategory.DEBIT && amount > 0) ||
                (category == TransferCategory.CREDIT && amount < 0)) {
            throw new IllegalArgumentException("Amount sign does not match transfer category");
        }

        if (sender.getBalance() < Math.abs(this.TransferAmount)) {
            throw new IllegalArgumentException("Sender has insufficient funds");
        }

        sender.setBalance(sender.getBalance() - Math.abs(this.TransferAmount));
        recipient.setBalance(recipient.getBalance() + Math.abs(this.TransferAmount));
    }

    public UUID getIdentifier() {
        return this.Identifier;
    }

    public void setIdentifier(UUID identifier) {
        this.Identifier = identifier;
    }

    public User getRecipient() {
        return this.Recipient;
    }

    public void setRecipient(User recipient) {
        this.Recipient = recipient;
    }

    public User getSender() {
        return Sender;
    }

    public void setSender(User sender) {
        this.Sender = sender;
    }

    public Integer getTransferAmount() {
        return this.TransferAmount;
    }

    public void setTransferAmount(Integer transferAmount) {
        this.TransferAmount = transferAmount;
    }

    public TransferCategory getTransferCategory() {
        return this.TransferCategory;
    }

    public void setTransferCategory(TransferCategory transferCategory) {
        this.TransferCategory = transferCategory;
    }

    @Override
    public String toString() {
        return ( "Transaction ID: " + this.getIdentifier() + "\n"
                + "Type: " + this.getTransferCategory() + "\n"
                + "Amount: " + this.getTransferAmount() + "\n"
                + "From: " + this.getSender().getName() + "\n"
                + "To: " + this.getRecipient().getName() + "\n" );
    }
}

