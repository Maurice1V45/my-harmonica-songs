package com.mivas.myharmonicasongs.model;

import android.widget.LinearLayout;

import com.mivas.myharmonicasongs.database.model.DbNote;

import java.util.ArrayList;
import java.util.List;

public class RowViewHolder {

    private List<NoteViewHolder> noteViewHolders;
    private LinearLayout layout;

    public RowViewHolder() {
    }

    public RowViewHolder(List<NoteViewHolder> noteViewHolders, LinearLayout layout) {
        this.noteViewHolders = noteViewHolders;
        this.layout = layout;
    }

    public List<NoteViewHolder> getNoteViewHolders() {
        return noteViewHolders;
    }

    public void setNoteViewHolders(List<NoteViewHolder> noteViewHolders) {
        this.noteViewHolders = noteViewHolders;
    }

    public LinearLayout getLayout() {
        return layout;
    }

    public void setLayout(LinearLayout layout) {
        this.layout = layout;
    }

    public void addNoteViewHolder(NoteViewHolder noteViewHolder) {
        this.noteViewHolders.add(noteViewHolder);
    }

    public void addNoteViewHolder(NoteViewHolder noteViewHolder, int index) {
        this.noteViewHolders.add(index, noteViewHolder);
    }
}
