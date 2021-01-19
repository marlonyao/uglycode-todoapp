package todo.application;

import todo.domain.User;
import todo.domain.UserLoginException;
import todo.domain.UserNotFoundException;
import todo.port.in.LoginUserUseCase;
import todo.port.out.UserRepository;

public class LoginUserCaseImpl implements LoginUserUseCase {
    private UserRepository userRepository;

    public LoginUserCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public int login(String username, String password) {
        User user = findUser(username);
        user.authenticate(password);
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
