package todo;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import todo.adapter.out.FakeItemRepository;
import todo.port.in.ListTodoUseCase;
import todo.application.ListTodoUseCaseImpl;
import todo.domain.Item;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ListTodoUseCaseTest {
    private FakeItemRepository itemRepository;
    private ListTodoUseCase todoUseCase;

    @BeforeEach
    void setUp() {
        itemRepository = new FakeItemRepository();
        itemRepository.add(new Item(1, "<item1>"));
        itemRepository.add(new Item(2, "<item2>", true));
        todoUseCase = new ListTodoUseCaseImpl(itemRepository);
    }

    @Test
    public void defaultListUndoneTodos() {
        List<Item> todos = todoUseCase.list(false);
        assertThat(todos).isEqualTo(ImmutableList.of(new Item(1, "<item1>")));
    }

    @Test
    public void listAllTodos() {
        List<Item> todos = todoUseCase.list(true);
        assertThat(todos).isEqualTo(ImmutableList.of(
                new Item(1, "<item1>"),
                new Item(2, "<item2>", true)
        ));
    }
}
