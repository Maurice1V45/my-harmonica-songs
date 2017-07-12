package com.mivas.myharmonicasongs.listener;

/**
 * Listener for {@link com.mivas.myharmonicasongs.dialog.SongDialog}
 */
public interface SongDialogListener {

    /**
     * Triggered when a key was selected.
     *
     * @param position The position of the selected key.
     */
    void onKeySelected(int position);

}
