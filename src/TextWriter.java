import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class TextWriter {

    public static void writeTextToFileByBufferedWriter(File file, String text) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
