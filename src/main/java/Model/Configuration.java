package Model;

import java.util.HashMap;
import java.util.Map;

import static Model.DictionaryType.VI_EN;

public class Configuration {
    public static boolean initiated = false;
    public static Map<DictionaryType, DictRange> dictRange = new HashMap<>();

    public static final class DictRange {
        public int start;
        public int end;
        public DictRange(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    public static DictRange getDictRange(DictionaryType type) {
        if (!initiated) init();
        return dictRange.get(type);
    }

    private static void init() {
        for (DictionaryType type: DictionaryType.values()) {
            switch (type) {
                case EN_VI:
                    dictRange.put(type, new DictRange(1, 200768));
                    break;
                case VI_EN:
                    dictRange.put(type, new DictRange(1, 79707));
                    break;
            }
        }
    }

}
