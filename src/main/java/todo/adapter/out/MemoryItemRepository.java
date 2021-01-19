package todo.adapter.out;

import todo.domain.Item;
import todo.domain.ItemNotFoundException;
import todo.port.out.ItemRepository;

import java.util.ArrayList;
import java.util.List;

public class MemoryItemRepository implements ItemRepository {
    private List<Item> items = new ArrayList<>();

    @Override
    public List<Item> findAll() {
        return items;
    }

    @Override
    public void add(Item item) {
        int itemIndex = findIndex(item.getId());
        if (itemIndex < 0) {
            items.add(item.copy());
            return;
        }
        items.set(itemIndex, item.copy());
    }

    private int findIndex(int itemId) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId() == itemId) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int count() {
        return items.size();
    }

    @Override
    public Item findById(int itemId) throws ItemNotFoundException {
        int itemIndex = findIndex(itemId);
        if (itemIndex < 0) {
            throw new ItemNotFoundException(itemId);
        }
        return items.get(itemIndex).copy();
    }

    @Override
    public List<Item> findUndone() {
        List<Item> result = new ArrayList<>();
        for (Item item : items) {
            if (!item.isDone()) {
                result.add(item.copy());
            }
        }
        return result;
    }
}
