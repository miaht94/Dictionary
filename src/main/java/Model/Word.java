package Model;

import java.util.Comparator;

public class Word implements Comparable<Word> {
//    private String id;
    private String title;
//    private String XML;

    public Word(String id, String title, String entry) {
//        this.id = id;
        this.title = title;
//        this.XML = entry;
    }

//    public void setId(String id) {
//        this.id = id;
//    }
//
    public String getId() {
        return null;
    }

    public String getXML() {
        return null;
    }

    public String getTitle() {
        return title;
    }

//    public void setXML(String XML) {
//        this.XML = XML;
//    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int compareTo(Word o) {
        String target = o.getTitle();
        if (target.length() > this.getTitle().length())
            target = target.substring(0, this.getTitle().length()).toLowerCase();
        return this.getTitle().toLowerCase().compareTo(target);
    }

    public static Comparator<Word> getStandardComparator() {
        return new Comparator<Word>() {
            @Override
            public int compare(Word o1, Word o2) {
                String os1 = o1.getTitle().toLowerCase();
                String os2 = o2.getTitle().toLowerCase();
                return os1.compareTo(os2);
            }
        };
    }
}
