package todo.application;

import todo.domain.login.UserSession;
import todo.port.in.LogoutUseCase;

public class LogoutUseCaseImpl implements LogoutUseCase {
    @Override
    public boolean logout() {
        UserSession.logout();
        return true;
    }
}
