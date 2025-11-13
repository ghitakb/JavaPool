public class Program {
    public static void main(String[] args) {
        try {
            User alice = new User("Alice", 1000);
            User bob = new User("Bob", 2000);
            User john = new User("john", 300);
            User mike = new User("mike",700);

            UsersArrayList usersList = new UsersArrayList();
            usersList.addUser(alice);
            usersList.addUser(bob);
            usersList.addUser(john);
            usersList.addUser(mike);

            System.out.println("Users list:");
            System.out.println(usersList);

            System.out.println("\nuser with ID = 1\n" + usersList.retrieveById(1));
            System.out.println("\nuser with index = 1\n" + usersList.retrieveByIndex(1));
            System.out.println("\nnumber of total Users\n" + usersList.getUsersNumber());
        }
        catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}
