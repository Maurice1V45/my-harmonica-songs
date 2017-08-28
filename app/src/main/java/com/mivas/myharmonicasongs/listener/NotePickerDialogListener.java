package com.mivas.myharmonicasongs.listener;

import com.mivas.myharmonicasongs.database.model.DbNote;
import com.mivas.myharmonicasongs.model.Cell;
import com.mivas.myharmonicasongs.model.CellLine;

public interface NotePickerDialogListener {

    /**
     * Triggered when a note was added.
     *
     * @param dbNote
     * @param newRow True, if the Insert new row option was picked from the options menu.
     */
    void onNoteAdded(DbNote dbNote, boolean newRow, CellLine cellLine);

    /**
     * Triggered when a note was edited.
     */
    void onNoteEdited(Cell cell);

    /**
     * Triggered when a note was deleted.
     */
    void onNoteDeleted(Cell cell, CellLine cellLine);

}
