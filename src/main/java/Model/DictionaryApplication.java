package Model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class DictionaryApplication {
    // Just for test
//    public static void main(String[] args) {
//        Dictionary app = Dictionary.getInstance();
//        System.out.println("Done");
//        List<Word> wordList = new ArrayList<>();
////        wordList.add(Dictionary.getInstance().getWords().get(20));
////        wordList.add(Dictionary.getInstance().getWords().get(21));
////        wordList.add(Dictionary.getInstance().getWords().get(22));
////        wordList.add(Dictionary.getInstance().getWords().get(23));
////        wordList.add(Dictionary.getInstance().getWords().get(24));
//        ObservableList<Word> list = FXCollections.observableList(wordList);
//        list.addListener(new ListChangeListener<Word>() {
//            @Override
//            public void onChanged(Change<? extends Word> c) {
//                System.out.println("List Changed");
//                c.next();
//                System.out.println("Sublist Added : " + c.getAddedSubList().toString());
//            }
//        });
//        app.searchWord("one",list);
//        DBReader dbreader = DBReader.getInstance();
//        try {
//            dbreader.getConnection().close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//    }
}
