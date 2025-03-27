import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        List<String> builtins = builtins();
        String dir = Path.of("").toAbsolutePath().toString();

        while (true) {
            System.out.print("$ ");
            String input = scanner.nextLine();
            List<String> tokens = parseInput(input);

            if (tokens.isEmpty()) {
                continue;
            }

            String command = tokens.get(0);
            String[] arguments = tokens.subList(1, tokens.size()).toArray(new String[0]);

            switch (command) {
                case "exit":
                    if (arguments.length > 0 && arguments[0].equals("0")) {
                        System.exit(0);
                    } else {
                        System.out.println(input + ": command not found");
                    }
                    break;
                case "echo":
                    System.out.println(String.join(" ", arguments));
                    break;
                case "type":
                    if (builtins.contains(arguments[0])) {
                        System.out.println(arguments[0] + " is a shell builtin");
                    } else {
                        String path = getPath(arguments[0]);
                        if (path != null) {
                            System.out.println(arguments[0] + " is " + path);
                        } else {
                            System.out.println(arguments[0] + ": not found");
                        }
                    }
                    break;
                case "pwd":
                    System.out.println(dir);
                    break;
                case "cd":
                    if (arguments.length == 0) {
                        System.out.println("cd: missing argument");
                        break;
                    }
                    String cd = arguments[0];
                    if (!cd.startsWith("/")) {
                        if (!cd.equals("~")) {
                            cd = dir + "/" + arguments[0];
                        } else {
                            cd = System.getenv("HOME");
                        }
                    }

                    if (Files.isDirectory(Path.of(cd))) {
                        dir = Path.of(cd).normalize().toString();
                    } else {
                        System.out.println("cd: " + cd + ": No such file or directory");
                    }
                    break;
                case "cat":
                    // Fix: Concatenate file contents instead of printing line-by-line
                    StringBuilder content = new StringBuilder();
                    for (String fileName : arguments) {
                        Path filePath = Path.of(fileName);
                        if (Files.exists(filePath)) {
                            content.append(Files.readString(filePath, StandardCharsets.UTF_8));
                        } else {
                            System.out.println("cat: " + fileName + ": No such file or directory");
                            return;
                        }
                    }
                    System.out.print(content.toString()); // Print as single string
                    break;
                default:
                    String path = getPath(command);
                    if (path != null) {
                        String[] fullPath = new String[tokens.size()];
                        fullPath[0] = command;
                        System.arraycopy(arguments, 0, fullPath, 1, arguments.length);
                        ProcessBuilder processBuilder = new ProcessBuilder(fullPath);
                        processBuilder.directory(new File(dir));
                        processBuilder.environment().putAll(System.getenv());
                        Process process = processBuilder.start();
                        process.getInputStream().transferTo(System.out);
                    } else {
                        System.out.println(command + ": command not found");
                    }
            }
        }
    }

    private static String getPath(String parameter) {
        for (String path : System.getenv("PATH").split(":")) {
            Path fullPath = Path.of(path, parameter);
            if (Files.isRegularFile(fullPath)) {
                return fullPath.toString();
            }
        }
        return null;
    }

    private static List<String> builtins() {
        return Arrays.asList("exit", "echo", "type", "pwd", "cd", "cat");
    }

    private static List<String> parseInput(String input) {
        List<String> tokens = new ArrayList<>();
        boolean inQuotes = false;
        boolean escapeNext = false;
        StringBuilder currentToken = new StringBuilder();

        for (char c : input.toCharArray()) {
            if (escapeNext) {
                currentToken.append(c);
                escapeNext = false;
            } else if (c == '\\') {
                escapeNext = true;
            } else if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ' ' && !inQuotes) {
                if (currentToken.length() > 0) {
                    tokens.add(currentToken.toString());
                    currentToken.setLength(0);
                }
            } else {
                currentToken.append(c);
            }
        }

        if (currentToken.length() > 0) {
            tokens.add(currentToken.toString());
        }

        return tokens;
    }
}
