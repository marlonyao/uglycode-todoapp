package todo.application;

import todo.domain.login.UserSession;
import todo.domain.login.User;
import todo.domain.login.UserLoginException;
import todo.domain.login.UserNotFoundException;
import todo.port.in.LoginUserUseCase;
import todo.port.out.UserRepository;

public class LoginUserUseCaseImpl implements LoginUserUseCase {
    private UserRepository userRepository;

    public LoginUserUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public int login(String username, String password) {
        User user = findUser(username);
        user.authenticate(password);
        UserSession.login(user);
        return user.getId();
    }

    private User findUser(String username) {
        try {
            return userRepository.findByUsername(username);
        } catch (UserNotFoundException e) {
            throw new UserLoginException(String.format("User [%s] not exists", username), e);
        }
    }
}
