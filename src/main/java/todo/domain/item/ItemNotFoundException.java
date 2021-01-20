package todo.domain.item;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(int userId, int itemId) {
        this(String.format("Item not found: userId=[%s], itemId=[%s]", userId, itemId));
    }

    private ItemNotFoundException(String message) {
        super(message);
    }
}
