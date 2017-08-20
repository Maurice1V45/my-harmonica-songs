package com.mivas.myharmonicasongs.listener;

public interface NotesShiftListener {

    void onNotesShiftedUp();
    void onNotesShiftedDown();
    void onNotesShiftConfirmationRequested(boolean shiftUp, int eligibleNotes, int allNotes);

}
