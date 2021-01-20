package todo.adapter.out;

import todo.domain.item.Item;
import todo.domain.item.ItemNotFoundException;
import todo.domain.RepositoryException;
import todo.port.out.ItemRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;
import static utils.FileUtils.*;

public class FileSystemItemRepository implements ItemRepository {
    private MemoryItemRepository memoryItems = new MemoryItemRepository();
    private File dbFile;

    public FileSystemItemRepository(File dbFile) {
        try {
            this.dbFile = dbFile;
            makeParentDirsIfNecessary(this.dbFile);
            doLoad();
        } catch (IOException e) {
            throw new RepositoryException(e);
        }
    }

    public FileSystemItemRepository(Path dbFile) {
        this(dbFile.toFile());
    }

    @Override
    public List<Item> findAll() {
        return memoryItems.findAll();
    }

    private void doLoad() throws IOException {
        if (!dbFile.exists()) {
            return;
        }
        readLines(dbFile).forEach(line -> memoryItems.add(parseItem(line)));
    }

    private Item parseItem(String line) {
        String[] parts = line.split(",", 3);
        return new Item(parseInt(parts[0]), parts[2], parseBoolean(parts[1]));
    }

    @Override
    public List<Item> findUndone() {
        return memoryItems.findUndone();
    }

    @Override
    public List<Item> findByUserId(int userId) {
        return null;
    }

    @Override
    public void add(Item item) {
        memoryItems.add(item);
        persist();
    }

    private void persist() {
        List<String> lines = memoryItems.findAll().stream().map(this::formatItem)
                .collect(Collectors.toList());
        try {
            writeLines(dbFile, lines);
        } catch (IOException e) {
            throw new RepositoryException(e);
        }
    }

    private String formatItem(Item item) {
        return String.format("%s,%s,%s", item.getId(), item.isDone(), item.getTodo());
    }

    @Override
    public int count() {
        return memoryItems.count();
    }

    @Override
    public int countByUserId(int userId) {
        return 0;
    }

    @Override
    public Item findById(int itemId) throws ItemNotFoundException {
        return memoryItems.findById(itemId);
    }
}
