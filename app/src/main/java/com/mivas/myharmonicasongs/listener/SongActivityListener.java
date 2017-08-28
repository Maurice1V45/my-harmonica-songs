package com.mivas.myharmonicasongs.listener;


import com.mivas.myharmonicasongs.database.model.DbNote;
import com.mivas.myharmonicasongs.database.model.DbSection;
import com.mivas.myharmonicasongs.model.Cell;
import com.mivas.myharmonicasongs.model.CellLine;

import java.util.List;

/**
 * Listener for {@link com.mivas.myharmonicasongs.SongActivity}
 */
public interface SongActivityListener {

    void onNotesChanged(List<DbNote> dbNotes);

}
