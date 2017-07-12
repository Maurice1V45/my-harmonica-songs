package com.mivas.myharmonicasongs.util;

import java.util.HashMap;
import java.util.Map;

public class SongKeyUtils {

    private static final Map<Integer, String> keysMap = new HashMap<Integer, String>();
    static {
        keysMap.put(0, "C");
        keysMap.put(1, "Db");
        keysMap.put(2, "D");
        keysMap.put(3, "Eb");
        keysMap.put(4, "E");
        keysMap.put(5, "F");
        keysMap.put(6, "Gb");
        keysMap.put(7, "G");
        keysMap.put(8, "Ab");
        keysMap.put(9, "A");
        keysMap.put(10, "Bb");
        keysMap.put(11, "B");
    }

    public static String getKey(int position) {
        return keysMap.get(position);
    }
}
