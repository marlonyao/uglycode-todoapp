package todo.usecase.item;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import todo.adapter.out.MemoryItemRepository;
import todo.port.in.CompleteTodoUseCase;
import todo.application.CompleteTodoUseCaseImpl;
import todo.domain.item.Item;
import todo.domain.item.ItemNotFoundException;

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

    @Nested
    class SingleUser {
        private int userId;

        @BeforeEach
        void setUp() {
            userId = 111;
        }

        @Test
        public void itemExists() {
            itemRepository.add(createItem(1, "<item1>"));
            todoUseCase.complete(userId, 1);
            assertThat(itemRepository.findByUserIdAndSeq(userId, 1).isDone()).isEqualTo(true);
        }

        @Test
        public void itemNotExists() {
            assertThatThrownBy(() -> todoUseCase.complete(userId, 1))
                    .isInstanceOf(ItemNotFoundException.class)
                    .hasMessage("Item not found: userId=[111], itemId=[1]");
        }

        private Item createItem(int itemId, String todo) {
            return new Item(itemId, userId, todo);
        }
    }

    @Nested
    class MultiUser {
        @Test
        public void complete() {
            itemRepository.add(new Item(1, 111, "<item1>"));
            itemRepository.add(new Item(1, 222, "<item2>"));
            todoUseCase.complete(222, 1);
            assertThat(itemRepository.findAll()).isEqualTo(ImmutableList.of(
                    new Item(1, 111, "<item1>", false),
                    new Item(1, 222, "<item2>", true)
            ));
        }
    }
}
