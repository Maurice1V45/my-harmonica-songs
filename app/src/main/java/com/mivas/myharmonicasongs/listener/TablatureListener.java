package com.mivas.myharmonicasongs.listener;


import com.mivas.myharmonicasongs.database.model.DbNote;
import com.mivas.myharmonicasongs.database.model.DbSection;

import java.util.List;

public interface TablatureListener {

    void onNotesChanged(List<DbNote> dbNotes);
    void onSectionsChanged(List<DbSection> dbSections);

}
