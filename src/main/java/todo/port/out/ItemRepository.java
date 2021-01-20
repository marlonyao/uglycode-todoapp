package todo.port.out;

import todo.domain.item.Item;
import todo.domain.item.ItemNotFoundException;

import java.util.List;

public interface ItemRepository {
    List<Item> findAll();

    List<Item> findUndone();

    List<Item> findByUserId(int userId);

    List<Item> findByUserIdAndUndone(int userId);

    void add(Item item);

    int count();

    int countByUserId(int userId);

    Item findById(int itemId) throws ItemNotFoundException;

    Item findByUserIdAndSeq(int userId, int todoId);

}
