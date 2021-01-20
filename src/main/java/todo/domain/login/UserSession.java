package todo.domain.login;

public class UserSession {
    private static int currentUserId;

    public static int currentUserId() {
        return currentUserId;
    }

    public static void login(User user) {
        currentUserId = user.getId();
    }

    public static void logout() {
        currentUserId = -1;
    }
}
