package todo.port.in;

import todo.domain.login.User;

public interface LoginUseCase {
    User login(String username, String password);
}
