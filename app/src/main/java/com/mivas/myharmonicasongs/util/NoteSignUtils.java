package com.mivas.myharmonicasongs.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Util class that stores and handles all sign representations.
 */
public class NoteSignUtils {

    private static final Map<Integer, String> buttonMap = new HashMap<Integer, String>();
    static {
        buttonMap.put(0, "*%s");
        buttonMap.put(1, "%s*");
        buttonMap.put(2, "<%s");
        buttonMap.put(3, "%s<");
    }

    private static final Map<Integer, String> formatMap = new HashMap<Integer, String>();
    static {
        formatMap.put(0, "-%s");
        formatMap.put(1, "%s-");
        formatMap.put(2, "%s");
        formatMap.put(3, "+%s");
        formatMap.put(4, "%s+");
    }

    public static String getButton(int position) {
        return buttonMap.get(position);
    }

    public static String getFormat(int position) {
        return formatMap.get(position);
    }
}
