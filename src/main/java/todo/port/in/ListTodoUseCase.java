package todo.port.in;

import todo.domain.item.Item;

import java.util.List;

public interface ListTodoUseCase {
    List<Item> list(int userId, boolean withAll);
}
