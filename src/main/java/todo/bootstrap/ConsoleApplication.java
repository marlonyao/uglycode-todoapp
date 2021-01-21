package todo.bootstrap;

import com.google.common.annotations.VisibleForTesting;
import todo.adapter.out.FileSystemItemRepository;
import todo.adapter.out.FileSystemUserRepository;
import todo.adapter.out.MemoryUserRepository;
import todo.application.*;
import todo.domain.item.Item;
import todo.domain.login.User;
import todo.domain.login.UserSession;
import todo.port.in.*;
import todo.port.out.ItemRepository;
import todo.port.out.UserRepository;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class ConsoleApplication {
    private static ItemRepository itemRepository;
    private static AddTodoUseCase addTodoUseCase;
    private static CompleteTodoUseCase completeTodoUseCase;
    private static ListTodoUseCase listTodoUseCase;
    private static LoginUseCase loginUseCase;
    private static UserRepository userRepository;

    public static void main(String[] args) {
        try {
            assembleApplication();
            processCommands();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processCommands() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            processCmdline(line);
        }
    }

    private static void processCmdline(String line) {
        try {
            Command cmd = parseCommand(line);
            cmd.execute();
        } catch (InvalidCommandException e) {
            System.err.println(e.getMessage());
        } catch (Throwable e) {
            System.err.println("Execute error: " + e.getMessage());
        }
    }

    @VisibleForTesting
    public static void initItemRepository(ItemRepository itemRepository) {
        ConsoleApplication.itemRepository = itemRepository;
    }

    @VisibleForTesting
    public static void initUserRepository(MemoryUserRepository userRepository) {
        ConsoleApplication.userRepository = userRepository;
    }

    private static void assembleApplication() {
        if (itemRepository == null) {
            itemRepository = new FileSystemItemRepository(new File("todo.txt"));
        }
        if (userRepository == null) {
            userRepository = new FileSystemUserRepository(new File(System.getProperty("user.home"), ".todo-config"));
        }
        addTodoUseCase = new AddTodoUseCaseImpl(itemRepository);
        completeTodoUseCase = new CompleteTodoUseCaseImpl(itemRepository);
        listTodoUseCase = new ListTodoUseCaseImpl(itemRepository);
        loginUseCase = new LoginUseCaseImpl(userRepository);
    }

    private static Command parseCommand(String line) {
        if (line.startsWith("todo login -u ")) {
            return new LoginCommand(line.substring("todo login -u ".length()).trim());
        }
        if (line.trim().equals("todo logout")) {
            return new LogoutCommand();
        }
        if (line.startsWith("todo add ")) {
            return new AddCommand(line.substring("todo add ".length()).trim());
        }
        if (line.startsWith("todo done ")) {
            return new DoneCommand(Integer.parseInt(line.substring("todo done ".length()).trim()));
        }
        if (line.trim().equals("todo list")) {
            return new ListTodoCommand(false);
        }
        if (line.trim().equals("todo list --all")) {
            return new ListTodoCommand(true);
        }
        throw new InvalidCommandException("Unknown command [" + line + "]");
    }

    public static ItemRepository getItemRepository() {
        return itemRepository;
    }


    static abstract class Command {
        public void execute() {
            if (needLogin() && !UserSession.isLogin()) {
                System.err.println("Should login first!");
                return;
            }
            realExecute();
        }

        protected abstract void realExecute();

        protected boolean needLogin() {
            return true;
        }
    }

    static class AddCommand extends Command {
        private String todo;

        public AddCommand(String todo) {
            this.todo = todo;
        }

        @Override
        protected void realExecute() {
            Item item = addTodoUseCase.addItem(UserSession.currentUserId(), todo);
            System.out.printf("%s. %s%n", item.getSeq(), item.getTodo());
            System.out.printf("Item <%s> added%n", item.getSeq());
        }
    }

    private static class DoneCommand extends Command {
        private int itemSeq;

        public DoneCommand(int itemSeq) {
            this.itemSeq = itemSeq;
        }

        @Override
        protected void realExecute() {
            completeTodoUseCase.complete(UserSession.currentUserId(), itemSeq);
            System.out.printf("item <%s> done.%n", itemSeq);
        }
    }

    private static class ListTodoCommand extends Command {
        private boolean withAll;

        public ListTodoCommand(boolean withAll) {
            this.withAll = withAll;
        }

        @Override
        protected void realExecute() {
            List<Item> items = listTodoUseCase.list(UserSession.currentUserId(), withAll);
            for (Item item : items) {
                System.out.println(formatItem(item));
            }
            System.out.printf("Total: %s items%n", items.size());
        }

        private String formatItem(Item item) {
            String done = item.isDone() ? "[Done] " : "";
            return item.getSeq() + ". " + done + item.getTodo();
        }
    }

    private static class LoginCommand extends Command {
        private String username;

        public LoginCommand(String user) {
            this.username = user;
        }

        @Override
        protected void realExecute() {
            System.out.print("Password:");
            System.out.flush();
            Scanner scanner = new Scanner(System.in);
            String password = scanner.nextLine();
            User user = loginUseCase.login(username, password);
            UserSession.login(user);
            System.out.println("Login Success!");
        }

        @Override
        protected boolean needLogin() {
            return false;
        }
    }

    private static class LogoutCommand extends Command {
        @Override
        protected void realExecute() {
            UserSession.logout();
            System.out.println("Logout Success!");
        }

        @Override
        protected boolean needLogin() {
            return false;
        }
    }
}
