package Model;

import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DictionaryUtils {

    public static <E> void listToObservableList(List<E> src, ObservableList<E> des) {
        des.clear();
        des.addAll(src);
    }

    static int mostLeftBinarySearch(List<Word> words, Word target) {
        if (words.size() < 1) return -1;
        int L = 0;
        int R = words.size() - 1;
        while (L < R) {
            int M = (L + R) / 2;
            if (target.compareTo(words.get(M)) > 0) L = M + 1;
            else R = M;
        }
        if (words.get(L).getTitle().toLowerCase().contains(target.getTitle().toLowerCase()))
            return L;
        else return -1;
    }

    static int mostRightBinarySearch(List<Word> words, Word target) {
        if (words.size() < 1) return -1;
        int L = 0;
        int R = words.size();
        while (L < R) {
            int M = (L + R) / 2;
            if (target.compareTo(words.get(M)) < 0) R = M;
            else L = M + 1;
        }
        try {
            if (words.get(R - 1).getTitle().toLowerCase().contains(target.getTitle().toLowerCase()))
                return R - 1;
            else return -1;
        } catch (ArrayIndexOutOfBoundsException e) {
            return -1;
        }
    }

    public static void main(String[] args) {
        List<Word> a = new ArrayList<>();
        a.add(new Word("abc"));
        a.add(new Word("acd"));
        a.add(new Word("Aeb"));
        a.add(new Word("AEE"));
        a.add(new Word("a2"));
        //mostLeftBinarySearch(a, new Word("a"));
        a.sort(Word.getStandardComparator());
        System.out.println(a.get(mostRightBinarySearch(a, new Word("a"))).getTitle());
    }
}
