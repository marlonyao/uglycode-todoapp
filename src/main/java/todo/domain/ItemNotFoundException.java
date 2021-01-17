package todo.domain;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(int itemId) {
        this(String.format("Item with id <%s> not found", itemId));
    }

    private ItemNotFoundException(String message) {
        super(message);
    }
}
