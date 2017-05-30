package com.mivas.myharmonicasongs.listener;


import com.mivas.myharmonicasongs.database.model.DbSong;

import java.util.List;

/**
 * Listener for {@link com.mivas.myharmonicasongs.MainActivity}.
 */
public interface MainActivityListener {

    /**
     * Triggered when a new song has been added.
     *
     * @param dbSong
     */
    void onSongAdded(DbSong dbSong);

    /**
     * Triggered when the Edit option was picked from the {@link com.mivas.myharmonicasongs.view.SongOptionsMenu}.
     *
     * @param dbSong
     */
    void onSongEdit(DbSong dbSong);

    /**
     * Triggered when a song was edited.
     *
     * @param dbSong
     */
    void onSongEditConfirmed(DbSong dbSong);

    /**
     * Triggered when the Delete option was picked from the {@link com.mivas.myharmonicasongs.view.SongOptionsMenu}
     *
     * @param dbSong
     */
    void onSongDelete(DbSong dbSong);

    /**
     * Triggered when a song was deleted.
     *
     * @param dbSong
     */
    void onSongDeleteConfirmed(DbSong dbSong);

    /**
     * Triggered when a song was selected.
     *
     * @param dbSong
     */
    void onSongSelected(DbSong dbSong);

    /**
     * Triggered when songs were selected for exporting.
     *
     * @param dbSongs
     */
    void onExportSongs(List<DbSong> dbSongs);
}
