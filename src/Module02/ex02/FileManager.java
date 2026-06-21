import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class FileManager {

    private Path currentDirectory;

    public FileManager(String startPath) throws IOException {
        Path path;
        try {
            path = Paths.get(startPath);
        } catch (InvalidPathException e) {
            throw new IOException("Error: Invalid path.");
        }

        if (!path.isAbsolute()) {
            throw new IOException("Error: Current folder must be an absolute path.");
        }

        path = path.normalize();
        if (!Files.isDirectory(path)) {
            throw new IOException("Error: Current folder does not exist.");
        }

        this.currentDirectory = path;
    }

    public void ls() throws IOException {
        try (Stream<Path> stream = Files.list(currentDirectory)) {
            List<Path> entries = stream
                    .sorted(Comparator.comparing(path -> path.getFileName().toString()))
                    .collect(Collectors.toList());

            for (Path entry : entries) {
                System.out.println(entry.getFileName() + " " + calculateSizeKb(entry) + " KB");
            }
        }
    }

    public void cd(String pathStr) throws IOException {
        Path newPath = resolvePath(pathStr);

        if (!Files.isDirectory(newPath)) {
            throw new IOException("Error: Invalid directory.");
        }

        currentDirectory = newPath;
        System.out.println(currentDirectory);
    }

    public void mv(String what, String where) throws IOException {
        Path source = resolvePath(what);
        if (!Files.exists(source)) {
            throw new IOException("Error: Source does not exist.");
        }

        Path destination = resolvePath(where);
        if (Files.isDirectory(destination)) {
            destination = destination.resolve(source.getFileName());
        }

        if (source.equals(destination)) {
            throw new IOException("Error: Source and destination are identical.");
        }

        if (Files.exists(destination)) {
            throw new IOException("Error: Destination already exists.");
        }

        Path parent = destination.getParent();
        if (parent == null || !Files.isDirectory(parent)) {
            throw new IOException("Error: Destination directory does not exist.");
        }

        Files.move(source, destination);
    }

    public Path getCurrentDirectory() {
        return currentDirectory;
    }

    private Path resolvePath(String pathStr) throws IOException {
        try {
            Path path = Paths.get(pathStr);
            if (!path.isAbsolute()) {
                path = currentDirectory.resolve(path);
            }
            return path.normalize();
        } catch (InvalidPathException e) {
            throw new IOException("Error: Invalid path.");
        }
    }

    private long calculateSizeKb(Path path) throws IOException {
        long bytes = calculateSizeBytes(path);
        return bytes / 1024;
    }

    private long calculateSizeBytes(Path path) throws IOException {
        if (Files.isRegularFile(path)) {
            return Files.size(path);
        }

        if (!Files.isDirectory(path)) {
            return 0;
        }

        try (Stream<Path> stream = Files.list(path)) {
            long sum = 0;
            for (Path child : stream.collect(Collectors.toList())) {
                sum += calculateSizeBytes(child);
            }
            return sum;
        }
    }
}
