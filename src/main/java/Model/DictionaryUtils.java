package Model;

import javafx.collections.ObservableList;

import java.util.List;

public class DictionaryUtils {

    public static <E> void listToObservableList(List<E> src, ObservableList<E> des) {
        des.clear();
        des.addAll(src);
    }

    static int customBinarySearch(List<Word> words, Word target) {
        int L = 0;
        int R = words.size() - 1;
        while (L < R) {
            int M = (L + R) / 2;
            if (target.compareTo(Dictionary.getInstance().getWords().get(M)) > 0) L = M + 1;
            else R = M;
        }
        if (Dictionary.getInstance().getWords().get(L).getTitle().toLowerCase().contains(target.getTitle().toLowerCase()))
            return L;
        else return -1;
    }
}
