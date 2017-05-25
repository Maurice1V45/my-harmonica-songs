package com.mivas.myharmonicasongs.listener;

/**
 * Listener for {@link com.mivas.myharmonicasongs.dialog.NotePickerDialog}.
 */
public interface NotePickerDialogListener {

    /**
     * Triggered when a note has been picked.
     *
     * @param note
     * @param blow
     */
    void onNoteSelected(int note, boolean blow);
}
