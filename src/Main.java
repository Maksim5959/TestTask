import java.io.File;

public class Main {

    private static final String INPUT_FILE_PATH = "files/input.txt";
    private static final String OUTPUT_FILE_PATH = "files/output.txt";

    public static void main(String[] args) {
        String resultString = Matcher.getResultString(TextReader.readTextFromFileByBufferedReader(new File(INPUT_FILE_PATH)));
        TextWriter.writeTextToFileByBufferedWriter(new File(OUTPUT_FILE_PATH), resultString);
    }
}
