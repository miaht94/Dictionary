package Model;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;


public class DictionaryUtils {
    static int customBinarySearch(List<Word> words, Word target) {
=======
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
>>>>>>> origin
        int L = 0;
        int R = words.size() - 1;
        while (L < R) {
            int M = (L + R) / 2;
<<<<<<< HEAD
            if (target.compareTo(Dictionary.getInstance().getWords().get(M)) > 0) L = M + 1;
            else R = M;
        }
        if (Dictionary.getInstance().getWords().get(L).getTitle().toLowerCase().contains(target.getTitle().toLowerCase()))
        return L;
        else return -1;
    }

    public static void main(String[] args) {
        List<Word> words = new ArrayList<>();
        words.add(new Word("1","b", "xxx"));
        words.add(new Word("2","aa", "xxx"));
        words.add(new Word("3","a", "xxx"));
        words.add(new Word("4","aaa", "xxx"));
        words.add(new Word("5","ones", "xxx"));
        words.add(new Word("6","c", "xxx"));

        words.sort(Word.getStandardComparator());
        int index = customBinarySearch(Dictionary.getInstance().getWords(), new Word("","Bach",""));
        if (index != -1)
            System.out.println(Dictionary.getInstance().getWords().get(index).getId());
        else System.out.println("Ko tim thay");
        System.out.println(new String("one").compareTo("org"));
=======
            if (target.compareTo(words.get(M)) > 0) L = M + 1;
            else R = M;
        }
        if (words.get(L).getTitle().toLowerCase().contains(target.getTitle().toLowerCase()))
            return L;
        else return -1;
    }

    static int mostRightBinarySearch(List<Word> words, Word target) {
        int L = 0;
        int R = words.size();
        while (L < R) {
            int M = (L + R) / 2;
            if (target.compareTo(words.get(M)) < 0) R = M;
            else L = M + 1;
        }
        if (words.get(R - 1).getTitle().toLowerCase().contains(target.getTitle().toLowerCase()))
            return R - 1;
        else return -1;
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
>>>>>>> origin
    }
}
