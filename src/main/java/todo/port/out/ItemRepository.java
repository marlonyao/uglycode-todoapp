package todo.port.out;

import todo.domain.item.Item;
import todo.domain.item.ItemNotFoundException;

import java.util.List;

public interface ItemRepository {
    List<Item> findAll();

    List<Item> findUndone();

    void add(Item item);

    int count();

    Item findById(int itemId) throws ItemNotFoundException;
}
