package com.mivas.myharmonicasongs.listener;


import com.mivas.myharmonicasongs.database.model.DbNote;
import com.mivas.myharmonicasongs.model.CellLine;

import java.util.List;

public interface SectionBarListener {

    void onSectionSelected(CellLine cellLine);

}
