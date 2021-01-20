package todo.usecase.item;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import todo.adapter.out.MemoryItemRepository;
import todo.port.in.ListTodoUseCase;
import todo.application.ListTodoUseCaseImpl;
import todo.domain.item.Item;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ListTodoUseCaseTest {
    private MemoryItemRepository itemRepository;
    private ListTodoUseCase todoUseCase;

    @BeforeEach
    void setUp() {
        itemRepository = new MemoryItemRepository();
        itemRepository.add(new Item(1, 111, "<item1>"));
        itemRepository.add(new Item(2, 111, "<item2>", true));
        itemRepository.add(new Item(1, 222, "<item22>"));
        todoUseCase = new ListTodoUseCaseImpl(itemRepository);
    }

    @Test
    public void defaultListUndoneTodos() {
        List<Item> todos = todoUseCase.list(111, false);
        assertThat(todos).isEqualTo(ImmutableList.of(new Item(1, 111, "<item1>")));
    }

    @Test
    public void listAllTodos() {
        List<Item> todos = todoUseCase.list(111, true);
        assertThat(todos).isEqualTo(ImmutableList.of(
                new Item(1, 111, "<item1>"),
                new Item(2, 111, "<item2>", true)
        ));
    }
}
