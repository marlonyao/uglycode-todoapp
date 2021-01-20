package todo.application;

import todo.domain.login.UserSession;
import todo.port.in.LogoutUserUseCase;

public class LogoutUserUseCaseImpl implements LogoutUserUseCase {
    @Override
    public boolean logout() {
        UserSession.logout();
        return true;
    }
}
