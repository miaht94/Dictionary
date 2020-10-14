package Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * The type Dictionary.
 * Singleton Design Partern
 */
public class Dictionary {
//    private DBReader dbReader = DBReader.getInstance(DictionaryType.EN_VI);
    public static DictionaryType currType = DictionaryType.EN_VI;
    public DictionarySearcher dictionarySearcher = new DictionarySearcher(DBReader.getInstance(currType), Configuration.getDictRange(currType));
    private static Dictionary dictionary = null;
    public Map<DictionaryType, DictionarySearcher> dictionarySearchers = new HashMap<>();

    private Dictionary() {
        for (DictionaryType type : DictionaryType.values()) {
            this.dictionarySearchers.put(type, new DictionarySearcher(DBReader.getInstance(type),Configuration.getDictRange(type)));
        }
    }

    /**
     * Gets instance of Dictionary
     *
     * @return the instance
     */
    public static Dictionary getInstance() {
        if (Dictionary.dictionary == null) dictionary = new Dictionary();
        return Dictionary.dictionary;
    }

    public static Dictionary getInstance(DictionaryType type) {
        if (Dictionary.dictionary == null) dictionary = new Dictionary();
        currType = type;
        dictionary.dictionarySearcher = dictionary.dictionarySearchers.get(type);
        return Dictionary.dictionary;
    }

    /**
     * Search word.
     *
     * @param target             the target
     * @param expectedChangeList the list in which element be changed (add, remove, v.v...)
     */
    public void searchWord(String target, ObservableList<Word> expectedChangeList) {
        target = target.toLowerCase();
        expectedChangeList.clear();
        List<Word> resultList = dictionarySearcher.search(target);
        if (resultList != null) {
//            for (Object w : resultList) {
//                expectedChangeList.add((Word) w);
//            }
            DictionaryUtils.listToObservableList(resultList, expectedChangeList);
            expectedChangeList.sort(Word.getStandardComparator());
        }
    }

    public List<Word> getWordsFromDB(int index, int amount) {
        ResultSet rs = DBReader.getInstance(currType).getRows(index, amount);
        List<Word> returnList =  new ArrayList<>();
        try {
            while (rs.next()) {
                returnList.add(new Word(rs.getString("title")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnList;
    }

    public static void main(String[] args) {
        List<Word> sampleList = new ArrayList<Word>();
        sampleList.add(new Word("1", "aaa", "cacac"));
        sampleList.add(new Word("1", "BB", "cacac"));
        sampleList.add(new Word("1", "aaaaa", "cacac"));
        sampleList.add(new Word("1", "b", "cacac"));
        sampleList.add(new Word("1", "bb", "cacac"));
        ObservableList<Word> sample = FXCollections.observableArrayList(sampleList);
        sample.sort(Word.getStandardComparator());
        System.out.println("Done");
    }
}
