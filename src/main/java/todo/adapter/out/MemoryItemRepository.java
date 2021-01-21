package todo.adapter.out;

import todo.domain.item.Item;
import todo.domain.item.ItemNotFoundException;
import todo.port.out.ItemRepository;

import java.util.ArrayList;
import java.util.List;

public class MemoryItemRepository implements ItemRepository {
    private List<Item> items = new ArrayList<>();

    public List<Item> findAll() {
        return items;
    }

    @Override
    public void add(Item item) {
        int itemIndex = findIndex(item.getUserId(), item.getSeq());
        if (itemIndex < 0) {
            items.add(item.copy());
            return;
        }
        items.set(itemIndex, item.copy());
    }

    private int findIndex(int userId, int itemSeq) {
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item.getUserId() == userId && item.getSeq() == itemSeq) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int countByUserId(int userId) {
        return findByUserId(userId).size();
    }

    @Override
    public Item findByUserIdAndSeq(int userId, int todoSeq) {
        int itemIndex = findIndex(userId, todoSeq);
        if (itemIndex < 0) {
            throw new ItemNotFoundException(userId, todoSeq);
        }
        return items.get(itemIndex).copy();
    }

    @Override
    public List<Item> findByUserIdAndUndone(int userId) {
        List<Item> result = new ArrayList<>();
        for (Item item : items) {
            if (item.getUserId() == userId && !item.isDone()) {
                result.add(item.copy());
            }
        }
        return result;
    }

    @Override
    public List<Item> findByUserId(int userId) {
        List<Item> result = new ArrayList<>();
        for (Item item : items) {
            if (item.getUserId() == userId) {
                result.add(item.copy());
            }
        }
        return result;
    }

}
