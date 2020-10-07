package dictionary;

import java.util.stream.IntStream;

public class DictionaryCommandline {
    public static void showAllWords() {
        Dictionary dict = Dictionary.getInstance();
        System.out.println(String.format("%1$-10s| %2$-" + dict.getLengthMax() + "s | %3$s", "NO", "English", "Vietnamese"));
        IntStream.range(0, dict.getWords().size()).
                forEach(i -> {
                    System.out.println(dict.stringFormatPrinting(i));
                });
    }

    public static void dictionaryBasic() {
        DictionaryManagement.insertFromCommandline();
        DictionaryCommandline.showAllWords();
    }

    public static void dictionaryAdvanced() {
        DictionaryManagement.insertFromFile();
        DictionaryCommandline.showAllWords();
        DictionaryManagement.lookUp();
    }
}
