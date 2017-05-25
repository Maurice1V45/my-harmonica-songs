package com.mivas.myharmonicasongs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.mivas.myharmonicasongs.database.handler.NoteDbHandler;
import com.mivas.myharmonicasongs.database.handler.SongDbHandler;
import com.mivas.myharmonicasongs.database.model.DbNote;
import com.mivas.myharmonicasongs.database.model.DbSong;
import com.mivas.myharmonicasongs.dialog.HarmonicaNotesDialog;
import com.mivas.myharmonicasongs.listener.SongActivityListener;
import com.mivas.myharmonicasongs.util.Constants;
import com.mivas.myharmonicasongs.view.NoteRowOptionsMenu;
import com.mivas.myharmonicasongs.view.SongOptionsMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class SongActivity extends AppCompatActivity implements SongActivityListener {

    private LinearLayout notesLayout;
    private DbSong dbSong;
    private List<DbNote> notes = new ArrayList<DbNote>();
    private Comparator notesComparator;
    private TextView noNotesText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initViews();
        initComparator();

        dbSong = SongDbHandler.getSongById(getIntent().getLongExtra(Constants.EXTRA_SONG_ID, 0));
        getSupportActionBar().setTitle(dbSong.getTitle());
        notes = NoteDbHandler.getNotesBySongId(dbSong.getId());
        refreshMatrix();

    }

    private void initViews() {
        notesLayout = (LinearLayout) findViewById(R.id.list_notes);
        noNotesText = (TextView) findViewById(R.id.text_no_notes);
    }

    private void refreshMatrix() {
        notesLayout.removeAllViews();
        if (notes.size() == 0) {
            LinearLayout rowLayout = new LinearLayout(SongActivity.this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            notesLayout.addView(rowLayout);
            addAddNoteToMatrix(0, 0, rowLayout);
        } else {
            int i = 0;
            DbNote thisNote = notes.get(i);
            DbNote nextNote = notes.size() < 2 ? null : notes.get(i + 1);
            LinearLayout rowLayout = new LinearLayout(SongActivity.this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            notesLayout.addView(rowLayout);
            while (i < notes.size()) {
                addNoteToMatrix(thisNote, rowLayout);
                if (nextNote == null || (nextNote.getRow() != thisNote.getRow())) {
                    addAddNoteToMatrix(thisNote.getRow(), thisNote.getColumn() + 1, rowLayout);
                    rowLayout = new LinearLayout(SongActivity.this);
                    rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                    notesLayout.addView(rowLayout);
                    if (i == notes.size() - 1) {
                        rowLayout = new LinearLayout(SongActivity.this);
                        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                        notesLayout.addView(rowLayout);
                        addAddNoteToMatrix(thisNote.getRow() + 1, 0, rowLayout);
                    }
                }
                i++;
                thisNote = i < (notes.size()) ? notes.get(i) : null;
                nextNote = i < (notes.size() - 1) ? notes.get(i + 1) : null;
            }
        }
        toggleNoNotesText();
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
        final NoteRowOptionsMenu optionsMenu = new NoteRowOptionsMenu(SongActivity.this, addNoteView);
        optionsMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 0:
                        //listener.onSongEdit(dbSong);
                        break;
                    case 1:
                        //listener.onSongDelete(dbSong);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        /*addNoteView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                optionsMenu.show();
                return false;
            }
        });*/
        parent.addView(addNoteView);
    }

    @Override
    public void onNoteAdded(DbNote dbNote) {
        notes.add(dbNote);
        Collections.sort(notes, notesComparator);
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
        int position = notes.indexOf(dbNote);
        notes.remove(dbNote);
        NoteDbHandler.deleteNote(dbNote);
        int rowCount = 0;
        for (int i=0; i < notes.size(); i++) {
            if (notes.get(i).getRow() == dbNote.getRow()) {
                rowCount++;
            }
        }
        if (rowCount > 0) {
            for (int i = position; i < notes.size(); i++) {
                if (notes.get(i).getRow() == dbNote.getRow()) {
                    DbNote note = notes.get(i);
                    note.setColumn(note.getColumn() - 1);
                    NoteDbHandler.insertNote(note);
                }
            }
        } else {
            for (int i = position; i < notes.size(); i++) {
                if (notes.get(i).getRow() > dbNote.getRow()) {
                    DbNote note = notes.get(i);
                    note.setRow(note.getRow() - 1);
                    NoteDbHandler.insertNote(note);
                }
            }
        }
        refreshMatrix();
    }

    private void initComparator() {
        notesComparator = new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                DbNote note1 = (DbNote) o1;
                DbNote note2 = (DbNote) o2;
                if (note1.getRow() < note2.getRow()) {
                    return -1;
                } else if (note1.getRow() > note2.getRow()) {
                    return 1;
                } else {
                    if (note1.getColumn() < note2.getColumn()) {
                        return -1;
                    } else if (note1.getColumn() > note2.getColumn()) {
                        return 1;
                    }
                }
                return 0;
            }
        };
    }

    private void toggleNoNotesText() {
        if (notes.isEmpty()) {
            noNotesText.setVisibility(View.VISIBLE);
        } else {
            noNotesText.setVisibility(View.GONE);
        }
    }

}
