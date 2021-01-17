package todo;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import todo.bootstrap.ConsoleApplication;
import todo.domain.Item;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;

public class ConsoleApplicationTest {
    private PipedOutputStream out;
    private PipedInputStream stdin;
    private PipedInputStream in;
    private PipedOutputStream stdout;
    private InputStream originStdin;
    private PrintStream originStdout;
    private Thread todoThread;

    @BeforeEach
    void setUp() throws IOException {
        setUpStreams();
        startToApp();
    }

    @AfterEach
    void tearDown() throws IOException, InterruptedException {
        tearDownStreams();
        stopTodoApp();
    }

    private void tearDownStreams() throws IOException {
        restoreStdIO();
        closeIO();
    }

    @Test
    public void todoApp() throws Exception {
        // 1. add todo
        assertInteract("todo add <item>\n", "1. <item>\nItem <1> added\n");
        assertThat(ConsoleApplication.getItemRepository().findAll()).isEqualTo(ImmutableList.of(
                new Item(1, "<item>")
        ));
        assertInteract("todo add <item2>\n", "2. <item2>\nItem <2> added\n");

        // 2. mark todo as done
        assertInteract("todo done 1\n", "item <1> done.\n");
        assertThat(ConsoleApplication.getItemRepository().findById(1).isDone()).isTrue();

        // 3. view undone todos
        assertInteract("todo list\n", "2. <item2>\nTotal: 1 items\n");

        // 4. view all todos
        assertInteract("todo list --all\n", "1. <item>\n2. <item2>\nTotal: 2 items\n");
    }

    private void assertInteract(String input, String output) throws IOException, InterruptedException {
        out.write(input.getBytes());
        out.flush();

        Thread.sleep(100);

        byte[] outBytes = new byte[in.available()];
        in.read(outBytes);
        assertThat(new String(outBytes)).isEqualTo(output);
    }

    private void setUpStreams() throws IOException {
        out = new PipedOutputStream();
        stdin = new PipedInputStream();
        stdout = new PipedOutputStream();
        in = new PipedInputStream();

        redirectStdIO();
    }

    private void redirectStdIO() throws IOException {
        originStdin = System.in;
        originStdout = System.out;
        out.connect(stdin);
        stdout.connect(in);
        System.setIn(stdin);
        System.setOut(new PrintStream(stdout));
    }

    private void startToApp() {
        Thread thread = new Thread(() -> ConsoleApplication.main(new String[0]));
        thread.start();
        todoThread = thread;
    }

    private void closeIO() throws IOException {
        out.close();
        in.close();
        stdin.close();
        stdout.close();
    }

    private void restoreStdIO() {
        System.setIn(originStdin);
        System.setOut(originStdout);
    }

    private void stopTodoApp() throws InterruptedException {
        todoThread.join();
    }
}
