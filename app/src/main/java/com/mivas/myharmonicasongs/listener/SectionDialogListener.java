package com.mivas.myharmonicasongs.listener;


import com.mivas.myharmonicasongs.database.model.DbSection;
import com.mivas.myharmonicasongs.model.CellLine;

/**
 * Listener for {@link com.mivas.myharmonicasongs.SongActivity}
 */
public interface SectionDialogListener {

    /**
     * Triggered when a new section has been added.
     *
     * @param dbSection
     */
    void onSectionAdded(DbSection dbSection, CellLine cellLine);

    /**
     * Triggered when a section was edited.
     *
     * @param dbSection
     */
    void onSectionEdit(DbSection dbSection, CellLine cellLine);

}
