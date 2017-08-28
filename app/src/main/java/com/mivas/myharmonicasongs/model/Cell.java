package com.mivas.myharmonicasongs.model;

import android.view.View;

import com.mivas.myharmonicasongs.database.model.DbNote;

public class Cell {

    private DbNote dbNote;
    private View view;

    public Cell() {
    }

    public Cell(DbNote dbNote, View view) {
        this.dbNote = dbNote;
        this.view = view;
    }

    public DbNote getDbNote() {
        return dbNote;
    }

    public void setDbNote(DbNote dbNote) {
        this.dbNote = dbNote;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
