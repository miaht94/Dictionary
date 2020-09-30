package Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * The type Dictionary.
 */
public class Dictionary {
    private DBReader dbReader = DBReader.getInstance();
    private DictionarySearcher dictionarySearcher = new DictionarySearcher(dbReader);
    private static Dictionary dictionary = null;
    private Dictionary() {
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


    /**
     * Search word.
     *
     * @param target is a substring of each word in return list
     * @return the Word list contail substring target
     */
    public List<Word> searchWord(String target) {
        List<Word> words = new ArrayList<Word>();
        if (target == "") {
            return null;
        }
        final String query = "select * from 'definitions' where title LIKE '" + target + "%'";
        ResultSet rs;
        try {
            rs = dbReader.executeQuery(query);
            while (rs.next()) {
                String id = rs.getString("id");
                String title = rs.getString("title");
                String entry = rs.getString("entry");
                Word word = new Word(id, title, entry);
                words.add(word);
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return words;
    };

    /**
     * Search word.
     *
     * @param target             the target
     * @param expectedChangeList the list in which element be changed (add, remove, v.v...)
     */
    public void searchWord(String target, ObservableList<Word> expectedChangeList) {
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
        ResultSet rs = dbReader.getRows(index, amount);
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
