package todo.adapter.out;

import todo.domain.item.Item;
import todo.domain.item.ItemNotFoundException;
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
        int itemIndex = findIndex(item.getUserId(), item.getId());
        if (itemIndex < 0) {
            items.add(item.copy());
            return;
        }
        items.set(itemIndex, item.copy());
    }

    private int findIndex(int userId, int itemId) {
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item.getUserId() == userId && item.getId() == itemId) {
                return i;
            }
        }
        return -1;
    }

    // TODO: 2021/1/21 要去掉
    private int oldFindIndex(int itemId) {
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item.getId() == itemId) {
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
    public int countByUserId(int userId) {
        return findByUserId(userId).size();
    }

    @Override
    public Item findById(int itemId) throws ItemNotFoundException {
        int itemIndex = oldFindIndex(itemId);
        if (itemIndex < 0) {
            throw new ItemNotFoundException(itemId);
        }
        return items.get(itemIndex).copy();
    }

    @Override
    public Item findByUserIdAndSeq(int userId, int todoId) {
        int itemIndex = findIndex(userId, todoId);
        if (itemIndex < 0) {
            // TODO: 2021/1/21 异常要传userId
            throw new ItemNotFoundException(todoId);
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
