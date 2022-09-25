import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public final class TextReader {

    public static String[] readTextFromFileByBufferedReader(File file) {
        String line;
        String[] splitText = new String[2];
        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            line = reader.readLine();
            for (int j = 0;line != null;++j) {
                for (int i = Integer.parseInt(line); i > 0; i--) {
                    result.append(reader.readLine()).append("\n");
                }
                splitText[j] = result.toString();
                result.setLength(0);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return splitText;
    }
}
