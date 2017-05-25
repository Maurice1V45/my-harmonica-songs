package com.mivas.myharmonicasongs.database.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Db model for Song
 */
@Table(name = "songs", id = "_id")
public class DbSong extends Model {

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "favourite")
    private boolean favourite;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }
}
