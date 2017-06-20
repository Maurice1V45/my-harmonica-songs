package com.mivas.myharmonicasongs.database.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Db model for Section
 */
@Table(name = "sections", id = "_id")
public class DbSection extends Model {

    @Column(name = "name")
    private String name;

    @Column(name = "row")
    private int row;

    @Column(name = "song_id")
    private long songId;

    public DbSection() {
    }

    public DbSection(String name, int row, long songId) {
        this.name = name;
        this.row = row;
        this.songId = songId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public long getSongId() {
        return songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }
}
