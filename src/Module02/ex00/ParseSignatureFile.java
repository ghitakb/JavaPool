import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class ParseSignatureFile {
    private final Path path;
    private final Map<String, byte[]> signatures = new HashMap<>();

    public ParseSignatureFile(String pathStr) throws IOException {
        try {
            this.path = Paths.get(pathStr);
        } catch (InvalidPathException e) {
            throw new IOException("Error: Invalid path.");
        }

        if (!Files.isRegularFile(this.path)) {
            throw new IOException("Error: The signatures file does not exist.");
        }
    }

    public void parseFile() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                storeMap(line, lineNumber);
                lineNumber++;
            }
        }

        if (signatures.isEmpty()) {
            throw new IOException("Error: The signatures file is empty.");
        }
    }

    private void storeMap(String line, int lineNumber) throws IOException {
        if (line.trim().isEmpty()) {
            return;
        }

        String[] parts = line.split(",", 2);
        if (parts.length != 2) {
            throw new IOException("Error: Invalid signature format at line " + lineNumber + ".");
        }

        String type = parts[0].trim();
        String sigStr = parts[1].trim();
        if (type.isEmpty() || sigStr.isEmpty()) {
            throw new IOException("Error: Invalid signature format at line " + lineNumber + ".");
        }

        String[] hexValues = sigStr.split("\\s+");
        byte[] sigBytes = new byte[hexValues.length];

        for (int i = 0; i < hexValues.length; i++) {
            if (!hexValues[i].matches("[0-9A-Fa-f]{2}")) {
                throw new IOException("Error: Invalid hex value at line " + lineNumber + ".");
            }
            sigBytes[i] = (byte) Integer.parseInt(hexValues[i], 16);
        }

        signatures.put(type, sigBytes);
    }

    public Map<String, byte[]> getSignatures() {
        return signatures;
    }
}
