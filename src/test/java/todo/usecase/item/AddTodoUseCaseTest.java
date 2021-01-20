package todo.usecase.item;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import todo.adapter.out.MemoryItemRepository;
import todo.application.AddTodoUseCaseImpl;
import todo.domain.item.Item;
import todo.domain.login.UserSession;

import static org.assertj.core.api.Assertions.assertThat;

public class AddTodoUseCaseTest {
    private MemoryItemRepository itemRepository;
    private AddTodoUseCaseImpl addTodoUseCase;

    @BeforeEach
    void setUp() {
        itemRepository = new MemoryItemRepository();
        addTodoUseCase = new AddTodoUseCaseImpl(itemRepository);
    }

    @Test
    public void addTodo() {
        Item item1 = addTodoUseCase.addItem("<item1>");
        assertThat(item1).isEqualTo(new Item(1, "<item1>"));

        Item item2 = addTodoUseCase.addItem("<item2>");
        assertThat(item2).isEqualTo(new Item(2, "<item2>"));

        assertThat(itemRepository.findAll()).isEqualTo(ImmutableList.of(
                new Item(1, "<item1>"),
                new Item(2, "<item2>")
        ));
    }
    
    @Test
    public void shouldNotSeeTodoOfOtherUser() {
        UserSession.changeCurrentUserId(111);
        addTodoUseCase.addItem("<item111>");
        assertThat(itemRepository.findByUserId(111)).isEqualTo(ImmutableList.of(
                new Item(1, 111, "<item111>")
        ));

        UserSession.changeCurrentUserId(222);
        addTodoUseCase.addItem("<item222>");
        assertThat(itemRepository.findByUserId(222)).isEqualTo(ImmutableList.of(
                new Item(1, 222, "<item222>")
        ));
    }
}
