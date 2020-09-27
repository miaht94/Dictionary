package Model;

import java.util.ArrayList;
import java.util.List;

public class Word {
    private String id;
    private String title;
    private String XML;

    public Word(String id, String title, String entry) {
        this.id = id;
        this.title = title;
        this.XML = entry;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getXML() {
        return this.XML;
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
}
