public class UserIdsGenerator {

    private static UserIdsGenerator Instance;
    int  count = 0;

    private UserIdsGenerator () {};

    public static UserIdsGenerator getInstance()
    {
        if (Instance == null)
            Instance = new UserIdsGenerator();
        return (Instance);
    }

    public int generateId()
    {
        return (++count);
    }
}