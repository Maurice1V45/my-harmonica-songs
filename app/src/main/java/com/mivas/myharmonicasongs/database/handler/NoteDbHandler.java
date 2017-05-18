package com.mivas.myharmonicasongs.database.handler;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.mivas.myharmonicasongs.database.model.DbNote;

import java.util.List;

/**
 * Db handler for notes.
 */
public class NoteDbHandler {

    /**
     * Inserts a note into db.
     *
     * @param dbNote The note to be inserted to the db.
     */
    public static void insertNote(DbNote dbNote) {
        dbNote.save();
    }

    /**
     * Inserts a list of notes into db.
     *
     * @param dbNotes The list of notes to be inserted into db.
     */
    public static void insertNotes(List<DbNote> dbNotes) {
        for (DbNote dbNote : dbNotes) {
            dbNote.save();
        }
    }

    /**
     * Retrieves all notes from the db.
     *
     * @return A list of notes.
     */
    public static List<DbNote> getNotes() {
        return new Select()
                .from(DbNote.class)
                .execute();
    }

    /**
     * Retrieves notes that belong to a song.
     *
     * @param songId The song id.
     * @return A list of notes.
     */
    public static List<DbNote> getNotesBySongId(long songId) {
        return new Select()
                .from(DbNote.class)
                .where("song_id = ?", songId)
                .execute();
    }

    /**
     * Deletes all notes from db.
     */
    public static void deleteNotes() {
        new Delete()
                .from(DbNote.class)
                .execute();
    }

    /**
     * Deletes all notes that belong to a song.
     *
     * @param songId The song id.
     */
    public static void deleteNotesBySongId(long songId) {
        new Delete()
                .from(DbNote.class)
                .where("song_id = ?", songId)
                .execute();
    }

}
