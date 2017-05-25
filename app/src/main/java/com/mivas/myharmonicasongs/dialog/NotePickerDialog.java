package com.mivas.myharmonicasongs.dialog;

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
import com.mivas.myharmonicasongs.listener.NotePickerDialogListener;
import com.mivas.myharmonicasongs.listener.SongActivityListener;

/**
 * Dialog for choosing a harmonica note.
 */
public class NotePickerDialog extends DialogFragment implements NotePickerDialogListener {

    private DbNote dbNote;
    private EditText wordField;
    private SongActivityListener listener;
    private Button deleteButton;
    private boolean editMode;
    private boolean newRow;

    /**
     * Default constructor
     */
    public NotePickerDialog() {
        // empty constructor
    }

    /**
     * Views initializer
     *
     * @param rootView
     */
    private void initViews(View rootView) {
        wordField = (EditText) rootView.findViewById(R.id.field_word);
        wordField.setText(dbNote.getWord());
        deleteButton = (Button) rootView.findViewById(R.id.button_delete);
        deleteButton.setVisibility(editMode ? View.VISIBLE : View.GONE);
        RecyclerView notesList = (RecyclerView) rootView.findViewById(R.id.list_harmonica_notes);
        notesList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL, false));
        notesList.setAdapter(new HarmonicaNotesAdapter(getActivity(), NotePickerDialog.this, dbNote));
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
        View view = inflater.inflate(R.layout.dialog_note_picker, container);
        initViews(view);
        initListeners();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // set width to match parent
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
            listener.onNoteAdded(dbNote, newRow);
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

    public void setNewRow(boolean newRow) {
        this.newRow = newRow;
    }
}
