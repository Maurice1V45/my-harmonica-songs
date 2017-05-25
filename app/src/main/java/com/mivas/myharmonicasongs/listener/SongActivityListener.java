package com.mivas.myharmonicasongs.listener;


import com.mivas.myharmonicasongs.database.model.DbNote;

public interface SongActivityListener {

    void onNoteAdded(DbNote dbNote, boolean newRow);
    void onNoteEdited(DbNote dbNote);
    void onNoteDeleted(DbNote dbNote);
}
