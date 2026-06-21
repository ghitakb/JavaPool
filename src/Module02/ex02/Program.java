import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Program {

    public static void main(String[] args) {

        final String prefix = "--current-folder=";
        if (args.length != 1 || !args[0].startsWith(prefix)) {
            System.err.println("Usage: --current-folder=ABSOLUTE_PATH");
            return;
        }

        String startPath = args[0].substring(prefix.length());

        try {
            FileManager fm = new FileManager(startPath);
            System.out.println(fm.getCurrentDirectory());

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                while (true) {
                    System.out.print("-> ");
                    String input = reader.readLine();

                    if (input == null) {
                        break;
                    }

                    String[] parts = input.trim().split("\\s+");
                    if (parts.length == 0 || parts[0].isEmpty()) {
                        continue;
                    }

                    try {
                        switch (parts[0]) {
                            case "ls":
                                requireArgs(parts, 1);
                                fm.ls();
                                break;

                            case "cd":
                                requireArgs(parts, 2);
                                fm.cd(parts[1]);
                                break;

                            case "mv":
                                requireArgs(parts, 3);
                                fm.mv(parts[1], parts[2]);
                                break;

                            case "exit":
                                requireArgs(parts, 1);
                                return;

                            default:
                                System.err.println("Error: Unknown command.");
                        }
                    } catch (IllegalArgumentException | IOException e) {
                        System.err.println(e.getMessage());
                    }
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void requireArgs(String[] parts, int expected) {
        if (parts.length != expected) {
            throw new IllegalArgumentException("Error: Invalid command arguments.");
        }
    }
}
