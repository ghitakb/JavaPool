package ex00;


import  java.util.UUID;


public class Transaction {

    private UUID    identifier;
    private User    recipient;
    private User    sender;
    private Integer transferAmount;
    private TransferCategory transferCategory;
    enum TransferCategory {
        DEBIT,
        CREDIT
    }

    public Transaction(User recipient, User sender, Integer amount, TransferCategory category) {
        this.identifier = UUID.randomUUID();
        this.recipient = recipient;
        this.sender = sender;
        this.transferCategory = category;
        this.transferAmount = amount;

        if ((category == TransferCategory.DEBIT && amount > 0) ||
                (category == TransferCategory.CREDIT && amount < 0)) {
            throw new IllegalArgumentException("Amount sign does not match transfer category");
        }

        // Ensure sender has enough money
        if (sender.getBalance() < Math.abs(this.transferAmount)) {
            throw new IllegalArgumentException("Sender has insufficient funds");
        }

        // Update balances
        sender.setBalance(sender.getBalance() - Math.abs(this.transferAmount));
        recipient.setBalance(recipient.getBalance() + Math.abs(this.transferAmount));
    }


    public UUID getIdentifier() {
        return identifier;
    }

    public void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Integer getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(Integer transferAmount) {
        this.transferAmount = transferAmount;
    }

    public TransferCategory getTransferCategory() {
        return transferCategory;
    }

    public void setTransferCategory(TransferCategory transferCategory) {
        this.transferCategory = transferCategory;
    }
}

