package com.mivas.myharmonicasongs.util;

public class TimeUtils {

    public static int toMillis(int minutes, int seconds) {
        return seconds * 1000 + minutes * 60 * 1000;
    }

    public static int getMinutes(int millis) {
        return (millis / 1000) / 60;
    }

    public static int getSeconds(int millis) {
        return (millis / 1000) % 60;
    }

    public static String toDisplayTime(int millis) {
        return getMinutes(millis) + ":" + String.format("%02d", getSeconds(millis));
    }
}
