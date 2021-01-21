package todo.usecase.login;

import org.junit.jupiter.api.Test;
import todo.application.LogoutUseCaseImpl;
import todo.domain.login.UserSession;
import todo.domain.login.User;
import todo.port.in.LogoutUseCase;

import static org.assertj.core.api.Assertions.assertThat;

public class LogoutUseCaseTestCase {
    @Test
    public void logout() {
        User user = new User(111, "testuser", "testpass");
        LogoutUseCase useCase = new LogoutUseCaseImpl();
        UserSession.login(user);
        boolean result = useCase.logout();
        assertThat(result).isTrue();
        assertThat(UserSession.isLogin()).isFalse();
    }
}
