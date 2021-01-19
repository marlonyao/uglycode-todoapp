package todo.adapter.out;

import org.junit.jupiter.api.Test;
import todo.domain.login.UserNotFoundException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemoryUserRepositoryTest {
    @Test
    public void userNotFound() {
        MemoryUserRepository repository = new MemoryUserRepository();
        assertThatThrownBy(() -> repository.findByUsername("foo"))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User [foo] not found")
                ;
    }
}