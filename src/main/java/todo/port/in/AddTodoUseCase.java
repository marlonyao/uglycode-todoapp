package todo.port.in;

import todo.domain.Item;

public interface AddTodoUseCase {
    Item addItem(String todo);
}
