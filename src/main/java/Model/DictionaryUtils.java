package Model;

import java.util.ArrayList;
import java.util.List;


public class DictionaryUtils {
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
    }
}
