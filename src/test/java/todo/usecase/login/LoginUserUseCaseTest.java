package todo.usecase.login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import todo.adapter.out.MemoryUserRepository;
import todo.application.LoginUserUseCaseImpl;
import todo.domain.login.LoginSession;
import todo.domain.login.User;
import todo.domain.login.UserLoginException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LoginUserUseCaseTest {
    private MemoryUserRepository userRepository;
    private LoginUserUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        userRepository = new MemoryUserRepository();
        userRepository.add(new User(111, "testuser", "testpass"));
        useCase = new LoginUserUseCaseImpl(userRepository);
    }

    @Test
    public void loginSuccess() {
        int userId = useCase.login("testuser", "testpass");
        assertThat(userId).isEqualTo(111);
        assertThat(LoginSession.currentUserId()).isEqualTo(111);
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

//    @Test
//    public void logout() {
//        useCase.login("testuser", "testpass");
//        boolean result = useCase.logout("testuser");
//        assertThat(result).isTrue();
//    }
}
