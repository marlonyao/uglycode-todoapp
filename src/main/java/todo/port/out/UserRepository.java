package todo.port.out;

import todo.domain.User;

public interface UserRepository {
    User findByUsername(String username);
}
