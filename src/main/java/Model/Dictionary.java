package Model;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import static java.lang.System.currentTimeMillis;

/**
 * The type Dictionary.
 */
public class Dictionary {
    private List<Word> words = new ArrayList<>();
    private DBReader dbReader = DBReader.getInstance();
    private static Dictionary dictionary = null;
    private Dictionary() {
        long count = currentTimeMillis();
        try {
            this.words = dbReader.getAllColumn("title");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            dbReader.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        this.words.sort(Word.getStandardComparator());
        System.out.println("Init Time : " + (System.currentTimeMillis() - count));
        try {
            DBReader.getInstance().close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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


    /**
     * Gets List of stored Word in Dictionary (Read from DB).
     *
     * @return the words atribute
     */
    public List<Word> getWords() {
        return words;
    }

    /**
     * Search word.
     *
     * @param target is a substring of each word in return listde
     * @return the Word list contail substring target
     */
    public List<Word> searchWord(String target) {
        List<Word> words = new ArrayList<Word>();
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
        List<Word> resultList = searchWord(target);
        for (Object w: resultList) {
            expectedChangeList.add((Word) w);
        }
        expectedChangeList.sort(Word.getStandardComparator());
    }

    private List<Word> searchWordFromMemory(String target) {
        List<Word> resultSet = new ArrayList<>();
        int firstIndex = DictionaryUtils.customBinarySearch(this.words, new Word("",target,""));
        if (firstIndex != -1) {
            while (getInstance().getWords().get(firstIndex).getTitle().toLowerCase().contains(target))
                resultSet.add(getInstance().getWords().get(firstIndex++));
        }
        resultSet.sort(Word.getStandardComparator());
        return resultSet;
    }

    /**
     * Search word from memory.
     *
     * @param target             the target
     * @param expectedChangeList the list in which element be changed (add, remove, v.v...)
     */
    public void searchWordFromMemory(String target, ObservableList<Word> expectedChangeList) {
        expectedChangeList.clear();
        if (target != null && !target.equals("")) {
            target = target.toLowerCase();
            List<Word> resultList = searchWordFromMemory(target);
            expectedChangeList.addAll(resultList);
        }
    }

    public Word getWordFromDB(int index) {
        ResultSet rs = null;
        try {
            rs = dbReader.executeQuery("SELECT title From definitions where _rowid_ = " + index);
            rs.next();
            return new Word("",rs.getString("title") , "");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
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
