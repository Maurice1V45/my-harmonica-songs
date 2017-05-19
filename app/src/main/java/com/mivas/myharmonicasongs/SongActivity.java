package com.mivas.myharmonicasongs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mivas.myharmonicasongs.database.handler.NoteDbHandler;
import com.mivas.myharmonicasongs.database.handler.SongDbHandler;
import com.mivas.myharmonicasongs.database.model.DbNote;
import com.mivas.myharmonicasongs.database.model.DbSong;
import com.mivas.myharmonicasongs.dialog.HarmonicaNotesDialog;
import com.mivas.myharmonicasongs.listener.SongActivityListener;
import com.mivas.myharmonicasongs.util.Constants;

import java.util.ArrayList;
import java.util.List;


public class SongActivity extends AppCompatActivity implements SongActivityListener {

    private LinearLayout notesLayout;
    private DbSong dbSong;
    private List<DbNote> notes = new ArrayList<DbNote>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);
        initViews();

        dbSong = SongDbHandler.getSongById(getIntent().getLongExtra(Constants.EXTRA_SONG_ID, 0));
        notes = NoteDbHandler.getNotesBySongId(dbSong.getId());
        refreshMatrix();

    }

    private void initViews() {
        notesLayout = (LinearLayout) findViewById(R.id.list_notes);
    }

    private void refreshMatrix() {
        notesLayout.removeAllViews();
        int i = 0;
        boolean matrixEnd = false;
        do {
            LinearLayout rowLayout = new LinearLayout(SongActivity.this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            notesLayout.addView(rowLayout);
            int j = 0;
            boolean rowEnd = false;
            do {
                DbNote dbNote = findNote(i, j);
                if (dbNote != null) {
                    addNoteToMatrix(dbNote, rowLayout);
                    j++;
                } else {
                    addAddNoteToMatrix(i, j, rowLayout);
                    rowEnd = true;
                }
            } while (!rowEnd);
            if (rowLayout.getChildCount() == 1) {
                matrixEnd = true;
            }
            i++;
        } while (!matrixEnd);
    }

    private DbNote findNote(int row, int column) {
        for (DbNote dbNote : notes) {
            if (dbNote.getRow() == row && dbNote.getColumn() == column) {
                return dbNote;
            }
        }
        return null;
    }

    private void addNoteToMatrix(final DbNote dbNote, LinearLayout parent) {
        View noteView = getLayoutInflater().inflate(R.layout.list_item_note, null);
        TextView noteText = (TextView) noteView.findViewById(R.id.text_note);
        noteText.setText(dbNote.isBlow() ? String.valueOf(dbNote.getHole()) : "-" + String.valueOf(dbNote.getHole()));
        TextView wordText = (TextView) noteView.findViewById(R.id.text_word);
        if (dbNote.getWord().isEmpty()) {
            wordText.setVisibility(View.GONE);
        } else {
            wordText.setVisibility(View.VISIBLE);
            wordText.setText(dbNote.getWord());
        }
        noteView.setClickable(true);
        noteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HarmonicaNotesDialog dialog = new HarmonicaNotesDialog();
                dialog.setDbNote(dbNote);
                dialog.setListener(SongActivity.this);
                dialog.setEditMode(true);
                dialog.show(getFragmentManager(), Constants.TAG_HARMONICA_NOTES_DIALOG);
            }
        });
        parent.addView(noteView);
    }

    private void addAddNoteToMatrix(final int row, final int column, LinearLayout parent) {
        View addNoteView = getLayoutInflater().inflate(R.layout.list_item_add_note, null);
        addNoteView.setClickable(true);
        addNoteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbNote dbNote = new DbNote();
                dbNote.setRow(row);
                dbNote.setColumn(column);
                dbNote.setSongId(dbSong.getId());
                HarmonicaNotesDialog dialog = new HarmonicaNotesDialog();
                dialog.setDbNote(dbNote);
                dialog.setListener(SongActivity.this);
                dialog.setEditMode(false);
                dialog.show(getFragmentManager(), Constants.TAG_HARMONICA_NOTES_DIALOG);
            }
        });
        parent.addView(addNoteView);
    }

    @Override
    public void onNoteAdded(DbNote dbNote) {
        notes.add(dbNote);
        NoteDbHandler.insertNote(dbNote);
        refreshMatrix();
    }

    @Override
    public void onNoteEdited(DbNote dbNote) {
        NoteDbHandler.insertNote(dbNote);
        refreshMatrix();
    }

    @Override
    public void onNoteDeleted(DbNote dbNote) {
        notes.remove(dbNote);
        NoteDbHandler.deleteNote(dbNote);
        refreshMatrix();
    }
}
