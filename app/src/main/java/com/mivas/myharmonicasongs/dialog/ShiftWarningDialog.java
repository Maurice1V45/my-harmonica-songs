package com.mivas.myharmonicasongs.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mivas.myharmonicasongs.MHSApplication;
import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.listener.NotesShiftListener;

/**
 * Dialog for warning before shifting notes.
 */
public class ShiftWarningDialog extends DialogFragment {

    private TextView titleText;
    private Button yesButton;
    private Button noButton;
    private NotesShiftListener listener;
    private boolean shiftUp;
    private int eligibleNotes;
    private int allNotes;

    /**
     * Default constructor
     */
    public ShiftWarningDialog() {
        // empty constructor
    }

    /**
     * Views initializer
     *
     * @param rootView
     */
    private void initViews(View rootView) {
        titleText = (TextView) rootView.findViewById(R.id.text_title);
        String message = String.format((allNotes - eligibleNotes) == 1 ? getString(R.string.shift_warning_dialog_note_text) : getString(R.string.shift_warning_dialog_notes_text), eligibleNotes, allNotes, (allNotes - eligibleNotes));
        titleText.setText(message);
        yesButton = (Button) rootView.findViewById(R.id.button_yes);
        noButton = (Button) rootView.findViewById(R.id.button_no);
    }

    /**
     * Listeners initializer
     */
    private void initListeners() {
        yesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (shiftUp) {
                    listener.onNotesShiftedUp();
                } else {
                    listener.onNotesShiftedDown();
                }
                getDialog().dismiss();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_shift_warning, container);
        initViews(view);
        initListeners();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // set width to match parent
        if (!MHSApplication.getInstance().isTablet() && getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    public void setListener(NotesShiftListener listener) {
        this.listener = listener;
    }

    public void setShiftUp(boolean shiftUp) {
        this.shiftUp = shiftUp;
    }

    public void setEligibleNotes(int eligibleNotes) {
        this.eligibleNotes = eligibleNotes;
    }

    public void setAllNotes(int allNotes) {
        this.allNotes = allNotes;
    }
}
