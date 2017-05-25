package com.mivas.myharmonicasongs.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.adapter.HarmonicaNotesAdapter;
import com.mivas.myharmonicasongs.database.model.DbNote;
import com.mivas.myharmonicasongs.listener.HarmonicaNotesDialogListener;
import com.mivas.myharmonicasongs.listener.SongActivityListener;

/**
 * Dialog for selecting a harmonica note.
 */
public class HarmonicaNotesDialog extends DialogFragment implements HarmonicaNotesDialogListener {

    private RecyclerView notesList;
    private DbNote dbNote;
    private EditText wordField;
    private SongActivityListener listener;
    private boolean editMode;
    private Button deleteButton;

    /**
     * Default constructor
     */
    public HarmonicaNotesDialog() {
        // empty constructor
    }

    /**
     * Views initializer
     *
     * @param rootView
     */
    private void initViews(View rootView) {
        wordField = (EditText) rootView.findViewById(R.id.word_field);
        wordField.setText(dbNote.getWord());
        notesList = (RecyclerView) rootView.findViewById(R.id.list_harmonica_notes);
        notesList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL, false));
        notesList.setAdapter(new HarmonicaNotesAdapter(getActivity(), HarmonicaNotesDialog.this, dbNote));
        deleteButton = (Button) rootView.findViewById(R.id.button_delete);
        deleteButton.setVisibility(editMode ? View.VISIBLE : View.GONE);
    }

    /**
     * Listeners initializer
     */
    private void initListeners() {
        deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onNoteDeleted(dbNote);
                dismiss();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_harmonica_notes, container);
        initViews(view);
        initListeners();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // set width to match parent
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onNoteSelected(int note, boolean blow) {
        dbNote.setHole(note);
        dbNote.setBlow(blow);
        dbNote.setWord(wordField.getText().toString());
        if (editMode) {
            listener.onNoteEdited(dbNote);
        } else {
            listener.onNoteAdded(dbNote);
        }
        dismiss();
    }

    public void setDbNote(DbNote dbNote) {
        this.dbNote = dbNote;
    }

    public void setListener(SongActivityListener listener) {
        this.listener = listener;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

}
