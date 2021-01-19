package todo.usecase;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import todo.adapter.out.FileSystemItemRepository;
import todo.domain.item.Item;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FileSystemItemRepositoryTest {
    @TempDir
    Path tempDir;
    private Path dbfile;
    private FileSystemItemRepository repository;

    @BeforeEach
    void setUp() {
        dbfile = tempDir.resolve("todo.txt");
        repository = new FileSystemItemRepository(dbfile);
    }

    @AfterEach
    void tearDown() {
        dbfile.toFile().delete();
    }

    @Test
    public void add() throws IOException {
        repository.add(new Item(1, "<item1>"));
        repository.add(new Item(2, "<item2>"));

        List<String> content = Files.readLines(dbfile.toFile(), Charsets.UTF_8);
        assertThat(content)
                .hasSize(2)
                .contains("1,false,<item1>")
                .contains("2,false,<item2>");
    }

    @Test
    public void update() throws IOException {
        repository.add(new Item(1, "<item1>"));
        repository.add(new Item(1, "<item11>"));
        List<String> content = Files.readLines(dbfile.toFile(), Charsets.UTF_8);
        assertThat(content)
                .hasSize(1)
                .contains("1,false,<item11>");
    }

    @Test
    public void find() throws IOException {
        try (BufferedWriter writer = Files.newWriter(dbfile.toFile(), Charsets.UTF_8)) {
            writer.write("1,false,<item1>\n");
            writer.write("2,true,<item2>\n");
        }
        repository = new FileSystemItemRepository(dbfile);

        assertThat(repository.findAll()).isEqualTo(ImmutableList.of(
                new Item(1, "<item1>"),
                new Item(2, "<item2>", true)
        ));
        assertThat(repository.count()).isEqualTo(2);
        assertThat(repository.findUndone()).isEqualTo(ImmutableList.of(
                new Item(1, "<item1>")));
        assertThat(repository.findById(1)).isEqualTo(new Item(1, "<item1>"));
    }
}
