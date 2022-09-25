public final class TextHandler {

    public static String[] splitToWords(String sentence) {
        return sentence.split(" ");
    }

    public static String[] splitToSentences(String splitText) {
        return splitText.split("\n");
    }
}
