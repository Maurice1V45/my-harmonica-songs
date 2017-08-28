package com.mivas.myharmonicasongs.listener;

import com.mivas.myharmonicasongs.dialog.NotePickerDialog;

/**
 * Listener for {@link NotePickerDialog}.
 */
public interface NotePickerAdapterListener {

    /**
     * Triggered when a note has been picked.
     *
     * @param note
     * @param blow
     * @param bend
     */
    void onNoteSelected(int note, boolean blow, float bend);
}
