package todo.port.out;

import todo.domain.item.Item;
import todo.domain.item.ItemNotFoundException;

import java.util.List;

public interface ItemRepository {
    List<Item> findAll();

    List<Item> findByUserId(int userId);

    List<Item> findByUserIdAndUndone(int userId);

    Item findByUserIdAndSeq(int userId, int todoSeq);

    void add(Item item);

    int countByUserId(int userId);

}
