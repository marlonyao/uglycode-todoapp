package todo.port.out;

import todo.domain.login.User;

public interface UserRepository {
    User findByUsername(String username);
}
