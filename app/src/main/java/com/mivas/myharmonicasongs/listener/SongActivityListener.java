package com.mivas.myharmonicasongs.listener;


import com.mivas.myharmonicasongs.database.model.DbNote;

/**
 * Listener for {@link com.mivas.myharmonicasongs.SongActivity}
 */
public interface SongActivityListener {

    /**
     * Triggered when a note was added.
     *
     * @param dbNote
     * @param newRow True, if the Insert new row option was picked from the {@link com.mivas.myharmonicasongs.view.NoteRowOptionsMenu}.
     */
    void onNoteAdded(DbNote dbNote, boolean newRow);

    /**
     * Triggered when a note was edited.
     *
     * @param dbNote
     */
    void onNoteEdited(DbNote dbNote);

    /**
     * Triggered when a note was deleted.
     *
     * @param dbNote
     */
    void onNoteDeleted(DbNote dbNote);
}
