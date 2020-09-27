package dictionary;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.IntStream;

public class DictionaryManagement {

    public static void insertFromCommandline() {
        Scanner sc = new Scanner(System.in);
        int length = sc.nextInt();
        sc.nextLine();
        IntStream.range(0, length).forEach(i -> Dictionary.getInstance().addWord(new Word(sc.nextLine(),sc.nextLine())));
    }

    public static void insertFromFile() {
        String path = DictionaryManagement.class.getClassLoader().getResource("dictionary.txt").getPath().substring(1);
        File file = new File(path);
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNext()) {
                String a = sc.nextLine();
                String[] b = a.split("\t");
                Dictionary.getInstance().addWord(new Word(b[0], b[1]));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Khong tim thay file dictionary");
        }

    }

    public static boolean lookUp() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhap tu can tim (target) or STT : ");
        String target = sc.nextLine();
        int index = lookUp(target);
        if (index != -1) {
            System.out.println(Dictionary.getInstance().stringFormatPrinting(index));
            return true;
        }
        else System.out.println("Khong tim thay tu da nhap");
        return false;
    }

    public static int lookUp(String target) {
        ArrayList<Word> dict = Dictionary.getInstance().getWords();
        if (target == null) throw new IllegalArgumentException();
        if (target != "")
            if (StringUtils.isNumeric(target)) return Integer.parseInt(target);
            for (int i = 0; i < dict.size(); i++) {
                if (dict.get(i).getWord_target().equals(target)) {
                    return i;
                }
            }
        return -1;
    }

    public static void addWord() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhap tu can them (Target) : ");
        String target = sc.nextLine();
        System.out.print("Nhap tu can them (Explain) : ");
        String explain = sc.nextLine();
        Word newWord = new Word(target, explain);
        addWord(newWord);
    }

    public static void addWord(Word newWord) {
        Dictionary.getInstance().addWord(newWord);
    }

    public static void modifyWord() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhap tu can sua hoac STT : ");
        String target = sc.nextLine();
        int indexTarget = -1;
        indexTarget = lookUp(target);
        if (indexTarget != -1) {
            System.out.println("Sua target tu " + Dictionary.getInstance().getWords().get(indexTarget).getWord_target() + " thanh ? : ");
            String newTarget = sc.nextLine();
            System.out.println("Sua explain tu " + Dictionary.getInstance().getWords().get(indexTarget).getWord_explain() + " thanh ? : ");
            String newExplain = sc.nextLine();
            modifyWord(indexTarget, new Word(newTarget, newExplain));
        } else System.out.println("Khong tim thay tu can sua");
    }

    public static void modifyWord(int index, Word newWord) {
        if (index < 0) throw new IllegalArgumentException();
        Dictionary.getInstance().getWords().set(index, newWord);
    }

    public static void deleteWord() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Nhap tu or STT can xoa : ");
        int indexTarget = lookUp(sc.nextLine());
        if (indexTarget != -1)
            deleteWord(indexTarget);
        else System.out.println("Khong the xoa tu da chon");
    }

    public static void deleteWord(int index) {
        if (index == -1) throw new IllegalArgumentException();
        Dictionary.getInstance().getWords().remove(index);
    }

    // Testing
    public static void main(String[] args) {
        insertFromFile();
        DictionaryCommandline.showAllWords();
        DictionaryManagement.deleteWord();
        DictionaryCommandline.showAllWords();
    }
}
