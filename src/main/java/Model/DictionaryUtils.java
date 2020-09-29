package Model;

import javafx.collections.ObservableList;

import java.util.List;

public class DictionaryUtils {
    public static <E> void listToObservableList(List<E> src, ObservableList<E> des) {
        des.clear();
        for (E item : src) {
            des.add(item);
        }
    }
}
