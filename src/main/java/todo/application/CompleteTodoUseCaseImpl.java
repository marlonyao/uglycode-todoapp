package todo.application;

import todo.domain.item.Item;
import todo.port.out.ItemRepository;
import todo.port.in.CompleteTodoUseCase;

public class CompleteTodoUseCaseImpl implements CompleteTodoUseCase {
    private ItemRepository itemRepository;

    public CompleteTodoUseCaseImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public void complete(int userId, int todoSeq) {
        Item item = itemRepository.findByUserIdAndSeq(userId, todoSeq);
        item.complete();
        itemRepository.add(item);
    }
}
