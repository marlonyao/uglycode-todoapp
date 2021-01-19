package todo.application;

import todo.domain.item.Item;
import todo.port.out.ItemRepository;
import todo.port.in.ListTodoUseCase;

import java.util.List;

public class ListTodoUseCaseImpl implements ListTodoUseCase {
    private ItemRepository itemRepository;

    public ListTodoUseCaseImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public List<Item> list(boolean withAll) {
        if (withAll) {
            return itemRepository.findAll();
        }
        return itemRepository.findUndone();
    }
}
