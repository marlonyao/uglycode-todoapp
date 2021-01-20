package todo.domain.item;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(int userId, int itemSeq) {
        this(String.format("Item not found: userId=[%s], itemSeq=[%s]", userId, itemSeq));
    }

    private ItemNotFoundException(String message) {
        super(message);
    }
}
