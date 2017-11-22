package com.mivas.myharmonicasongs.util;

import com.mivas.myharmonicasongs.database.model.DbSong;

public class AudioFileUtils {

    public static final String getFormattedFileName(DbSong dbSong, String fileName) {
        return dbSong.getId() + Constants.SEPARATOR_AUDIO_FILE + fileName;
    }

    public static final String getRawFileName(DbSong dbSong) {
        String formattedName = dbSong.getAudioFile();
        if (formattedName != null){
            String[] splittedName = formattedName.split(Constants.SEPARATOR_AUDIO_FILE);
            if (splittedName.length > 1) {
                return splittedName[1];
            }
        }
        return "";
    }
}
