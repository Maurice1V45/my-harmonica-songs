package com.mivas.myharmonicasongs.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Util class that stores and handles all sign representations.
 */
public class NoteSignUtils {

    private static final Map<Integer, String> signMap = new HashMap<Integer, String>();
    static {
        signMap.put(0, "-4");
        signMap.put(1, "4-");
        signMap.put(2, "4");
        signMap.put(3, "+4");
        signMap.put(4, "4+");
    }

    private static final Map<Integer, String> formatMap = new HashMap<Integer, String>();
    static {
        formatMap.put(0, "-%s");
        formatMap.put(1, "%s-");
        formatMap.put(2, "%s");
        formatMap.put(3, "+%s");
        formatMap.put(4, "%s+");
    }

    public static String getSign(int position) {
        return signMap.get(position);
    }

    public static String getFormat(int position) {
        return formatMap.get(position);
    }
}
