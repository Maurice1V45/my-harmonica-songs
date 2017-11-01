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
import com.mivas.myharmonicasongs.listener.InsertNoteDialogListener;
import com.mivas.myharmonicasongs.listener.NotesShiftDialogListener;
import com.mivas.myharmonicasongs.util.NotesShiftUtils;

import java.util.List;

public class InsertNoteDialog extends DialogFragment {

    private InsertNoteDialogListener listener;

    /**
     * Default constructor
     */
    public InsertNoteDialog() {
        // empty constructor
    }

    /**
     * Views initializer
     *
     * @param rootView
     */
    private void initViews(View rootView) {
    }

    /**
     * Listeners initializer
     */
    private void initListeners() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_insert_note, container);
        initViews(view);
        initListeners();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }

    public void setListener(InsertNoteDialogListener listener) {
        this.listener = listener;
    }


}
