package dictionary;

import java.util.ArrayList;

// Singleton
public class Dictionary {
    private ArrayList<Word> words = new ArrayList<>();
    private static Dictionary singletonInstance;
    private int lengthMax = 0;
    private Dictionary() {

    };

    public void addWord(Word word) {
        this.words.add(word);
        this.lengthMax = Math.max(word.getWord_explain().length(), this.lengthMax);
        this.lengthMax = Math.max(word.getWord_target().length(), this.lengthMax);
    }

    public ArrayList<Word> getWords() {
        return words;
    }

    public static Dictionary getInstance() {
        if (Dictionary.singletonInstance == null) {
            Dictionary.singletonInstance = new Dictionary();
        }
        return Dictionary.singletonInstance;
    }

    public int getLengthMax() {
        return lengthMax;
    }

    public String stringFormatPrinting(int index) {
        return String.format("%1$-10d| %2$-" + Math.max(this.getLengthMax(), 8) + "s| %3$s", index, this.getWords().get(index).getWord_target(), this.getWords().get(index).getWord_explain());
    }

    // Testing
    public static void main(String[] args) {
        Dictionary dict = Dictionary.getInstance();
        DictionaryManagement.insertFromCommandline();
        DictionaryCommandline.showAllWords();
    }
}
