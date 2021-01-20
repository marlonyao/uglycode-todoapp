package todo.domain.login;

import com.google.common.annotations.VisibleForTesting;

public class UserSession {
    private static int currentUserId;

    public static int currentUserId() {
        return currentUserId;
    }

    public static boolean isLogin() {
        return currentUserId > 0;
    }

    public static void login(User user) {
        currentUserId = user.getId();
    }

    public static void logout() {
        currentUserId = -1;
    }

    @VisibleForTesting
    public static void changeCurrentUserId(int userId) {
        currentUserId = userId;
    }

}
