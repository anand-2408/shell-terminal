import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> builtins = builtins();

        while (true) {
            System.out.print("$ ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) continue;

            String[] parts = input.split(" ");
            String command = parts[0];
            String[] parameters = new String[parts.length - 1];
            System.arraycopy(parts, 1, parameters, 0, parts.length - 1);

            if (command.equals("exit")) {
                if (parameters.length > 0 && parameters[0].equals("0")) {
                    System.exit(0);
                } else {
                    System.out.println("exit: command not found");
                }
            } else if (command.equals("echo")) {
                System.out.println(String.join(" ", parameters));
            } else if (command.equals("type")) {
                handleTypeCommand(parameters, builtins);
            } else {
                executeCommand(command, parameters);
            }
        }
    }

    private static void handleTypeCommand(String[] parameters, List<String> builtins) {
        if (parameters.length == 0) {
            System.out.println("type: missing operand");
            return;
        }

        String parameter = parameters[0];
        if (builtins.contains(parameter)) {
            System.out.println(parameter + " is a shell builtin");
        } else {
            String path = getPath(parameter);
            if (path != null) {
                System.out.println(parameter + " is " + path);
            } else {
                System.out.println(parameter + ": not found");
            }
        }
    }

    private static void executeCommand(String command, String[] parameters) {
        String path = getPath(command);
        if (path != null) {
            try {
                String[] commandWithParams = new String[parameters.length + 1];
                commandWithParams[0] = path;
                System.arraycopy(parameters, 0, commandWithParams, 1, parameters.length);

                Process process = new ProcessBuilder(commandWithParams).inheritIO().start();
                process.waitFor();
            } catch (IOException | InterruptedException e) {
                System.out.println(command + ": error executing command");
            }
        } else {
            System.out.println(command + ": command not found");
        }
    }

    private static String getPath(String command) {
        for (String dir : System.getenv("PATH").split(":")) {
            Path fullPath = Path.of(dir, command);
            if (Files.isExecutable(fullPath)) {
                return fullPath.toString();
            }
        }
        return null;
    }

    private static List<String> builtins() {
        List<String> builtins = new ArrayList<>();
        builtins.add("exit");
        builtins.add("echo");
        builtins.add("type");
        return builtins;
    }
}
