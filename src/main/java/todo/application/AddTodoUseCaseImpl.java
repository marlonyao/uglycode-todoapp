package todo.application;

import todo.domain.item.Item;
import todo.port.out.ItemRepository;
import todo.port.in.AddTodoUseCase;

public class AddTodoUseCaseImpl implements AddTodoUseCase {
    private ItemRepository itemRepository;

    public AddTodoUseCaseImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Item addItem(String todo) {
        int itemId = itemRepository.count() + 1;
        Item item = new Item(itemId, todo);
        itemRepository.add(item);
        return item;
    }
}
