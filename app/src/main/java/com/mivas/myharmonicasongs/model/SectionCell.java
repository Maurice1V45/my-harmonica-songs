package com.mivas.myharmonicasongs.model;

import android.widget.TextView;

import com.mivas.myharmonicasongs.database.model.DbSection;

public class SectionCell {

    private DbSection dbSection;
    private TextView textView;

    public SectionCell() {
    }

    public SectionCell(DbSection dbSection, TextView textView) {
        this.dbSection = dbSection;
        this.textView = textView;
    }

    public DbSection getDbSection() {
        return dbSection;
    }

    public void setDbSection(DbSection dbSection) {
        this.dbSection = dbSection;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }
}
