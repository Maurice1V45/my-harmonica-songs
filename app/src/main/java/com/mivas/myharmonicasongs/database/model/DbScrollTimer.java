package com.mivas.myharmonicasongs.database.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Db model for Section
 */
@Table(name = "scroll_timers", id = "_id")
public class DbScrollTimer extends Model {

    @Column(name = "time")
    private int time;

    @Column(name = "section_id")
    private long sectionId;

    @Column(name = "section_line")
    private int sectionLine;

    @Column(name = "song_id")
    private long songId;

    public DbScrollTimer() {

    }

    public DbScrollTimer(int time, long sectionId, int sectionLine, long songId) {
        this.time = time;
        this.sectionId = sectionId;
        this.sectionLine = sectionLine;
        this.songId = songId;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public long getSectionId() {
        return sectionId;
    }

    public void setSectionId(long sectionId) {
        this.sectionId = sectionId;
    }

    public int getSectionLine() {
        return sectionLine;
    }

    public void setSectionLine(int sectionLine) {
        this.sectionLine = sectionLine;
    }

    public long getSongId() {
        return songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }
}
