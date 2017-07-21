package com.mivas.myharmonicasongs.listener;


import com.mivas.myharmonicasongs.database.model.DbSong;

import java.util.List;

/**
 * Listener for {@link com.mivas.myharmonicasongs.SettingsActivity}.
 */
public interface SettingsActivityListener {

    /**
     * Triggered when restore songs was confirmed.
     */
    void onRestoreConfirmed();
}
