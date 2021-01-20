package todo.domain.item;


import com.google.common.annotations.VisibleForTesting;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Item {
    private final int id;
    private int userId;
    private final String todo;
    private boolean done;

    public Item(int id, String todo) {
        this.id = id;
        this.todo = todo;
    }

    public Item(int id, int userId, String todo) {
        this.id = id;
        this.userId = userId;
        this.todo = todo;
    }

    @VisibleForTesting
    public Item(int id, String todo, boolean done) {
        this(id, todo);
        this.done = done;
    }

    @VisibleForTesting
    public Item(int id, int userId, String todo, boolean done) {
        this(id, todo);
        this.userId = userId;
        this.done = done;
    }

    public int getId() {
        return id;
    }

    public String getTodo() {
        return todo;
    }

    public boolean isDone() {
        return done;
    }

    public void complete() {
        done = true;
    }

    @VisibleForTesting
    public Item copy() {
        return new Item(id, userId, todo, done);
    }

    public int getUserId() {
        return userId;
    }
}
