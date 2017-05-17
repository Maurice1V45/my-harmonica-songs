package com.mivas.myharmonicasongs.listener;


import com.mivas.myharmonicasongs.database.model.DbSong;

public interface MainActivityListener {

    void onSongAdded(DbSong dbSong);
    void onSongEdit(DbSong dbSong);
    void onSongEditConfirmed(DbSong dbSong);
    void onSongDelete(DbSong dbSong);
    void onSongDeleteConfirmed(DbSong dbSong);
    void onSongSelected(DbSong dbSong);
}
