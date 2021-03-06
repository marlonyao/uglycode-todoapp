package todo;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import todo.adapter.out.MemoryItemRepository;
import todo.adapter.out.MemoryUserRepository;
import todo.bootstrap.ConsoleApplication;
import todo.domain.item.Item;
import todo.domain.login.User;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;

public class ConsoleApplicationTest {
    private PipedOutputStream stdinWriter;
    private PipedInputStream stdin;
    private PipedInputStream stdoutReader;
    private PipedOutputStream stdout;
    private PipedInputStream stderrReader;
    private PipedOutputStream stderr;
    private InputStream originStdin;
    private PrintStream originStdout;
    private PrintStream originStderr;
    private Thread todoThread;
    private int userId;

    @BeforeEach
    void setUp() throws IOException {
        setUpStreams();
        startTodoApp();
    }

    @AfterEach
    void tearDown() throws IOException, InterruptedException {
        tearDownStreams();
        stopTodoApp();
    }


    @Test
    public void todoApp() throws Exception {
        // 0. login
        assertInteract("todo login -u testuser\n", "Password:");
        assertInteract("testpass\n", "Login Success!\n");

        // 1. add todo
        assertInteract("todo add <item>\n", "1. <item>\nItem <1> added\n");
        assertThat(ConsoleApplication.getItemRepository().findByUserId(userId)).isEqualTo(ImmutableList.of(
                new Item(userId, 1, "<item>")
        ));
        assertInteract("todo add <item2>\n", "2. <item2>\nItem <2> added\n");

        // 2. mark todo as done
        assertInteract("todo done 1\n", "item <1> done.\n");
        assertThat(ConsoleApplication.getItemRepository().findByUserIdAndSeq(userId, 1).isDone()).isTrue();

        // 3. view undone todos
        assertInteract("todo list\n", "2. <item2>\nTotal: 1 items\n");

        // 4. view all todos
        assertInteract("todo list --all\n", "1. [Done] <item>\n2. <item2>\nTotal: 2 items\n");

        assertInteract("foobar\n", "", "Unknown command [foobar]\n");
        
        // 5. logout
        assertInteract("todo logout\n", "Logout Success!\n");

        // 6. operate again
        assertInteract("todo list\n", "", "Should login first!\n");
        assertInteract("todo list --all\n", "", "Should login first!\n");
        assertInteract("todo add foo\n", "", "Should login first!\n");
        assertInteract("todo done 1\n", "", "Should login first!\n");
    }

    private void assertInteract(String input, String output) throws IOException, InterruptedException {
        assertInteract(input, output, "");
    }

    private void assertInteract(String input, String output, String errorOutput) throws IOException, InterruptedException {
        stdinWriter.write(input.getBytes());
        stdinWriter.flush();

        Thread.sleep(100);

        byte[] outBytes = new byte[stdoutReader.available()];
        stdoutReader.read(outBytes);
        assertThat(new String(outBytes)).isEqualTo(output);

        byte[] errBytes = new byte[stderrReader.available()];
        stderrReader.read(errBytes);
        assertThat(new String(errBytes)).isEqualTo(errorOutput);
    }

    private void setUpStreams() throws IOException {
        stdinWriter = new PipedOutputStream();
        stdin = new PipedInputStream();
        stdout = new PipedOutputStream();
        stdoutReader = new PipedInputStream();
        stderr = new PipedOutputStream();
        stderrReader = new PipedInputStream();

        redirectStdIO();
    }

    private void redirectStdIO() throws IOException {
        // pipe stream 安排：stdinWriter -> stdin -> [program] -> stdout -> stdoutReader
        //                                                    -> stderr -> stderrReader
        originStdin = System.in;
        originStdout = System.out;
        originStderr = System.err;
        stdinWriter.connect(stdin);
        stdout.connect(stdoutReader);
        stderr.connect(stderrReader);
        System.setIn(stdin);
        System.setOut(new PrintStream(stdout));
        System.setErr(new PrintStream(stderr));
    }

    private void startTodoApp() {
        ConsoleApplication.initItemRepository(new MemoryItemRepository());
        MemoryUserRepository userRepository = new MemoryUserRepository();
        userId = 222;
        userRepository.add(new User(userId, "testuser", "testpass"));
        ConsoleApplication.initUserRepository(userRepository);
        Thread thread = new Thread(() -> ConsoleApplication.main(new String[0]));
        thread.start();
        todoThread = thread;
    }

    private void closeIO() throws IOException {
        stdinWriter.close();
        stdoutReader.close();
        stderrReader.close();
        stdin.close();
        stdout.close();
        stderr.close();
    }

    private void tearDownStreams() throws IOException {
        restoreStdIO();
        closeIO();
    }

    private void restoreStdIO() {
        System.setIn(originStdin);
        System.setOut(originStdout);
        System.setErr(originStderr);
    }

    private void stopTodoApp() throws InterruptedException {
        todoThread.join();
    }
}
