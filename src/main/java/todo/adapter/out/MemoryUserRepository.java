package todo.adapter.out;

import todo.domain.User;
import todo.domain.UserNotFoundException;
import todo.port.out.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class MemoryUserRepository implements UserRepository {
    private List<User> users = new ArrayList<>();

    public void add(User user) {
        users.add(user);
    }

    @Override
    public User findByUsername(String username) {
        for (User user : users) {
            if (user.getName().equals(username)) {
                return user;
            }
        }
        throw new UserNotFoundException(String.format("User [%s] not found", username));
    }
}
