package com.mivas.myharmonicasongs.dialog;

import android.app.DialogFragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.database.model.DbNote;
import com.mivas.myharmonicasongs.listener.NotesShiftDialogListener;
import com.mivas.myharmonicasongs.util.NotesShiftUtils;

import java.util.List;

/**
 * Dialog for deleting a song.
 */
public class NotesShiftDialog extends DialogFragment {

    private View increaseView;
    private View decreaseView;
    private TextView increaseEligibleText;
    private TextView decreaseEligibleText;
    private NotesShiftDialogListener listener;
    private List<DbNote> dbNotes;
    private int increaseEligible;
    private int decreaseEligible;

    /**
     * Default constructor
     */
    public NotesShiftDialog() {
        // empty constructor
    }

    /**
     * Views initializer
     *
     * @param rootView
     */
    private void initViews(View rootView) {
        increaseView = rootView.findViewById(R.id.view_increase_notes);
        decreaseView = rootView.findViewById(R.id.view_decrease_notes);
        increaseEligibleText = rootView.findViewById(R.id.text_eligible_notes_increase);
        decreaseEligibleText = rootView.findViewById(R.id.text_eligible_notes_decrease);
    }

    /**
     * Listeners initializer
     */
    private void initListeners() {
        increaseView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (increaseEligible < dbNotes.size()) {
                    listener.onNotesShiftConfirmationRequested(true, increaseEligible, dbNotes.size());
                } else {
                    listener.onNotesShiftedUp();
                }
                dismiss();
            }
        });
        decreaseView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (decreaseEligible < dbNotes.size()) {
                    listener.onNotesShiftConfirmationRequested(false, decreaseEligible, dbNotes.size());
                } else {
                    listener.onNotesShiftedDown();
                }
                dismiss();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_notes_shift, container);
        initViews(view);
        initListeners();
        initEligibles();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }

    private void initEligibles() {
        for (DbNote dbNote : dbNotes) {
            if (NotesShiftUtils.isIncreasePossible(dbNote)) {
                increaseEligible++;
            }
            if (NotesShiftUtils.isDecreasePossible(dbNote)) {
                decreaseEligible++;
            }
        }
        if (dbNotes.size() == 0 || increaseEligible == 0) {
            increaseEligibleText.setText(R.string.notes_shift_dialog_no);
            increaseEligibleText.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
            increaseView.setEnabled(false);
        } else {
            if (increaseEligible == dbNotes.size()) {
                increaseEligibleText.setText(R.string.notes_shift_dialog_all);
                increaseEligibleText.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
            } else {
                increaseEligibleText.setText(increaseEligible + " / " + dbNotes.size());
                increaseEligibleText.setTextColor(ContextCompat.getColor(getActivity(), R.color.red));
            }
        }
        if (dbNotes.size() == 0 || decreaseEligible == 0) {
            decreaseEligibleText.setText(R.string.notes_shift_dialog_no);
            decreaseEligibleText.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
            decreaseView.setEnabled(false);
        } else {
            if (decreaseEligible == dbNotes.size()) {
                decreaseEligibleText.setText(R.string.notes_shift_dialog_all);
                decreaseEligibleText.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
            } else {
                decreaseEligibleText.setText(decreaseEligible + " / " + dbNotes.size());
                decreaseEligibleText.setTextColor(ContextCompat.getColor(getActivity(), R.color.red));
            }
        }
    }

    public void setListener(NotesShiftDialogListener listener) {
        this.listener = listener;
    }

    public void setDbNotes(List<DbNote> dbNotes) {
        this.dbNotes = dbNotes;
    }

}
