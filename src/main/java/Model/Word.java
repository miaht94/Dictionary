package Model;

import java.sql.SQLException;
import java.util.Comparator;

public class Word implements Comparable<Word> {
    private String id;
    private int ID = -1;
    private String title;
    private String XML;

    public Word(String title) {
        this.title = title;
    }

    public Word(int ID, String title) {
        this.ID = ID;
        this.title = title;
    }

    public Word(String title, String XML) {
        this.XML = XML;
        this.title = title;
    }

    public Word(String id, String title, String XML) {
        this.id = id;
        this.title = title;
        this.XML = XML;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getXML() {
        if (this.XML == null && this.ID != -1)
            try {
                return DBReader.getInstance(Dictionary.currType).executeQuery("Select entry from " + Dictionary.currType.toString() + " where _rowid_ = " + this.ID).getString("entry");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                return "Khong load duoc DB";
            }
        else return this.XML;
    }

    public String getTitle() {
        return title;
    }

    public void setXML(String XML) {
        this.XML = XML;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int compareTo(Word o) {
        String target = o.getTitle().toLowerCase();
        //String current = this.getTitle();
        //if (target.charAt(0) == '\'' || target.charAt(0) == '-') target = target.substring(1);
        // if (current.charAt(0) == '\'' || current.charAt(0) == '-') current = current.substring(1);
        if (target.length() > this.getTitle().length())
            target = target.substring(0, this.getTitle().length());
        return this.getTitle().toLowerCase().compareTo(target);
    }

    public static Comparator<Word> getStandardComparator() {
//        return new Comparator<Word>() {
//            @Override
//            public int compare(Word o1, Word o2) {
//                String os1 = o1.getTitle().toLowerCase();
//                String os2 = o2.getTitle().toLowerCase();
//                return os1.compareTo(os2);
//            }
//        };
        return (o1, o2) -> {
            String os1 = o1.getTitle().toLowerCase();
            String os2 = o2.getTitle().toLowerCase();
            return os1.compareTo(os2);
        };
    }
}
