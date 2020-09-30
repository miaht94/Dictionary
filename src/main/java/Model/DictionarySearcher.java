package Model;

import java.util.List;

public class DictionarySearcher {
    private String charCached = null;
    private List<Word> cachedWords = null;
    public String getCharCached() {
        return charCached;
    }

    public void setCharCached(String charCached) {
        this.charCached = charCached;
    }

    public void search() {
        DictionaryUtils.customBinarySearch()
    }

}
