package com.mivas.myharmonicasongs;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mivas.myharmonicasongs.database.handler.NoteDbHandler;
import com.mivas.myharmonicasongs.database.handler.SectionDbHandler;
import com.mivas.myharmonicasongs.database.handler.SongDbHandler;
import com.mivas.myharmonicasongs.database.model.DbNote;
import com.mivas.myharmonicasongs.database.model.DbSection;
import com.mivas.myharmonicasongs.database.model.DbSong;
import com.mivas.myharmonicasongs.dialog.NotePickerDialog;
import com.mivas.myharmonicasongs.dialog.SectionDialog;
import com.mivas.myharmonicasongs.listener.SongActivityListener;
import com.mivas.myharmonicasongs.util.Constants;
import com.mivas.myharmonicasongs.util.CustomToast;
import com.mivas.myharmonicasongs.util.ExportHelper;

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
    private List<DbSection> sections = new ArrayList<DbSection>();
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
        sections = SectionDbHandler.getSectionsBySongId(dbSong.getId());
        getSupportActionBar().setTitle(dbSong.getTitle());
        drawNotes();

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
    private void drawNotes() {

        // clear all views first
        notesLayout.removeAllViews();
        if (notes.size() == 0) {

            // display only 1 plus note
            LinearLayout rowLayout = new LinearLayout(SongActivity.this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            notesLayout.addView(rowLayout);
            addPlusToNotesView(0, 0, rowLayout);
        } else {

            // i represents the index of the note in the notes list
            int i = 0;

            // store current note and the next note in the list
            DbNote thisNote = notes.get(i);
            DbNote nextNote = notes.size() < 2 ? null : notes.get(i + 1);

            // check if row has a section and display it
            DbSection rowSection = getSectionByRow(thisNote.getRow());
            if (rowSection != null) {
                addSectionToNotesView(rowSection, notesLayout);
            }

            // init and add a horizontal linear layout for the notes on this row
            LinearLayout rowLayout = new LinearLayout(SongActivity.this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            notesLayout.addView(rowLayout);

            while (i < notes.size()) {

                // add the current note to the horizontal linear layout
                addNoteToNotesView(thisNote, rowLayout);

                // if there is no next note or if this note is the last on this row
                if (nextNote == null || (nextNote.getRow() != thisNote.getRow())) {

                    // add a plus note
                    addPlusToNotesView(thisNote.getRow(), thisNote.getColumn() + 1, rowLayout);
                    if (nextNote != null) {
                        rowSection = getSectionByRow(nextNote.getRow());
                        if (rowSection != null) {
                            addSectionToNotesView(rowSection, notesLayout);
                        }
                    }

                    // create a new horizontal linear layout
                    rowLayout = new LinearLayout(SongActivity.this);
                    rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                    notesLayout.addView(rowLayout);

                    // if there are no more notes
                    if (i == notes.size() - 1) {

                        // create a new horizontal linear layout and add a plus note on it
                        rowLayout = new LinearLayout(SongActivity.this);
                        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                        notesLayout.addView(rowLayout);
                        addPlusToNotesView(thisNote.getRow() + 1, 0, rowLayout);
                    }
                }

                // increment the current note and the next note
                i++;
                thisNote = i < (notes.size()) ? notes.get(i) : null;
                nextNote = i < (notes.size() - 1) ? notes.get(i + 1) : null;
            }
        }

        // display no notes text if the notes list is empty
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
        noteText.setText(getNoteText(dbNote.getHole(), dbNote.isBlow(), dbNote.getBend()));
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
        boolean overLastRow = (row > (getNumberOfRows() - 1));
        DbSection dbSection = getSectionByRow(row);

        // set long press listener with options menu
        final MenuBuilder menuBuilder = new MenuBuilder(SongActivity.this);
        menuBuilder.setOptionalIconsVisible(true);
        MenuInflater inflater = new MenuInflater(SongActivity.this);
        inflater.inflate(R.menu.menu_note_options, menuBuilder);
        final MenuPopupHelper optionsMenu = new MenuPopupHelper(SongActivity.this, menuBuilder, addNoteView);
        menuBuilder.findItem(R.id.action_delete_row).setVisible(column != 0);
        menuBuilder.findItem(R.id.action_insert_row).setVisible(!overLastRow);
        menuBuilder.findItem(R.id.action_copy_row).setVisible(column != 0);
        menuBuilder.findItem(R.id.action_paste_row).setVisible(copiedNotes.size() != 0);
        menuBuilder.findItem(R.id.action_add_section).setVisible(dbSection == null && !overLastRow);
        menuBuilder.findItem(R.id.action_edit_section).setVisible(dbSection != null);
        menuBuilder.findItem(R.id.action_delete_section).setVisible(dbSection != null);
        menuBuilder.setCallback(new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_add_note:
                        onAddNoteCommand(row, column);
                        break;
                    case R.id.action_delete_row:
                        onDeleteRowCommand(row);
                        break;
                    case R.id.action_insert_row:
                        onNewRowCommand(row);
                        break;
                    case R.id.action_copy_row:
                        onCopyRowCommand(row);
                        break;
                    case R.id.action_paste_row:
                        onPasteRowCommand(row, column);
                        break;
                    case R.id.action_add_section:
                        onAddSectionCommand(row);
                        break;
                    case R.id.action_edit_section:
                        onEditSectionCommand(row);
                        break;
                    case R.id.action_delete_section:
                        onDeleteSectionCommand(row);
                        break;
                    default:
                        break;
                }
                return false;
            }

            @Override
            public void onMenuModeChange(MenuBuilder menu) {

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
     * Adds a section to the notes view.
     *
     * @param dbSection
     */
    private void addSectionToNotesView(DbSection dbSection, LinearLayout parent) {
        TextView sectionView = (TextView) getLayoutInflater().inflate(R.layout.list_item_section, null);
        sectionView.setText(dbSection.getName());
        sectionView.setTextColor(ContextCompat.getColor(SongActivity.this, R.color.black));
        parent.addView(sectionView);
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

        // delete all notes on the row
        Iterator<DbNote> iterator = notes.iterator();
        while (iterator.hasNext()) {
            DbNote note = iterator.next();
            if (note.getRow() == row) {
                iterator.remove();
                NoteDbHandler.deleteNote(note);
            }
        }

        // delete the section if there is one
        DbSection dbSection = getSectionByRow(row);
        if (dbSection != null) {
            sections.remove(dbSection);
            SectionDbHandler.deleteSection(dbSection);
        }

        // decrement rows and sections
        decrementRows(row);
        decrementSections(row);
        drawNotes();
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
        drawNotes();
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
            drawNotes();
        }
    }

    /**
     * Opens the add section dialog.
     */
    private void onAddSectionCommand(int row) {
        SectionDialog sectionDialog = new SectionDialog();
        DbSection dbSection = new DbSection();
        dbSection.setRow(row);
        dbSection.setSongId(dbSong.getId());
        sectionDialog.setDbSection(dbSection);
        sectionDialog.setListener(SongActivity.this);
        sectionDialog.setEditMode(false);
        sectionDialog.show(getFragmentManager(), Constants.TAG_SECTION_DIALOG);
    }

    /**
     * Opens the edit section dialog.
     */
    private void onEditSectionCommand(int row) {
        DbSection dbSection = getSectionByRow(row);
        if (dbSection != null) {
            SectionDialog sectionDialog = new SectionDialog();
            sectionDialog.setDbSection(dbSection);
            sectionDialog.setListener(SongActivity.this);
            sectionDialog.setEditMode(true);
            sectionDialog.show(getFragmentManager(), Constants.TAG_SECTION_DIALOG);
        }
    }

    private void onDeleteSectionCommand(int row) {
        DbSection dbSection = getSectionByRow(row);
        if (dbSection != null) {
            sections.remove(dbSection);
            SectionDbHandler.deleteSection(dbSection);
        }
        drawNotes();
    }

    @Override
    public void onNoteAdded(DbNote dbNote, boolean newRow) {
        if (newRow) {
            incrementRows(dbNote.getRow());
            incrementSections(dbNote.getRow());
        }
        notes.add(dbNote);
        NoteDbHandler.insertNote(dbNote);
        Collections.sort(notes, notesComparator);
        drawNotes();
    }

    @Override
    public void onNoteEdited(DbNote dbNote) {
        NoteDbHandler.insertNote(dbNote);
        drawNotes();
    }

    @Override
    public void onNoteDeleted(DbNote dbNote) {

        // delete the note
        int position = notes.indexOf(dbNote);
        notes.remove(dbNote);
        NoteDbHandler.deleteNote(dbNote);

        // check the remaining notes on the row
        int rowCount = 0;
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getRow() == dbNote.getRow()) {
                rowCount++;
            }
        }

        if (rowCount > 0) {

            // decrement the column of the notes to the right of the deleted note
            for (int i = position; i < notes.size(); i++) {
                if (notes.get(i).getRow() == dbNote.getRow()) {
                    DbNote note = notes.get(i);
                    note.setColumn(note.getColumn() - 1);
                    NoteDbHandler.insertNote(note);
                }
            }
        } else {

            // delete the section on this row
            DbSection dbSection = getSectionByRow(dbNote.getRow());
            if (dbSection != null) {
                sections.remove(dbSection);
                SectionDbHandler.deleteSection(dbSection);
            }

            // decrement the row of the notes below the deleted note
            for (int i = position; i < notes.size(); i++) {
                if (notes.get(i).getRow() > dbNote.getRow()) {
                    DbNote note = notes.get(i);
                    note.setRow(note.getRow() - 1);
                    NoteDbHandler.insertNote(note);
                }
            }

            // decrement the row of the sections below the deleted note
            decrementSections(dbNote.getRow());
        }

        drawNotes();
    }

    @Override
    public void onSectionAdded(DbSection dbSection) {
        sections.add(dbSection);
        SectionDbHandler.insertSection(dbSection);
        drawNotes();
    }

    @Override
    public void onSectionEdit(DbSection dbSection) {
        SectionDbHandler.insertSection(dbSection);
        drawNotes();
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

    private void incrementSections(int row) {
        for (DbSection dbSection : sections) {
            if (dbSection.getRow() >= row) {
                dbSection.setRow(dbSection.getRow() + 1);
                SectionDbHandler.insertSection(dbSection);
            }
        }
    }

    private void decrementSections(int row) {
        if (notes.isEmpty()) {

            // delete all sections
            for (DbSection dbSection : sections) {
                SectionDbHandler.deleteSection(dbSection);
            }
            sections.clear();

        } else {

            // decrement sections after the specified row
            for (DbSection dbSection : sections) {
                if (dbSection.getRow() > row) {
                    dbSection.setRow(dbSection.getRow() - 1);
                    SectionDbHandler.insertSection(dbSection);
                }
            }

            // search for possible multiple sections on the same row
            List<DbSection> duplicateSections = new ArrayList<DbSection>();
            for (DbSection dbSection : sections) {
                if (dbSection.getRow() == row) {
                    duplicateSections.add(dbSection);
                }
            }

            // leave only 1 section on the row and delete the others
            if (duplicateSections.size() > 1) {
                for (int i = 1; i < duplicateSections.size(); i++) {
                    sections.remove(duplicateSections.get(i));
                    SectionDbHandler.deleteSection(duplicateSections.get(i));
                }
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

    private String getNoteText(int hole, boolean blow, float bending) {
        if (blow) {
            if (bending == 0f) {
                return String.format("%s", hole);
            } else if (bending == 0.5f) {
                return String.format("%s'", hole);
            } else if (bending == 1f) {
                return String.format("%s''", hole);
            }
        } else {
            if (bending == 0f) {
                return String.format("-%s", hole);
            } else if (bending == -0.5f) {
                return String.format("-%s'", hole);
            } else if (bending == -1f) {
                return String.format("-%s''", hole);
            } else if (bending == -1.5f) {
                return String.format("-%s'''", hole);
            }
        }
        return "";
    }

    /**
     * Searches if there is a section on the specified row and returns it.
     *
     * @param row
     * @return
     */
    private DbSection getSectionByRow(int row) {
        for (DbSection dbSection : sections) {
            if (dbSection.getRow() == row) {
                return dbSection;
            }
        }
        return null;
    }

}
