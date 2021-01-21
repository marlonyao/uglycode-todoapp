package todo.adapter.out;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import todo.domain.login.User;
import todo.domain.login.UserNotFoundException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static utils.FileUtils.*;

public class FileSystemUserRepositoryTest {
    @TempDir
    Path tempDir;
    private File userFile;
    private FileSystemUserRepository userRepository;

    @BeforeEach
    void setUp() throws IOException {
        userFile = tempDir.resolve("users.txt").toFile();
        writeLines(userFile, ImmutableList.of("1,testuser,testpass"));
        userRepository = new FileSystemUserRepository(userFile);
    }

    @Test
    public void find() {
        assertThat(userRepository.findByUsername("testuser")).isEqualTo(
                new User(1, "testuser", "testpass"));

        assertThatThrownBy(() -> userRepository.findByUsername("foobar"))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User [foobar] not found");
    }


}
