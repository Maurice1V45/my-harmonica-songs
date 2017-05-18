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
        //setDummyNotes();
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
        notesLayout.invalidate();
    }

    private void setDummyNotes() {
        DbNote note1 = new DbNote();
        note1.setHole(4);
        note1.setBlow(true);
        note1.setRow(0);
        note1.setColumn(0);
        note1.setWord("When");
        note1.setSongId(dbSong.getId());
        DbNote note2 = new DbNote();
        note2.setHole(5);
        note2.setBlow(true);
        note2.setRow(0);
        note2.setColumn(1);
        note2.setWord("I");
        note2.setSongId(dbSong.getId());
        DbNote note3 = new DbNote();
        note3.setHole(6);
        note3.setBlow(true);
        note3.setRow(0);
        note3.setColumn(2);
        note3.setWord("am");
        note3.setSongId(dbSong.getId());
        DbNote note4 = new DbNote();
        note4.setHole(4);
        note4.setBlow(false);
        note4.setRow(1);
        note4.setColumn(0);
        note4.setWord("down");
        note4.setSongId(dbSong.getId());
        notes.add(note1);
        notes.add(note2);
        notes.add(note3);
        notes.add(note4);
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
                dialog.show(getFragmentManager(), Constants.TAG_HARMONICA_NOTES_DIALOG);
            }
        });
        parent.addView(addNoteView);
    }

    @Override
    public void onNoteAdded(DbNote dbNote) {
        NoteDbHandler.insertNote(dbNote);
        notes.add(dbNote);
        refreshMatrix();
    }
}
