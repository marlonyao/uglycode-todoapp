package todo.adapter.out;

import todo.domain.login.User;
import todo.port.out.UserRepository;
import utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class FileSystemUserRepository implements UserRepository {
    List<User> users = new ArrayList<>();

    public FileSystemUserRepository(File userFile) throws IOException {
        List<String> lines = FileUtils.readLines(userFile);
        for (String line : lines) {
            users.add(parseUser(line));
        }
    }

    private User parseUser(String line) {
        String[] parts = line.split(",", 3);
        User user = new User(Integer.parseInt(parts[0]), parts[1], parts[2]);
        return user;
    }

    @Override
    public User findByUsername(String username) {
        return users.stream()
                .filter(byName(username))
                .findFirst()
                .get();
    }

    private Predicate<User> byName(String username) {
        return user -> user.getName().equals(username);
    }
}
