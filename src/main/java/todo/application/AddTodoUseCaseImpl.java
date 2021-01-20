package todo.application;

import todo.domain.item.Item;
import todo.domain.login.UserSession;
import todo.port.out.ItemRepository;
import todo.port.in.AddTodoUseCase;

public class AddTodoUseCaseImpl implements AddTodoUseCase {
    private ItemRepository itemRepository;

    public AddTodoUseCaseImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Item addItem(String todo) {
        Item item;
        if (UserSession.isLogin()) {
            int itemId = itemRepository.findByUserId(UserSession.currentUserId()).size() + 1;
            item = new Item(itemId, UserSession.currentUserId(), todo);
        } else {
            int itemId = itemRepository.count() + 1;
            item = new Item(itemId, todo);
        }
        itemRepository.add(item);
        return item;
    }
}
