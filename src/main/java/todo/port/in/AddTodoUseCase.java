package todo.port.in;

import todo.domain.item.Item;

public interface AddTodoUseCase {
    Item addItem(int userId, String todo);
}
