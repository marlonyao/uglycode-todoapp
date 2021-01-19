package todo.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import todo.adapter.out.MemoryItemRepository;
import todo.port.in.CompleteTodoUseCase;
import todo.application.CompleteTodoUseCaseImpl;
import todo.domain.Item;
import todo.domain.ItemNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CompleteTodoUseCaseTest {
    private MemoryItemRepository itemRepository;
    private CompleteTodoUseCase todoUseCase;

    @BeforeEach
    void setUp() {
        itemRepository = new MemoryItemRepository();
        todoUseCase = new CompleteTodoUseCaseImpl(itemRepository);
    }

    @Test
    public void itemExists() {
        itemRepository.add(new Item(1, "<item1>"));
        todoUseCase.complete(1);
        assertThat(itemRepository.findById(1).isDone()).isEqualTo(true);
    }

    @Test
    public void itemNotExists() {
        assertThatThrownBy(() -> todoUseCase.complete(1))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Item with id <1> not found");
    }
}
