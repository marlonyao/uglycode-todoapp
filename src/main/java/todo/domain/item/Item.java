package todo.domain.item;


import com.google.common.annotations.VisibleForTesting;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Item {
    private int userId;
    private final int seq;
    private final String todo;
    private boolean done;

    public Item(int userId, int seq, String todo) {
        this.seq = seq;
        this.userId = userId;
        this.todo = todo;
    }

    public Item(int userId, int seq, String todo, boolean done) {
        this(userId, seq, todo);
        this.done = done;
    }

    public int getSeq() {
        return seq;
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
        return new Item(userId, seq, todo, done);
    }

    public int getUserId() {
        return userId;
    }
}
