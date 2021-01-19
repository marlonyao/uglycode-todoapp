package todo.domain;

public class User {
    private final int userId;
    private final String username;
    private final String password;

    public User(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return userId;
    }

    public String getName() {
        return username;
    }

    public void authenticate(String password) {
        if (!this.password.equals(password)) {
            throw new UserLoginException("Wrong password");
        }
    }
}
