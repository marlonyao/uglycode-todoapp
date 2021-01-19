package todo.bootstrap;

import com.google.common.annotations.VisibleForTesting;
import todo.adapter.out.FileSystemItemRepository;
import todo.application.AddTodoUseCaseImpl;
import todo.application.CompleteTodoUseCaseImpl;
import todo.application.ListTodoUseCaseImpl;
import todo.domain.item.Item;
import todo.port.in.AddTodoUseCase;
import todo.port.in.CompleteTodoUseCase;
import todo.port.in.ListTodoUseCase;
import todo.port.out.ItemRepository;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class ConsoleApplication {
    private static ItemRepository itemRepository;
    private static AddTodoUseCase addTodoUseCase;
    private static CompleteTodoUseCase completeTodoUseCase;
    private static ListTodoUseCase listTodoUseCase;

    public static void main(String[] args) {
        assembleApplication();
        processCommands();
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

    private static void assembleApplication() {
        if (itemRepository == null) {
            itemRepository = new FileSystemItemRepository(new File("todo.txt"));
        }
        addTodoUseCase = new AddTodoUseCaseImpl(itemRepository);
        completeTodoUseCase = new CompleteTodoUseCaseImpl(itemRepository);
        listTodoUseCase = new ListTodoUseCaseImpl(itemRepository);
    }

    private static Command parseCommand(String line) {
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
        abstract void execute();
    }

    static class AddCommand extends Command {
        private String todo;

        public AddCommand(String todo) {
            this.todo = todo;
        }

        @Override
        void execute() {
            Item item = addTodoUseCase.addItem(todo);
            System.out.printf("%s. %s%n", item.getId(), item.getTodo());
            System.out.printf("Item <%s> added%n", item.getId());
        }
    }

    private static class DoneCommand extends Command {
        private int itemId;

        public DoneCommand(int itemId) {
            this.itemId = itemId;
        }

        @Override
        void execute() {
            completeTodoUseCase.complete(itemId);
            System.out.printf("item <%s> done.%n", itemId);
        }
    }

    private static class ListTodoCommand extends Command {
        private boolean withAll;

        public ListTodoCommand(boolean withAll) {
            this.withAll = withAll;
        }

        @Override
        void execute() {
            List<Item> items = listTodoUseCase.list(withAll);
            for (Item item : items) {
                System.out.println(formatItem(item));
            }
            System.out.printf("Total: %s items%n", items.size());
        }

        private String formatItem(Item item) {
            String done = item.isDone() ? "[Done] " : "";
            return item.getId() + ". " + done + item.getTodo();
        }
    }
}
