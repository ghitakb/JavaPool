public interface UsersList {
    void    addUser(User user);
    User    retrieveById(Integer id) throws UserNotFoundException;
    User    retrieveByIndex(int index) throws UserNotFoundException;
    int     getUsersNumber();
}
