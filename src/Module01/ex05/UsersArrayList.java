import java.util.Arrays;


class UserNotFoundException extends Exception {
    public UserNotFoundException (String message) {
        super(message);
    }
}

public class UsersArrayList implements UsersList {
    private User[]  Users;
    private int     len;

    public UsersArrayList(){
        this.Users = new User[10];
        this.len = 0;
    }

    public User[] getUsers() {
        return (Users);
    }

    public void setUsers(User[] users) {
        Users = users;
    }

    public int getLen() {
        return (len);
    }

    public void setLen(int len) {
        this.len = len;
    }

    @Override
    public void addUser(User user) {
        if (Users.length == len)
        {
            User[] newUsers = new User[len + (len / 2)];
            System.arraycopy(Users, 0, newUsers, 0, len);
            Users = newUsers;
        }
        Users[len] = user;
        len++;
    }

    @Override
    public User retrieveById(Integer id) throws UserNotFoundException {
        for (int i = 0; i < len; i++) {
            User user = Users[i];
            if (user != null && user.getID().equals(id)) {
                return (user);
            }
        }
        throw new UserNotFoundException("User with ID " + id + " not found.");
    }

    @Override
    public User retrieveByIndex(int index) throws UserNotFoundException {
        if (index >= 0 && index < len)
            return (Users[index]);
        throw new UserNotFoundException ("User with index " + index + " not found.");
    }

    @Override
    public int getUsersNumber() {
        return (len);
    }

    @Override
    public String toString() {
        return (Arrays.toString(Users));
    }

}
