package todo.usecase.item;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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

    @Nested
    class SingleUser {
        private int userId = 111;

        @Test
        public void addTodo() {
            Item item1 = addTodoUseCase.addItem(userId, "<item1>");
            assertThat(item1).isEqualTo(new Item(1, userId, "<item1>"));

            Item item2 = addTodoUseCase.addItem(userId, "<item2>");
            assertThat(item2).isEqualTo(new Item(2, userId, "<item2>"));

            assertThat(itemRepository.findAll()).isEqualTo(ImmutableList.of(
                    createItem(1, "<item1>"),
                    createItem(2, "<item2>")
            ));
        }

        private Item createItem(int itemId, String todo) {
            return new Item(itemId, userId, todo);
        }
    }

    @Nested
    class MultiUser {
        @Test
        public void shouldNotSeeTodoOfOtherUser() {
            addTodoUseCase.addItem(111, "<item111>");
            assertThat(itemRepository.findByUserId(111)).isEqualTo(ImmutableList.of(
                    new Item(1, 111, "<item111>")
            ));

            addTodoUseCase.addItem(222, "<item222>");
            assertThat(itemRepository.findByUserId(222)).isEqualTo(ImmutableList.of(
                    new Item(1, 222, "<item222>")
            ));
        }
    }
}
