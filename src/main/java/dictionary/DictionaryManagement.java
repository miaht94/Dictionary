package dictionary;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
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

    // Testing
    public static void main(String[] args) {
        insertFromFile();
        DictionaryCommandline.showAllWords();
    }
}
