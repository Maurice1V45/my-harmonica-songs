package com.mivas.myharmonicasongs.listener;


import com.mivas.myharmonicasongs.database.model.DbSong;

import java.util.List;

/**
 * Listener for {@link com.mivas.myharmonicasongs.SettingsActivity}.
 */
public interface SettingsActivityListener {

    /**
     * Triggered after the songs have been imported and added to db.
     *
     * @param dbSongs
     */
    void onSongsImported(List<DbSong> dbSongs);

    /**
     * Triggered if there was an error importing songs.
     */
    void onSongsImportedError();
}
