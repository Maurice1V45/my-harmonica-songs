package com.mivas.myharmonicasongs.model;

import android.widget.LinearLayout;

import java.util.List;

public class CellLine {

    private List<Cell> cells;
    private LinearLayout layout;
    private SectionCell sectionCell;

    public CellLine() {
    }

    public CellLine(List<Cell> cells, LinearLayout layout) {
        this.cells = cells;
        this.layout = layout;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    public LinearLayout getLayout() {
        return layout;
    }

    public void setLayout(LinearLayout layout) {
        this.layout = layout;
    }

    public SectionCell getSectionCell() {
        return sectionCell;
    }

    public void setSectionCell(SectionCell sectionCell) {
        this.sectionCell = sectionCell;
    }

    public void addCell(Cell cell) {
        this.cells.add(cell);
    }

    public void addCell(Cell cell, int index) {
        this.cells.add(index, cell);
    }
}
