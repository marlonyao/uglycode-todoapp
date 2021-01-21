package todo.usecase.login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import todo.adapter.out.MemoryUserRepository;
import todo.application.LoginUseCaseImpl;
import todo.domain.login.User;
import todo.domain.login.UserLoginException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LoginUseCaseTest {
    private MemoryUserRepository userRepository;
    private LoginUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        userRepository = new MemoryUserRepository();
        userRepository.add(new User(111, "testuser", "testpass"));
        useCase = new LoginUseCaseImpl(userRepository);
    }

    @Test
    public void loginSuccess() {
        User user = useCase.login("testuser", "testpass");
        assertThat(user.getId()).isEqualTo(111);
    }

    @Test
    public void loginFail_WrongPassword() {
        assertThatThrownBy(() -> useCase.login("testuser", "wrong password"))
                .isInstanceOf(UserLoginException.class)
                .hasMessage("Wrong password");
    }

    @Test
    public void loginFail_UserNotExsits() {
        assertThatThrownBy(() -> useCase.login("wronguser", "testpass"))
                .isInstanceOf(UserLoginException.class)
                .hasMessage("User [wronguser] not exists");
    }

}
