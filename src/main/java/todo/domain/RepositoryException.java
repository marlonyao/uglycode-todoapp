package todo.domain;

public class RepositoryException extends RuntimeException {
    public RepositoryException(Throwable e) {
        super(e.getMessage(), e);
    }
}
