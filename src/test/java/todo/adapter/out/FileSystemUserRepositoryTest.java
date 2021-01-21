package todo.adapter.out;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import todo.domain.login.User;
import utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class FileSystemUserRepositoryTest {
    @TempDir
    Path tempDir;
    
    @Test
    public void find() throws IOException {
        File userFile = tempDir.resolve("users.txt").toFile();
        FileUtils.writeLines(userFile, ImmutableList.of("1,testuser,testpass"));

        FileSystemUserRepository userRepository = new FileSystemUserRepository(userFile);
        assertThat(userRepository.findByUsername("testuser")).isEqualTo(
                new User(1, "testuser", "testpass"));
    }
}
