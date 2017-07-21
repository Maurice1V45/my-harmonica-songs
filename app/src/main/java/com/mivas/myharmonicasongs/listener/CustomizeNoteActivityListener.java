package com.mivas.myharmonicasongs.listener;

import com.mivas.myharmonicasongs.CustomizeNoteActivity;

/**
 * Listener for {@link CustomizeNoteActivity}
 */
public interface CustomizeNoteActivityListener {

    /**
     * Triggered when a sign was selected.
     *
     * @param position The position of the selected sign.
     */
    void onSignSelected(int position);

}
