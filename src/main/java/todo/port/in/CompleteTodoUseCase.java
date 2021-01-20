package todo.port.in;

public interface CompleteTodoUseCase {
    void complete(int userId, int todoId);
}
