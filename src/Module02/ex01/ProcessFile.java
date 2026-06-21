import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ProcessFile {

    private static final Pattern WORD_PATTERN = Pattern.compile("\\p{L}+");

    private final Path filePath;

    public ProcessFile(String path) throws IOException {

        try {
            this.filePath = Paths.get(path);
        }
        catch (InvalidPathException e) {
            throw new IOException("Error: Invalid path.");
        }

        if(!Files.isRegularFile(filePath)) {
            throw new IOException("Error: File does not exist.");
        }
    }


    public void addWordsTo(Set<String> dictionary) throws IOException {
        try(BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;

            while((line = reader.readLine()) != null) {
                Matcher matcher = WORD_PATTERN.matcher(line.toLowerCase(Locale.ROOT));
                while(matcher.find()) {
                    dictionary.add(matcher.group());
                }
            }
        }
    }

    public int[] buildVector(Map<String, Integer> dictionary) throws IOException {
        int[] vector = new int[dictionary.size()];

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;

            while ((line = reader.readLine()) != null) {
                Matcher matcher = WORD_PATTERN.matcher(line.toLowerCase(Locale.ROOT));
                while (matcher.find()) {
                    String word = matcher.group();
                    Integer index = dictionary.get(word);
                    if (index != null) {
                        vector[index]++;
                    }
                }
            }
        }
        return vector;
    }
}
