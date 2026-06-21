import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;

public class ResultFile implements Closeable {

    private final FileWriter writer;

    public ResultFile(String pathStr) throws IOException {
        this.writer = new FileWriter(pathStr, false);
    }

    public void write(String type) throws IOException {
        writer.write(type + System.lineSeparator());
        writer.flush();
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }
}
