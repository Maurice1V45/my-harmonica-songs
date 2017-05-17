package com.mivas.myharmonicasongs.database.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "notes", id = "_id")
public class DbNote extends Model {

    @Column(name = "hole")
    private int hole;

    @Column(name = "blow")
    private boolean blow;

    @Column(name = "word")
    private String word;

    @Column(name = "row")
    private int row;

    @Column(name = "column")
    private int column;

    @Column(name = "song_id")
    private long songId;

    public int getHole() {
        return hole;
    }

    public void setHole(int hole) {
        this.hole = hole;
    }

    public boolean isBlow() {
        return blow;
    }

    public void setBlow(boolean blow) {
        this.blow = blow;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public long getSongId() {
        return songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }
}
