package com.mivas.myharmonicasongs.model;

import android.view.View;
import android.widget.TextView;

import com.mivas.myharmonicasongs.database.model.DbSection;

public class SectionCell {

    private DbSection dbSection;
    private View sectionView;
    private TextView textView;

    public SectionCell() {
    }

    public SectionCell(DbSection dbSection, View sectionView, TextView textView) {
        this.dbSection = dbSection;
        this.textView = textView;
        this.textView = textView;
    }

    public DbSection getDbSection() {
        return dbSection;
    }

    public void setDbSection(DbSection dbSection) {
        this.dbSection = dbSection;
    }

    public View getSectionView() {
        return sectionView;
    }

    public void setSectionView(View sectionView) {
        this.sectionView = sectionView;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }
}
