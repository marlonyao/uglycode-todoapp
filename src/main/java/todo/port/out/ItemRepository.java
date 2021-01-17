package todo.port.out;

import todo.domain.Item;
import todo.domain.ItemNotFoundException;

import java.util.List;

public interface ItemRepository {
    List<Item> findAll();

    List<Item> findUndone();

    void add(Item item);

    int count();

    Item findById(int itemId) throws ItemNotFoundException;
}
