package Model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Callable;

public class DictionarySearcher {
    private String cachedString = null;
    private List<Word> cachedWords = new ArrayList<>();
    private final Map<String, String> wordMap = new HashMap<>();
    private final List<Word> firstTimeCache =  new ArrayList<>();
    private DBReader dbReader;
    private int startIndex = 1;
    private int endIndex = 200768;
    public DictionarySearcher(DBReader dbReader) {
        this.dbReader = dbReader;
        try {
            getRangeChar();
            setFirstTimeCache();
            cachedWords = firstTimeCache;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    private void getRangeChar() throws SQLException {
        ResultSet rs = dbReader.executeQuery("SELECT * FROM definitions where _rowid_ between " + startIndex + " and " + (endIndex - 1));
        int count = 1;
        int temp_count = 1;
        wordMap.put("", "0 0");
        rs.next();
        String tempChar = rs.getString("title").substring(0,1).toLowerCase();
        String currFirstChar = null;
        while (true) {
            if (!rs.next()) {
                wordMap.put(currFirstChar, temp_count + " " + count);
                break;
            }
            currFirstChar = rs.getString("title").substring(0,1).toLowerCase();
            if (wordMap.get(currFirstChar) == null && !currFirstChar.equals("-") && !currFirstChar.equals("'")) {
                wordMap.put(tempChar, temp_count + " " + count);
                tempChar = currFirstChar;
                temp_count = count;
            }
            count++;
        }
    }

    public String getCachedString() {
        return cachedString;
    }

    public void setCachedString(String cachedString) {
        this.cachedString = cachedString;
    }

    private void setFirstTimeCache() throws SQLException {
        for (Map.Entry<String, String> item : wordMap.entrySet()) {
            String key = item.getKey();
            String val = item.getValue();
            String[] index = val.split(" ");
            int start = Integer.parseInt(index[0]);
            int end = Integer.parseInt(index[1]);
            ResultSet rs = dbReader.getRows(start, Math.min(400, end - start + 1));
            int count = 0;
            while(rs.next()) {
                firstTimeCache.add(new Word(start + count, rs.getString("title")));
                count++;
            }
        }
        firstTimeCache.sort(Word.getStandardComparator());
    }

    private void fetchCacheList(String firstChar) {
        this.cachedString = firstChar;
        this.cachedWords.clear();
//        Callable<List<Word>> fetch = new Fetcher();
        String[] range = wordMap.get(firstChar).split(" ");
        int start = Integer.parseInt(range[0]);
        int end = Integer.parseInt(range[1]);
        ResultSet rs = dbReader.getRows(start, end - start + 1);

        try {
            int count = 0;
            while (rs.next()) {
                cachedWords.add(new Word(start + count, rs.getString("title")));
                count++;
            }
            cachedWords.sort(Word.getStandardComparator());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
//    private class Fetcher implements Callable<List<Word>> {
//
//        @Override
//        public List<Word> call() throws Exception {
//            return null;
//        }
//    }
    public List<Word> search(String target) {
        if (target.equals("")) return null;
        if (target.charAt(0) == '\'' || target.charAt(0) == '-') target = target.substring(1);
        List<Word> lookupList = cachedWords;
        if (cachedString == null || target.charAt(0) != cachedString.charAt(0)) {
            char firstChar = target.charAt(0);
            fetchCacheList(Character.toString(firstChar));
//            this.cachedString = "" + target.charAt(0);
            lookupList = firstTimeCache;
        }
        List<Word> result = new ArrayList<>();
        int start = DictionaryUtils.mostLeftBinarySearch(lookupList, new Word(target));
        int end = DictionaryUtils.mostRightBinarySearch(lookupList, new Word(target));
        if (start != -1 && end != -1)
            result.addAll(cachedWords.subList(start, end + 1));
        return result;
    }

}
