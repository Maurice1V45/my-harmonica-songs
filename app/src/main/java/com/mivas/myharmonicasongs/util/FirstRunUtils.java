package com.mivas.myharmonicasongs.util;

import android.util.Log;

import com.mivas.myharmonicasongs.database.handler.NoteDbHandler;
import com.mivas.myharmonicasongs.database.handler.SongDbHandler;
import com.mivas.myharmonicasongs.database.model.DbNote;
import com.mivas.myharmonicasongs.database.model.DbSong;

import java.util.ArrayList;
import java.util.List;

/**
 * Util class for adding a song on the first run.
 */
public class FirstRunUtils {

    public static void addSampleSong() {
        DbSong dbSong = new DbSong();
        dbSong.setTitle("Happy Birthday Sample Song");
        dbSong.setAuthor("");
        SongDbHandler.insertSong(dbSong);
        long songId = dbSong.getId();
        List<DbNote> dbNotes = new ArrayList<DbNote>();
        dbNotes.add(new DbNote(6, true, "Ha", 0, 0, 0, songId));
        dbNotes.add(new DbNote(6, true, "ppy", 0, 1, 0, songId));
        dbNotes.add(new DbNote(6, false, "birth", 0, 2, 0, songId));
        dbNotes.add(new DbNote(6, true, "day", 0, 3, 0, songId));
        dbNotes.add(new DbNote(7, true, "to", 0, 4, 0, songId));
        dbNotes.add(new DbNote(7, false, "you", 0, 5, 0, songId));
        dbNotes.add(new DbNote(6, true, "Ha", 1, 0, 0, songId));
        dbNotes.add(new DbNote(6, true, "ppy", 1, 1, 0, songId));
        dbNotes.add(new DbNote(6, false, "birth", 1, 2, 0, songId));
        dbNotes.add(new DbNote(6, true, "day", 1, 3, 0, songId));
        dbNotes.add(new DbNote(8, false, "to", 1, 4, 0, songId));
        dbNotes.add(new DbNote(7, true, "you", 1, 5, 0, songId));
        dbNotes.add(new DbNote(6, true, "Ha", 2, 0, 0, songId));
        dbNotes.add(new DbNote(6, true, "ppy", 2, 1, 0, songId));
        dbNotes.add(new DbNote(9, true, "birth", 2, 2, 0, songId));
        dbNotes.add(new DbNote(8, true, "day", 2, 3, 0, songId));
        dbNotes.add(new DbNote(7, true, "dear", 2, 4, 0, songId));
        dbNotes.add(new DbNote(7, false, "u", 2, 5, 0, songId));
        dbNotes.add(new DbNote(6, false, "ser", 2, 6, 0, songId));
        dbNotes.add(new DbNote(9, false, "Ha", 3, 0, 0, songId));
        dbNotes.add(new DbNote(9, false, "ppy", 3, 1, 0, songId));
        dbNotes.add(new DbNote(8, true, "birth", 3, 2, 0, songId));
        dbNotes.add(new DbNote(7, true, "day", 3, 3, 0, songId));
        dbNotes.add(new DbNote(8, false, "to", 3, 4, 0, songId));
        dbNotes.add(new DbNote(7, true, "you", 3, 5, 0, songId));
        NoteDbHandler.insertNotes(dbNotes);
    }
}
