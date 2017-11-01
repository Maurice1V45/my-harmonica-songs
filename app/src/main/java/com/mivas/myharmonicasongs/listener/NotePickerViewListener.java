package com.mivas.myharmonicasongs.listener;

import com.mivas.myharmonicasongs.database.model.DbNote;
import com.mivas.myharmonicasongs.database.model.DbSection;
import com.mivas.myharmonicasongs.model.Cell;
import com.mivas.myharmonicasongs.model.CellLine;

public interface NotePickerViewListener {

    void onNoteAdded(DbNote dbNote, boolean newRow, CellLine cellLine);
    void onNoteEdited(CellLine cellLine, Cell cell);
    void onNoteDeleted(CellLine cellLine, Cell cell);
    void onBendsSelected(boolean bends, CellLine cellLine);
    void onRowDeleted(CellLine cellLine);
    void onNotesCopied(CellLine cellLine);
    void onNotesPasted(CellLine cellLine, Cell cell);
    void onSectionAdded(DbSection dbSection, CellLine cellLine);
    void onSectionEdited(DbSection dbSection, CellLine cellLine);
    void onSectionDeleted(CellLine cellLine);
}
