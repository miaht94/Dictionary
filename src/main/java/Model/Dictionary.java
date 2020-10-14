package Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
    public static Map<DictionaryType, List<Word>> addedWordList = new HashMap<>();

    private Dictionary() {
        for (DictionaryType type : DictionaryType.values()) {
            addedWordList.put(type, new ArrayList<>());
            this.dictionarySearchers.put(type, new DictionarySearcher(DBReader.getInstance(type), Configuration.getDictRange(type)));
        }
        readAddedWord();
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
            int count = index;
            while (rs.next()) {
                returnList.add(new Word(count, rs.getString("title")));
                count++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnList;
    }

    public void addWord(Word word) {
        //word.setID(-1);
        //word.setId("User - " + currType.toString() + " " + addedWordList.get(currType).size());
        addedWordList.get(currType).add(word);
    }

    public void delWord(Word word) {
        if (word.getID() == -1 && word.getId() == null) System.out.println("Argument ko hop le");
        else if (word.getID() == -1) {
            for (Word temp : addedWordList.get(currType)) {
                if (temp.getId().equals(word.getId())) {
                    addedWordList.get(currType).remove(temp);
                    break;
                }
            }
        } else {
            int index = word.getID();
            DBReader.getInstance(currType).delRow(index);
        }
    }

    public static void appendAddedWordToFile() {
        File file = new File("./UserAdded.txt");
        try {
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            for (DictionaryType type: DictionaryType.values()) {
                List<Word> tempList = addedWordList.get(type);
                for (Word word: tempList) {
                    fw.write(type.toString() + "\t" + word.getTitle() + "\t" + word.getXML() + "\n");
                }
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readAddedWord() {
        File file = new File("./UserAdded.txt");
        try {
            file.createNewFile();
            Scanner sc = new Scanner(file);
            int count_1 = 0;
            int count_2 = 0;
            while (sc.hasNext()) {
                String line = sc.nextLine();
                String[] info = line.split("\t");
                if (info.length > 0) {
                    switch (info[0]) {
                        case "EN_VI" :
                            addedWordList.get(DictionaryType.EN_VI).add(new Word("User - EN_VI " + count_1, info[1], info[2]));
                            count_1++;
                            break;
                        case "VI_EN" :
                            addedWordList.get(DictionaryType.VI_EN).add(new Word("User - VI_EN " + count_2, info[1], info[2]));
                            count_2++;
                            break;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
