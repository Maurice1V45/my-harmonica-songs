package com.mivas.myharmonicasongs.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.mivas.myharmonicasongs.MHSApplication;
import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.adapter.NotePickerAdapter;
import com.mivas.myharmonicasongs.database.model.DbNote;
import com.mivas.myharmonicasongs.listener.NotePickerDialogListener;
import com.mivas.myharmonicasongs.listener.SongActivityListener;
import com.mivas.myharmonicasongs.model.NoteViewHolder;
import com.mivas.myharmonicasongs.model.RowViewHolder;

/**
 * Dialog for choosing a harmonica note.
 */
public class NotePickerDialog extends DialogFragment implements NotePickerDialogListener {

    private DbNote dbNote;
    private RecyclerView notesList;
    private EditText wordField;
    private SongActivityListener listener;
    private View deleteButton;
    private CheckBox showBendingsCheckbox;
    private NoteViewHolder noteViewHolder;
    private RowViewHolder rowViewHolder;
    private NotePickerAdapter adapter;
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
        wordField.setHintTextColor(ContextCompat.getColor(getActivity(), R.color.greyDD));
        if (dbNote.getColumn() == 0) {
            wordField.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        }
        showBendingsCheckbox = (CheckBox) rootView.findViewById(R.id.checkbox_show_bendings);
        showBendingsCheckbox.setChecked(dbNote.getBend() != 0);
        deleteButton = rootView.findViewById(R.id.button_delete);
        deleteButton.setVisibility(editMode ? View.VISIBLE : View.GONE);
        notesList = (RecyclerView) rootView.findViewById(R.id.list_harmonica_notes);
        notesList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL, false));
        adapter = new NotePickerAdapter(getActivity(), NotePickerDialog.this, dbNote, dbNote.getBend() != 0);
        notesList.setAdapter(adapter);
    }

    /**
     * Listeners initializer
     */
    private void initListeners() {
        deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onNoteDeleted(dbNote, noteViewHolder, rowViewHolder);
                dismiss();
            }
        });
        showBendingsCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showBendingsCheckbox.setText(isChecked ? R.string.note_picker_dialog_button_hide_bendings : R.string.note_picker_dialog_button_show_bendings);
                adapter.setShowBendings(isChecked);
                adapter.notifyDataSetChanged();
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

        if (getDialog() != null) {

            // hide keyboard
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            if (!MHSApplication.getInstance().isTablet()) {

                // set width to match parent
                getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        }
    }

    @Override
    public void onNoteSelected(int note, boolean blow, float bend) {
        dbNote.setHole(note);
        dbNote.setBlow(blow);
        dbNote.setBend(bend);
        dbNote.setWord(wordField.getText().toString());
        if (editMode) {
            listener.onNoteEdited(dbNote, noteViewHolder);
        } else {
            listener.onNoteAdded(dbNote, newRow, rowViewHolder);
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

    public void setRowViewHolder(RowViewHolder rowViewHolder) {
        this.rowViewHolder = rowViewHolder;
    }

    public void setNoteViewHolder(NoteViewHolder noteViewHolder) {
        this.noteViewHolder = noteViewHolder;
    }
}
