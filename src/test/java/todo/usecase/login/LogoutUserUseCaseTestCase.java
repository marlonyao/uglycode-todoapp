package todo.usecase.login;

import org.junit.jupiter.api.Test;
import todo.application.LogoutUserUseCaseImpl;
import todo.domain.login.UserSession;
import todo.domain.login.User;
import todo.port.in.LogoutUserUseCase;

import static org.assertj.core.api.Assertions.assertThat;

public class LogoutUserUseCaseTestCase {
    @Test
    public void logout() {
        User user = new User(111, "testuser", "testpass");
        LogoutUserUseCase useCase = new LogoutUserUseCaseImpl();
        UserSession.login(user);
        boolean result = useCase.logout();
        assertThat(result).isTrue();
        assertThat(UserSession.isLogin()).isFalse();
    }
}
