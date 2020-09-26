package Model;

import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
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
    private List<Word> words = new ArrayList<>();
    private DBReader dbReader = DBReader.getInstance();
    private static Dictionary dictionary = null;
    private Dictionary() {
        this.words = this.searchWord("");
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
     * @param target is a substring of each word in return list
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
        List resultList = searchWord(target);
        for (Object w: resultList) {
            expectedChangeList.add((Word) w);
        }
    }
}
