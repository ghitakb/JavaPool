import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class AnalyzeFile {

    private final ParseSignatureFile signatureFile;

    public AnalyzeFile(ParseSignatureFile signatureFile) {
        this.signatureFile = signatureFile;
    }

    public String analyzeFirstBytes(String pathStr) throws IOException {
        Path path;
        try {
            path = Paths.get(pathStr);
        } catch (InvalidPathException e) {
            throw new IOException("Error: Invalid path.");
        }

        if (!Files.isRegularFile(path)) {
            throw new IOException("Error: The file does not exist.");
        }

        Map<String, byte[]> signatures = signatureFile.getSignatures();
        int maxLen = 0;
        for (byte[] sig : signatures.values()) {
            if (sig.length > maxLen) {
                maxLen = sig.length;
            }
        }

        if (maxLen == 0) {
            throw new IOException("Error: The signatures database is empty.");
        }

        byte[] fileBytes;
        try (FileInputStream fis = new FileInputStream(path.toFile())) {
            fileBytes = fis.readNBytes(maxLen);
        }

        String detectedType = null;
        int longestMatch = 0;

        for (Map.Entry<String, byte[]> entry : signatures.entrySet()) {
            String type = entry.getKey();
            byte[] sig = entry.getValue();

            if (fileBytes.length < sig.length) {
                continue;
            }

            boolean match = true;
            for (int i = 0; i < sig.length; i++) {
                if (fileBytes[i] != sig[i]) {
                    match = false;
                    break;
                }
            }

            if (match && sig.length > longestMatch) {
                longestMatch = sig.length;
                detectedType = type;
            }
        }

        return detectedType; // null = UNDEFINED
    }
}

