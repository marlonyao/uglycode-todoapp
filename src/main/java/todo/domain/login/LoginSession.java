package todo.domain.login;

public class LoginSession {
    private static int currentUserId;

    public static int currentUserId() {
        return currentUserId;
    }

    public static void switchTo(User user) {
        currentUserId = user.getId();
    }
}
