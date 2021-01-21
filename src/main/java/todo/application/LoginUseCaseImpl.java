package todo.application;

import todo.domain.login.UserSession;
import todo.domain.login.User;
import todo.domain.login.UserLoginException;
import todo.domain.login.UserNotFoundException;
import todo.port.in.LoginUseCase;
import todo.port.out.UserRepository;

public class LoginUseCaseImpl implements LoginUseCase {
    private UserRepository userRepository;

    public LoginUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User login(String username, String password) {
        User user = findUser(username);
        user.authenticate(password);
        return user;
    }

    private User findUser(String username) {
        try {
            return userRepository.findByUsername(username);
        } catch (UserNotFoundException e) {
            throw new UserLoginException(String.format("User [%s] not exists", username), e);
        }
    }
}
