import  java.util.UUID;


class TransactionNotFoundException extends RuntimeException  {
    public TransactionNotFoundException (String message) {
        super(message);
    }
}

public  class TransactionsLinkedList implements TransactionsList {

    private Node    head;
    private int     size;

    private class   Node {
        private Transaction transaction;
        private Node        next;

        public Node(Transaction transaction){
            this.transaction = transaction;
            this.next = null;
        }

        public void setNextNode(Node node) {
            this.next = node;
        }

        public Node getNextNode() {
            return this.next;
        }
    }

    public TransactionsLinkedList(){
        this.head = null;
        this.size = 0;
    }

    @Override
    public void addTransaction(Transaction transaction){
        Node newNode = new Node(transaction);
        newNode.setNextNode(head);
        head = newNode;
        size++;
    }

    @Override
    public void removeTransaction(UUID identifier) throws TransactionNotFoundException {
        if (head == null) {
            throw new TransactionNotFoundException("Transaction list is empty.");
        }

        // head is the one to remove
        if (head.transaction.getIdentifier().equals(identifier)) {
            head = head.getNextNode();
            size--;
            return;
        }

        Node prev = head;
        Node current = head.getNextNode();

        while (current != null) {
            if (current.transaction.getIdentifier().equals(identifier)) {
                prev.setNextNode(current.getNextNode());
                size--;
                return;
            }
            prev = current;
            current = current.getNextNode();
        }
        throw new TransactionNotFoundException("Transaction with ID " + identifier + " not found.");
    }


    @Override
    public Transaction[]    toArray(){
        Transaction[] transactionArray = new Transaction[size];

        Node current = head;
        int i = 0;
        while (current != null)
        {
            transactionArray[i++] = current.transaction;
            current = current.getNextNode();
        }
        return (transactionArray);
    }
}