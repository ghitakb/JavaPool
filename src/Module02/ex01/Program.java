import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


public class Program {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Error: Expected 2 file names as input.");
            return;
        }

        try {
            ProcessFile fileA = new ProcessFile(args[0]);
            ProcessFile fileB = new ProcessFile(args[1]);

            Set<String> words = new TreeSet<>();
            fileA.addWordsTo(words);
            fileB.addWordsTo(words);

            Map<String, Integer> dictionary = buildDictionary(words);
            writeDictionary(dictionary);

            int[] vectorA = fileA.buildVector(dictionary);
            int[] vectorB = fileB.buildVector(dictionary);

            double similarity = SimilarityCalculator.cosine(vectorA, vectorB);
            System.out.printf("Similarity = %.2f%n", truncateTwoDecimals(similarity));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private static Map<String, Integer> buildDictionary(Set<String> words) {
        Map<String, Integer> dictionary = new LinkedHashMap<>();
        for (String word : words) {
            dictionary.put(word, dictionary.size());
        }
        return dictionary;
    }

    private static void writeDictionary(Map<String, Integer> dictionary) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("dictionary.txt"))) {
            writer.write(String.join(", ", dictionary.keySet()));
            writer.newLine();
        }
    }

    private static double truncateTwoDecimals(double value) {
        return Math.floor(value * 100) / 100;
    }
}
