package com.mivas.myharmonicasongs.listener;


import com.mivas.myharmonicasongs.database.model.DbNote;
import com.mivas.myharmonicasongs.database.model.DbSection;
import com.mivas.myharmonicasongs.model.NoteViewHolder;
import com.mivas.myharmonicasongs.model.RowViewHolder;

/**
 * Listener for {@link com.mivas.myharmonicasongs.SongActivity}
 */
public interface SongActivityListener {

    /**
     * Triggered when a note was added.
     *
     * @param dbNote
     * @param newRow True, if the Insert new row option was picked from the options menu.
     */
    void onNoteAdded(DbNote dbNote, boolean newRow, RowViewHolder rowViewHolder);

    /**
     * Triggered when a note was edited.
     *
     * @param dbNote
     */
    void onNoteEdited(DbNote dbNote, NoteViewHolder noteViewHolder);

    /**
     * Triggered when a note was deleted.
     *
     * @param dbNote
     */
    void onNoteDeleted(DbNote dbNote, NoteViewHolder noteViewHolder, RowViewHolder rowViewHolder);

    /**
     * Triggered when a new section has been added.
     *
     * @param dbSection
     */
    void onSectionAdded(DbSection dbSection);

    /**
     * Triggered when a section was edited.
     *
     * @param dbSection
     */
    void onSectionEdit(DbSection dbSection);

}
