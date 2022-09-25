import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class Matcher {

    public static String getResultString(String[] splitText) {
        String[] sentences1 = TextHandler.splitToSentences(splitText[0]);
        String[] sentences2 = TextHandler.splitToSentences(splitText[1]);
        if (sentences1.length >= sentences2.length) {
            return mapToString(sentences2, sentences1);
        } else {
            return mapToString(sentences1, sentences2);
        }
    }

    private static String mapToString(String[] sentences1, String[] sentences2) {
        return getSentencesMatchMap(sentences2, sentences1).entrySet().stream()
                .map(e -> e.getValue().equals("?") ? String.format("%s:%s", e.getKey(), e.getValue()) : String.format("%s:%s", e.getValue(), e.getKey()))
                .reduce(((s1, s2) -> String.format("%s\n%s", s1, s2))).orElse("");
    }

    private static Map<String, String> getSentencesMatchMap(String[] sentences1, String[] sentences2) {
        Map<String, String> matchesMap = new HashMap<>();
        Map<String, Integer> bestMatchesContainer = new HashMap<>();

        for (String sentence1 : sentences1) {
            Map<String, Integer> sentencesWithMatchLevel = new HashMap<>();
            for (String sentence2 : sentences2) {
                int sentenceMatchLevel = defineSentencesMatchLevel(TextHandler.splitToWords(sentence1), TextHandler.splitToWords(sentence2));
                sentencesWithMatchLevel.put(sentence2, sentenceMatchLevel);
            }
            Map<String, Integer> bestMatch = getBestMatch(sentencesWithMatchLevel);
            String key = getKey(bestMatch);
            Integer value = getValue(bestMatch);

            findMatches(matchesMap, bestMatchesContainer, sentence1, key, value);
        }
        findMatchesByNumberOfCharacters(sentences2, matchesMap);
        return matchesMap;
    }

    private static void findMatchesByNumberOfCharacters(String[] sentences2, Map<String, String> matchesMap) {
        for (String sentence : sentences2) {
            if (!matchesMap.containsKey(sentence)) {
                String[] sentences = matchesMap.entrySet().stream()
                        .filter(e -> e.getValue().equals("?"))
                        .map(Map.Entry::getKey)
                        .toArray(String[]::new);
                Optional<String> match = getBestMatchSentenceByNumberOfCharacters(sentence, sentences);
                if (match.isPresent()) {
                    matchesMap.put(sentence, match.get());
                    matchesMap.remove(match.get());
                }
            }
        }
    }

    private static void findMatches(Map<String, String> matchesMap, Map<String, Integer> bestMatchesContainer, String sentence1, String key, Integer value) {
        if (bestMatchesContainer.containsKey(key) && bestMatchesContainer.get(key) < value) {
            bestMatchesContainer.put(key, value);
            matchesMap.put(matchesMap.get(key), "?");
            matchesMap.put(key, sentence1);
        } else if (bestMatchesContainer.containsKey(key) && bestMatchesContainer.get(key) >= value) {
            matchesMap.put(sentence1, "?");
        } else {
            bestMatchesContainer.put(key, value);
            matchesMap.put(key, sentence1);
        }
    }

    private static Map<String, Integer> getBestMatch(Map<String, Integer> sentencesWithMatchLevel) {
        return sentencesWithMatchLevel.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue))
                .stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static String getKey(Map<String, Integer> bestMatch) {
        return bestMatch.keySet().stream().findFirst().orElse("");
    }

    private static Integer getValue(Map<String, Integer> bestMatch) {
        return bestMatch.values().stream().findFirst().orElse(0);
    }

    private static Optional<String> getBestMatchSentenceByNumberOfCharacters(String sentence, String[] sentences) {
        Map<String, Integer> wordWithMatchLevel = new HashMap<>();
        for (String s : sentences) {
            wordWithMatchLevel.put(s, Math.abs(s.length() - sentence.length()));
        }
        return wordWithMatchLevel.entrySet().stream().min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    private static int defineSentencesMatchLevel(String[] words1, String[] words2) {
        int result = 0;
        for (String word1 : words1) {
            for (String word2 : words2) {
                int wordMatchLevel = defineWordsMatchLevel(word1, word2);
                if (wordMatchLevel != Integer.MIN_VALUE) {
                    result += wordMatchLevel;
                }
            }
        }
        return result;
    }

    private static int defineWordsMatchLevel(String word1, String word2) {
        if (word1.length() == word2.length() && word1.equals(word2)) {
            return word1.length();
        } else {
            StringBuilder cutWord = new StringBuilder();
            cutWord.append(word1);
            for (int i = word1.toCharArray().length - 1; i > 0; i--) {
                String[] split = word2.split(cutWord.toString());
                if (split.length > 1 || word2.equals(cutWord.toString())) {
                    String cutWordStr = cutWord.toString();
                    return cutWordStr.length() - (word1.length() - cutWordStr.length() + word2.length() - cutWordStr.length());
                } else {
                    cutWord.deleteCharAt(i);
                }
            }
        }
        return Integer.MIN_VALUE;
    }
}
