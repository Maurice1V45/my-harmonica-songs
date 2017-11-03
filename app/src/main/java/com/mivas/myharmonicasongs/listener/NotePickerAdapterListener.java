package com.mivas.myharmonicasongs.listener;

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
