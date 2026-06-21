import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Program {

    public static void main(String[] args) {
        try {
            ParseSignatureFile database = new ParseSignatureFile("signatures.txt");
            database.parseFile();

            AnalyzeFile analyzer = new AnalyzeFile(database);

            try (BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
                 ResultFile writer = new ResultFile("result.txt")) {
                while (true) {
                    System.out.print("-> ");
                    String input = stdin.readLine();

                    if (input == null) {
                        break;
                    }

                    if (input.equals("42")) {
                        break;
                    }

                    try {
                        String type = analyzer.analyzeFirstBytes(input);
                        if (type != null) {
                            writer.write(type);
                        }
                        System.out.println("PROCESSED");
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }
}
