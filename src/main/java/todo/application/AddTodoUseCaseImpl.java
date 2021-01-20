package todo.application;

import todo.domain.item.Item;
import todo.port.in.AddTodoUseCase;
import todo.port.out.ItemRepository;

public class AddTodoUseCaseImpl implements AddTodoUseCase {
    private ItemRepository itemRepository;

    public AddTodoUseCaseImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Item addItem(int userId, String todo) {
        int itemId = itemRepository.countByUserId(userId) + 1;
        Item item = new Item(itemId, userId, todo);
        itemRepository.add(item);
        return item;
    }
}
