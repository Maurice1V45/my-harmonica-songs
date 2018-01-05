package com.mivas.myharmonicasongs.util;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.mivas.myharmonicasongs.MHSApplication;
import com.mivas.myharmonicasongs.database.handler.SongDbHandler;
import com.mivas.myharmonicasongs.database.model.DbSong;

import java.util.List;

public class AnalyticUtils {

    public static void setUserProperties() {
        FirebaseAnalytics firebaseAnalytics = MHSApplication.getInstance().getFirebaseAnalytics();
        firebaseAnalytics.setUserProperty("harp_type", getHarpTypeString(PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_HARP_TYPE, 0)));
        firebaseAnalytics.setUserProperty("songs_count", getSongsCountString(SongDbHandler.getSongs()));
        firebaseAnalytics.setUserProperty("instrumentals_count", getSongsCountString(SongDbHandler.getSongsWithInstrumentals()));
    }

    private static String getHarpTypeString(int harpType) {
        switch (harpType) {
            case 0:
                return "diatonic 10";
            case 1:
                return "chromatic 12";
            case 2:
                return "chromatic 16";
            default:
                return "";
        }
    }

    private static String getSongsCountString(List<DbSong> dbSongs) {
        int size = dbSongs.size();
        int i = 0;
        while (i < 100) {
            if (i <= size && size < i + 10) {
                return String.valueOf(i) + "-" + String.valueOf(i + 9);
            }
            i += 10;
        }
        return "100+";
    }
}
