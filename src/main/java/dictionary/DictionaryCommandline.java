package dictionary;

import java.util.stream.IntStream;

public class DictionaryCommandline {
    public static void showAllWords() {
        Dictionary dict = Dictionary.getInstance();
        System.out.println(String.format("%1$-10s| %2$-" + dict.getLengthMax() + "s | %3$s", "NO", "English", "Vietnamese"));
        IntStream.range(0, dict.getWords().size()).
                forEach(i -> {
                    System.out.println(String.format("%1$-10d| %2$-" + Math.max(dict.getLengthMax(), 8) + "s| %3$s", i, dict.getWords().get(i).getWord_target(), dict.getWords().get(i).getWord_explain()));
                });
    }
    public static void dictionaryBasic() {
        DictionaryManagement.insertFromCommandline();
        DictionaryCommandline.showAllWords();
    }
}
