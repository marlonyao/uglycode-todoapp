package todo.domain;

public class PersistException extends RuntimeException {
    public PersistException(String message, Throwable cause) {
        super(message, cause);
    }
}
