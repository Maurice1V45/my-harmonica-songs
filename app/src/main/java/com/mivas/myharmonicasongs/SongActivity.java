package com.mivas.myharmonicasongs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.mivas.myharmonicasongs.database.handler.NoteDbHandler;
import com.mivas.myharmonicasongs.database.handler.SongDbHandler;
import com.mivas.myharmonicasongs.database.model.DbNote;
import com.mivas.myharmonicasongs.database.model.DbSong;
import com.mivas.myharmonicasongs.dialog.NotePickerDialog;
import com.mivas.myharmonicasongs.listener.SongActivityListener;
import com.mivas.myharmonicasongs.util.Constants;
import com.mivas.myharmonicasongs.util.CustomToast;
import com.mivas.myharmonicasongs.util.ExportHelper;
import com.mivas.myharmonicasongs.view.NoteRowOptionsMenu;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Activity that displays notes.
 */
public class SongActivity extends AppCompatActivity implements SongActivityListener {

    private LinearLayout notesLayout;
    private DbSong dbSong;
    private List<DbNote> notes = new ArrayList<DbNote>();
    private Comparator notesComparator;
    private TextView noNotesText;
    private List<DbNote> copiedNotes = new ArrayList<DbNote>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initViews();
        initComparator();

        dbSong = SongDbHandler.getSongById(getIntent().getLongExtra(Constants.EXTRA_SONG_ID, 0));
        notes = NoteDbHandler.getNotesBySongId(dbSong.getId());
        getSupportActionBar().setTitle(dbSong.getTitle());
        refreshNotesView();

    }

    /**
     * Views initializer.
     */
    private void initViews() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        notesLayout = (LinearLayout) findViewById(R.id.list_notes);
        noNotesText = (TextView) findViewById(R.id.text_no_notes);
    }

    /**
     * Redraws the whole view of notes.
     */
    private void refreshNotesView() {
        notesLayout.removeAllViews();
        if (notes.size() == 0) {
            LinearLayout rowLayout = new LinearLayout(SongActivity.this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            notesLayout.addView(rowLayout);
            addPlusToNotesView(0, 0, rowLayout);
        } else {
            int i = 0;
            DbNote thisNote = notes.get(i);
            DbNote nextNote = notes.size() < 2 ? null : notes.get(i + 1);
            LinearLayout rowLayout = new LinearLayout(SongActivity.this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            notesLayout.addView(rowLayout);
            while (i < notes.size()) {
                addNoteToNotesView(thisNote, rowLayout);
                if (nextNote == null || (nextNote.getRow() != thisNote.getRow())) {
                    addPlusToNotesView(thisNote.getRow(), thisNote.getColumn() + 1, rowLayout);
                    rowLayout = new LinearLayout(SongActivity.this);
                    rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                    notesLayout.addView(rowLayout);
                    if (i == notes.size() - 1) {
                        rowLayout = new LinearLayout(SongActivity.this);
                        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                        notesLayout.addView(rowLayout);
                        addPlusToNotesView(thisNote.getRow() + 1, 0, rowLayout);
                    }
                }
                i++;
                thisNote = i < (notes.size()) ? notes.get(i) : null;
                nextNote = i < (notes.size() - 1) ? notes.get(i + 1) : null;
            }
        }
        toggleNoNotesText();
    }

    /**
     * Adds a note to the notes view.
     *
     * @param dbNote
     * @param parent
     */
    private void addNoteToNotesView(final DbNote dbNote, LinearLayout parent) {
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
                NotePickerDialog dialog = new NotePickerDialog();
                dialog.setDbNote(dbNote);
                dialog.setListener(SongActivity.this);
                dialog.setEditMode(true);
                dialog.show(getFragmentManager(), Constants.TAG_HARMONICA_NOTES_DIALOG);
            }
        });
        parent.addView(noteView);
    }

    /**
     * Adds a plus button to the notes view.
     *
     * @param row
     * @param column
     * @param parent
     */
    private void addPlusToNotesView(final int row, final int column, LinearLayout parent) {
        View addNoteView = getLayoutInflater().inflate(R.layout.list_item_add_note, null);
        addNoteView.setClickable(true);
        boolean displayInsertRow = false;
        if (row < (getNumberOfRows() - 1)) {
            displayInsertRow = true;
        }

        // set long press listener with options menu
        final NoteRowOptionsMenu optionsMenu = new NoteRowOptionsMenu(SongActivity.this, addNoteView, column, copiedNotes.size(), displayInsertRow);
        optionsMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 0:
                        onAddNoteCommand(row, column);
                        break;
                    case 1:
                        onNewRowCommand(row);
                        break;
                    case 2:
                        onDeleteRowCommand(row);
                        break;
                    case 3:
                        onCopyRowCommand(row);
                        break;
                    case 4:
                        onPasteRowCommand(row, column);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        addNoteView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                optionsMenu.show();
                return true;
            }
        });

        // set click listener
        addNoteView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onAddNoteCommand(row, column);
            }
        });
        parent.addView(addNoteView);
    }

    /**
     * Adds a note to the position of the plus button.
     *
     * @param row
     * @param column
     */
    private void onAddNoteCommand(int row, int column) {
        DbNote dbNote = new DbNote();
        dbNote.setRow(row);
        dbNote.setColumn(column);
        dbNote.setSongId(dbSong.getId());
        NotePickerDialog dialog = new NotePickerDialog();
        dialog.setDbNote(dbNote);
        dialog.setListener(SongActivity.this);
        dialog.setEditMode(false);
        dialog.setNewRow(false);
        dialog.show(getFragmentManager(), Constants.TAG_HARMONICA_NOTES_DIALOG);
    }

    /**
     * Adds a note to the row after the clicked plus button.
     *
     * @param row
     */
    private void onNewRowCommand(int row) {
        DbNote dbNote = new DbNote();
        dbNote.setRow(row + 1);
        dbNote.setColumn(0);
        dbNote.setSongId(dbSong.getId());
        NotePickerDialog dialog = new NotePickerDialog();
        dialog.setDbNote(dbNote);
        dialog.setListener(SongActivity.this);
        dialog.setEditMode(false);
        dialog.setNewRow(true);
        dialog.show(getFragmentManager(), Constants.TAG_HARMONICA_NOTES_DIALOG);
    }

    /**
     * Deletes the current row.
     *
     * @param row
     */
    private void onDeleteRowCommand(int row) {
        Iterator<DbNote> iterator = notes.iterator();
        while (iterator.hasNext()) {
            DbNote note = iterator.next();
            if (note.getRow() == row) {
                iterator.remove();
                NoteDbHandler.deleteNote(note);
            }
        }
        decrementRows(row);
        refreshNotesView();
    }

    /**
     * Copies the current row into a temporary list.
     *
     * @param row
     */
    private void onCopyRowCommand(int row) {
        copiedNotes.clear();
        for (DbNote note : notes) {
            if (note.getRow() == row) {
                copiedNotes.add(new DbNote(note));
            }
        }
        int copedNotesNr = copiedNotes.size();
        if (copedNotesNr == 1) {
            CustomToast.makeText(SongActivity.this, copedNotesNr + " " + getString(R.string.toast_note_copied), Toast.LENGTH_SHORT).show();
        } else {
            CustomToast.makeText(SongActivity.this, copedNotesNr + " " + getString(R.string.toast_notes_copied), Toast.LENGTH_SHORT).show();
        }
        refreshNotesView();
    }

    /**
     * Pastes the copied notes to the current position.
     *
     * @param row
     * @param column
     */
    private void onPasteRowCommand(int row, int column) {
        if (copiedNotes.isEmpty()) {
            Toast.makeText(SongActivity.this, R.string.toast_no_notes_copied, Toast.LENGTH_SHORT).show();
        } else {
            int currentColumn = column;
            for (DbNote copiedNote : copiedNotes) {
                DbNote pastedNote = new DbNote(copiedNote);
                pastedNote.setRow(row);
                pastedNote.setColumn(currentColumn);
                notes.add(pastedNote);
                NoteDbHandler.insertNote(pastedNote);
                currentColumn++;
            }
            Collections.sort(notes, notesComparator);
            refreshNotesView();
        }
    }

    @Override
    public void onNoteAdded(DbNote dbNote, boolean newRow) {
        if (newRow) {
            incrementRows(dbNote.getRow());
        }
        notes.add(dbNote);
        NoteDbHandler.insertNote(dbNote);
        Collections.sort(notes, notesComparator);
        refreshNotesView();
    }

    @Override
    public void onNoteEdited(DbNote dbNote) {
        NoteDbHandler.insertNote(dbNote);
        refreshNotesView();
    }

    @Override
    public void onNoteDeleted(DbNote dbNote) {
        int position = notes.indexOf(dbNote);
        notes.remove(dbNote);
        NoteDbHandler.deleteNote(dbNote);
        int rowCount = 0;
        for (int i = 0; i < notes.size(); i++) {
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
        refreshNotesView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_song_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_export_song:
                List<DbSong> dbSongs = new ArrayList<DbSong>();
                dbSongs.add(dbSong);
                ExportHelper.getInstance().launchExportIntent(SongActivity.this, dbSongs, dbSong.getTitle() + ".mhs");
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * Initializes the comparator which sorts the notes ascendantly.
     */
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

    /**
     * Displays the No Notes text if there are currently no notes.
     */
    private void toggleNoNotesText() {
        if (notes.isEmpty()) {
            noNotesText.setVisibility(View.VISIBLE);
        } else {
            noNotesText.setVisibility(View.GONE);
        }
    }

    /**
     * Increments rows by 1, starting with the specified row and going up.
     *
     * @param row The row from where to start the incrementation.
     */
    private void incrementRows(int row) {
        for (DbNote note : notes) {
            int noteRow = note.getRow();
            if (noteRow >= row) {
                note.setRow(noteRow + 1);
                NoteDbHandler.insertNote(note);
            }
        }
    }

    /**
     * Decrements rows by 1, starting with the specified row and going up.
     *
     * @param row The row from where to start the decrementation.
     */
    private void decrementRows(int row) {
        for (DbNote note : notes) {
            int noteRow = note.getRow();
            if (noteRow >= row) {
                note.setRow(noteRow - 1);
                NoteDbHandler.insertNote(note);
            }
        }
    }

    /**
     * Retrieves the current number of rows.
     *
     * @return The number of rows.
     */
    private int getNumberOfRows() {
        if (notes.isEmpty()) {
            return 0;
        } else {
            return notes.get(notes.size() - 1).getRow() + 1;
        }
    }

}
