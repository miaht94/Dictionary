package dictionary;
import java.util.Scanner;
import java.util.stream.IntStream;

public class DictionaryManagement {
    public static Scanner sc;
    public static void insertFromCommandline() {
        sc = new Scanner(System.in);
        int length = sc.nextInt();
        sc.nextLine();
        IntStream.range(0, length).forEach(i -> Dictionary.getInstance().addWord(new Word(sc.nextLine(),sc.nextLine())));
    }
}
