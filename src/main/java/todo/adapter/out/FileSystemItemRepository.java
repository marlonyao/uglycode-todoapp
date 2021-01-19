package todo.adapter.out;

import com.google.common.base.Charsets;
import todo.domain.Item;
import todo.domain.ItemNotFoundException;
import todo.domain.RepositoryException;
import todo.port.out.ItemRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;

public class FileSystemItemRepository implements ItemRepository {
    private MemoryItemRepository memoryItems = new MemoryItemRepository();
    private File dbFile;

    public FileSystemItemRepository(File dbFile) {
        this.dbFile = dbFile;
        if (!this.dbFile.exists()) {
            this.dbFile.getAbsoluteFile().getParentFile().mkdirs();
        } else {
            load();
        }
    }

    public FileSystemItemRepository(Path dbFile) {
        this(dbFile.toFile());
    }

    @Override
    public List<Item> findAll() {
        return memoryItems.findAll();
    }

    private void load() {
        try {
            for (String line : Files.readAllLines(dbFile.toPath(), Charsets.UTF_8)) {
                memoryItems.add(parseItem(line));
            }
        } catch (IOException e) {
            throw new RepositoryException(String.format("Fail to load file [%s]", dbFile), e);
        }
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
    public void add(Item item) {
        memoryItems.add(item);
        persist();
    }

    private void persist() {
        try (FileOutputStream fout = new FileOutputStream(dbFile, false)) {
            for (Item item : memoryItems.findAll()) {
                fout.write(String.format("%s,%s,%s\n", item.getId(), item.isDone(), item.getTodo()).getBytes());
            }
        } catch (IOException e) {
            throw new RepositoryException(String.format("Fail to persist items to file [%s]", dbFile), e);
        }
    }

    @Override
    public int count() {
        return memoryItems.count();
    }

    @Override
    public Item findById(int itemId) throws ItemNotFoundException {
        return memoryItems.findById(itemId);
    }
}
