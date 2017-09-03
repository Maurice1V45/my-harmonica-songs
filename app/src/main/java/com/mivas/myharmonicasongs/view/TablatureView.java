package com.mivas.myharmonicasongs.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.database.handler.NoteDbHandler;
import com.mivas.myharmonicasongs.database.handler.SectionDbHandler;
import com.mivas.myharmonicasongs.database.model.DbNote;
import com.mivas.myharmonicasongs.database.model.DbSection;
import com.mivas.myharmonicasongs.database.model.DbSong;
import com.mivas.myharmonicasongs.dialog.NotePickerDialog;
import com.mivas.myharmonicasongs.dialog.SectionDialog;
import com.mivas.myharmonicasongs.listener.NotePickerDialogListener;
import com.mivas.myharmonicasongs.listener.SectionDialogListener;
import com.mivas.myharmonicasongs.listener.SongActivityListener;
import com.mivas.myharmonicasongs.model.Cell;
import com.mivas.myharmonicasongs.model.CellLine;
import com.mivas.myharmonicasongs.model.SectionCell;
import com.mivas.myharmonicasongs.util.Constants;
import com.mivas.myharmonicasongs.util.CustomToast;
import com.mivas.myharmonicasongs.util.CustomizationUtils;
import com.mivas.myharmonicasongs.util.DimensionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class TablatureView extends ScrollView implements NotePickerDialogListener, SectionDialogListener {

    private Context context;
    private LinearLayout parentLayout;
    private List<DbNote> dbNotes;
    private List<DbNote> copiedNotes = new ArrayList<DbNote>();
    private List<DbSection> dbSections;
    private DbSong dbSong;
    private List<CellLine> cellLines = new ArrayList<CellLine>();
    private Comparator notesComparator;
    private SongActivityListener songActivityListener;

    //customizations
    private int blowSign;
    private int blowStyle;
    private int blowTextColor;
    private int blowBackgroundColor;
    private int drawSign;
    private int drawStyle;
    private int drawTextColor;
    private int drawBackgroundColor;
    private int sectionStyle;
    private int sectionTextColor;

    // measures
    private int MEASURE_CELL_WIDTH;
    private int MEASURE_CELL_HEIGHT;
    private int MEASURE_CELL_MARGIN;
    private int MEASURE_NOTE_TEXT_SIZE;
    private int MEASURE_NOTE_WORD_SIZE;
    private int MEASURE_PLUS_SIZE;
    private int MEASURE_SECTION_PADDING_TOP;
    private int MEASURE_SECTION_PADDING_LEFT;
    private int MEASURE_SECTION_PADDING_BOTTOM;
    private int MEASURE_SECTION_SIZE;
    private int MEASURE_TABLATURE_TOP_PADDING;
    private int MEASURE_TABLATURE_BOTTOM_PADDING;
    private int MEASURE_TABLATURE_BOTTOM_PADDING_WITH_MEDIA;

    public TablatureView(Context context) {
        super(context);
        this.context = context;
        initViews();
    }

    public TablatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initViews();
    }

    public TablatureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initViews();
    }

    @SuppressLint("NewApi")
    public TablatureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        initViews();
    }

    public void setDbNotes(List<DbNote> dbNotes) {
        this.dbNotes = dbNotes;
    }

    public void setDbSong(DbSong dbSong) {
        this.dbSong = dbSong;
    }

    public void setDbSections(List<DbSection> dbSections) {
        this.dbSections = dbSections;
    }

    public void setSongActivityListener(SongActivityListener songActivityListener) {
        this.songActivityListener = songActivityListener;
    }

    private void initViews() {
        initMeasures();
        initCustomizations();
        initComparator();

        // init the scrollview
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
        setPadding(0, MEASURE_TABLATURE_TOP_PADDING, 0, MEASURE_TABLATURE_BOTTOM_PADDING);
        setClipToPadding(false);
        setVerticalScrollBarEnabled(false);

        // init horizontalscrollview
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(context);
        RelativeLayout.LayoutParams horizontalScrollLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        horizontalScrollView.setLayoutParams(horizontalScrollLayoutParams);
        horizontalScrollView.setPadding(MEASURE_TABLATURE_TOP_PADDING, 0, MEASURE_TABLATURE_TOP_PADDING, 0);
        horizontalScrollView.setClipToPadding(false);
        horizontalScrollView.setHorizontalScrollBarEnabled(false);
        addView(horizontalScrollView);

        // init parent layout
        parentLayout = new LinearLayout(context);
        RelativeLayout.LayoutParams parentLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        parentLayout.setLayoutParams(parentLayoutParams);
        parentLayout.setOrientation(LinearLayout.VERTICAL);
        horizontalScrollView.addView(parentLayout);
    }

    private void initMeasures() {
        MEASURE_CELL_WIDTH = DimensionUtils.dpToPx(context, 48);
        MEASURE_CELL_HEIGHT = DimensionUtils.dpToPx(context, 66);
        MEASURE_CELL_MARGIN = DimensionUtils.dpToPx(context, 3);
        MEASURE_NOTE_TEXT_SIZE = 24;
        MEASURE_NOTE_WORD_SIZE = 10;
        MEASURE_PLUS_SIZE = DimensionUtils.dpToPx(context, 42);
        MEASURE_SECTION_PADDING_TOP = DimensionUtils.dpToPx(context, 24);
        MEASURE_SECTION_PADDING_LEFT = DimensionUtils.dpToPx(context, 12);
        MEASURE_SECTION_PADDING_BOTTOM = DimensionUtils.dpToPx(context, 4);
        MEASURE_SECTION_SIZE = 24;
        MEASURE_TABLATURE_TOP_PADDING = DimensionUtils.dpToPx(context, 3);
        MEASURE_TABLATURE_BOTTOM_PADDING = DimensionUtils.dpToPx(context, 3);
        MEASURE_TABLATURE_BOTTOM_PADDING_WITH_MEDIA = DimensionUtils.dpToPx(context, 75);
    }

    public void initCustomizations() {
        blowSign = CustomizationUtils.getBlowSign();
        blowStyle = CustomizationUtils.getBlowStyle();
        blowTextColor = CustomizationUtils.getBlowTextColor();
        blowBackgroundColor = CustomizationUtils.getBlowBackgroundColor();
        drawSign = CustomizationUtils.getDrawSign();
        drawStyle = CustomizationUtils.getDrawStyle();
        drawTextColor = CustomizationUtils.getDrawTextColor();
        drawBackgroundColor = CustomizationUtils.getDrawBackgroundColor();
        sectionStyle = CustomizationUtils.getSectionStyle();
        sectionTextColor = CustomizationUtils.getSectionTextColor();
    }

    public void initialize() {

        // clear all views first
        cellLines.clear();
        parentLayout.removeAllViews();
        if (dbNotes.size() == 0) {

            // display only 1 plus note
            LinearLayout row = new LinearLayout(context);
            row.setOrientation(LinearLayout.HORIZONTAL);
            parentLayout.addView(row);
            CellLine cellLine = new CellLine(new ArrayList<Cell>(), row);
            cellLines.add(cellLine);
            addPlusCell(cellLine);
        } else {

            // i represents the index of the note in the notes list
            int i = 0;

            // store current note and the next note in the list
            DbNote thisNote = dbNotes.get(i);
            DbNote nextNote = dbNotes.size() < 2 ? null : dbNotes.get(i + 1);

            // init and add a horizontal linear layout for the notes on this row
            LinearLayout row = new LinearLayout(context);
            row.setOrientation(LinearLayout.HORIZONTAL);
            parentLayout.addView(row);
            CellLine cellLine = new CellLine(new ArrayList<Cell>(), row);
            cellLines.add(cellLine);

            // check if row has a section and display it
            DbSection dbSection = getSectionByRow(thisNote.getRow());
            if (dbSection != null) {
                addSectionCell(dbSection, cellLine);
            }

            while (i < dbNotes.size()) {

                // add the current note to the horizontal linear layout
                addNoteCell(thisNote, cellLine);

                // if there is no next note or if this note is the last on this row
                if (nextNote == null || (nextNote.getRow() != thisNote.getRow())) {

                    // add a plus note
                    addPlusCell(cellLine);

                    // create a new horizontal linear layout
                    row = new LinearLayout(context);
                    row.setOrientation(LinearLayout.HORIZONTAL);
                    parentLayout.addView(row);
                    cellLine = new CellLine(new ArrayList<Cell>(), row);
                    cellLines.add(cellLine);

                    // add section if any
                    if (nextNote != null) {
                        dbSection = getSectionByRow(nextNote.getRow());
                        if (dbSection != null) {
                            addSectionCell(dbSection, cellLine);
                        }
                    }

                    // if there are no more notes
                    if (i == dbNotes.size() - 1) {

                        // add a last plus
                        addPlusCell(cellLine);
                    }
                }

                // increment the current note and the next note
                i++;
                thisNote = i < (dbNotes.size()) ? dbNotes.get(i) : null;
                nextNote = i < (dbNotes.size() - 1) ? dbNotes.get(i + 1) : null;
            }
        }

        // display no notes text if the notes list is empty
        //toggleNoNotesText();
    }

    private void addPlusCell(final CellLine cellLine) {

        // set add note layout properties
        final RelativeLayout parent = new RelativeLayout(context);
        LinearLayout.LayoutParams parentLayoutParams = new LinearLayout.LayoutParams(MEASURE_CELL_WIDTH, MEASURE_CELL_HEIGHT);
        parentLayoutParams.setMargins(MEASURE_CELL_MARGIN, MEASURE_CELL_MARGIN, MEASURE_CELL_MARGIN, MEASURE_CELL_MARGIN);
        parent.setLayoutParams(parentLayoutParams);
        parent.setClickable(true);

        // set plus image properties
        ImageView plusImage = new ImageView(context);
        RelativeLayout.LayoutParams plusImageLayoutParams = new RelativeLayout.LayoutParams(MEASURE_PLUS_SIZE, MEASURE_PLUS_SIZE);
        plusImageLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        plusImage.setLayoutParams(plusImageLayoutParams);
        plusImage.setScaleType(ImageView.ScaleType.FIT_XY);
        plusImage.setImageResource(R.drawable.selector_round_plus);
        parent.addView(plusImage);

        // add view holder
        cellLine.getLayout().addView(parent);
        final Cell cell = new Cell(null, parent);
        cellLine.addCell(cell);

        parent.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                int row = 0;
                int column = 0;
                boolean isLastPlus = false; // last plus in the tablature

                List<Cell> cells = cellLine.getCells();
                if (cells.size() <= 1) {

                    // this is the last plus in the tab
                    isLastPlus = true;
                    row = cellLines.size() - 1;
                } else {

                    // get last note in the row
                    Cell lastNoteCell = cells.get(cells.size() - 2);
                    row = lastNoteCell.getDbNote().getRow();
                    column = lastNoteCell.getDbNote().getColumn() + 1;
                }

                DbSection dbSection = getSectionByRow(row);

                // set long press listener with options menu
                final MenuBuilder menuBuilder = new MenuBuilder(context);
                menuBuilder.setOptionalIconsVisible(true);
                MenuInflater inflater = new MenuInflater(context);
                inflater.inflate(R.menu.menu_note_options, menuBuilder);
                final MenuPopupHelper optionsMenu = new MenuPopupHelper(context, menuBuilder, parent);
                menuBuilder.findItem(R.id.action_delete_row).setVisible(column != 0);
                menuBuilder.findItem(R.id.action_insert_row).setVisible(!isLastPlus);
                menuBuilder.findItem(R.id.action_copy_row).setVisible(column != 0);
                menuBuilder.findItem(R.id.action_paste_row).setVisible(copiedNotes.size() != 0);
                menuBuilder.findItem(R.id.action_add_section).setVisible(dbSection == null && !isLastPlus);
                menuBuilder.findItem(R.id.action_edit_section).setVisible(dbSection != null);
                menuBuilder.findItem(R.id.action_delete_section).setVisible(dbSection != null);
                menuBuilder.setCallback(new MenuBuilder.Callback() {

                    @Override
                    public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_add_note:
                                onAddNoteCommand(cellLine, cell);
                                break;
                            case R.id.action_delete_row:
                                onDeleteRowCommand(cellLine);
                                break;
                            case R.id.action_insert_row:
                                onNewRowCommand(cellLine);
                                break;
                            case R.id.action_copy_row:
                                onCopyRowCommand(cellLine);
                                break;
                            case R.id.action_paste_row:
                                onPasteRowCommand(cellLine);
                                break;
                            case R.id.action_add_section:
                                onAddSectionCommand(cellLine);
                                break;
                            case R.id.action_edit_section:
                                onEditSectionCommand(cellLine);
                                break;
                            case R.id.action_delete_section:
                                onDeleteSectionCommand(cellLine);
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
                optionsMenu.show();
                return true;
            }
        });

        // set click listener
        parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int row = 0;
                int column = 0;

                List<Cell> cells = cellLine.getCells();
                if (cells.size() <= 1) {

                    // this is the last plus in the tab
                    row = cellLines.size() - 1;
                } else {

                    // get last note in the row
                    Cell lastNoteCell = cells.get(cells.size() - 2);
                    row = lastNoteCell.getDbNote().getRow();
                    column = lastNoteCell.getDbNote().getColumn() + 1;
                }
                onAddNoteCommand(cellLine, cell);
            }
        });

        songActivityListener.onNotesChanged(dbNotes);
    }

    /**
     * TODO maybe delete this
     * Searches if there is a section on the specified row and returns it.
     *
     * @param row
     * @return
     */
    private DbSection getSectionByRow(int row) {
        for (DbSection dbSection : dbSections) {
            if (dbSection.getRow() == row) {
                return dbSection;
            }
        }
        return null;
    }

    @Override
    public void onNoteAdded(DbNote dbNote, boolean newRow, CellLine cellLine) {
        if (newRow) {
            ActiveAndroid.beginTransaction();
            incrementRows(dbNote.getRow());
            incrementSections(dbNote.getRow());
            ActiveAndroid.setTransactionSuccessful();
            ActiveAndroid.endTransaction();
        }

        dbNotes.add(dbNote);
        NoteDbHandler.insertNote(dbNote);
        Collections.sort(dbNotes, notesComparator);

        if (newRow) {

            // create a new cellLine and add the note and a plus on it
            LinearLayout newLayout = new LinearLayout(context);
            newLayout.setOrientation(LinearLayout.HORIZONTAL);
            int index = cellLines.indexOf(cellLine) + 1;
            parentLayout.addView(newLayout, index);
            CellLine newCellLine = new CellLine(new ArrayList<Cell>(), newLayout);
            cellLines.add(index, newCellLine);
            addNoteCell(dbNote, newCellLine);
            addPlusCell(newCellLine);
        } else {
            addNoteCell(dbNote, cellLine);
            addLastPlus(cellLine);
        }

        songActivityListener.onNotesChanged(dbNotes);
    }

    @Override
    public void onNoteEdited(Cell cell) {
        DbNote dbNote = cell.getDbNote();
        NoteDbHandler.insertNote(dbNote);
        TextView noteTextView = (TextView) ((LinearLayout) cell.getView()).getChildAt(0);
        if (dbNote.isBlow()) {
            CustomizationUtils.styleNoteText(noteTextView, dbNote.getHole(), dbNote.getBend(), blowSign, blowStyle, blowTextColor);
        } else {
            CustomizationUtils.styleNoteText(noteTextView, dbNote.getHole(), dbNote.getBend(), drawSign, drawStyle, drawTextColor);
        }
        TextView wordTextView = (TextView) ((LinearLayout) cell.getView()).getChildAt(1);
        wordTextView.setText(dbNote.getWord());
        wordTextView.setVisibility(dbNote.getWord().isEmpty() ? View.GONE : View.VISIBLE);

        songActivityListener.onNotesChanged(dbNotes);
    }

    @Override
    public void onNoteDeleted(Cell cell, CellLine cellLine) {

        // delete the note
        DbNote dbNote = cell.getDbNote();
        int position = dbNotes.indexOf(dbNote);
        dbNotes.remove(dbNote);
        NoteDbHandler.deleteNote(dbNote);

        // remove the view
        cellLine.getLayout().removeView(cell.getView());
        cellLine.getCells().remove(cell);

        // check the remaining notes on the row
        int rowCount = 0;
        for (int i = 0; i < dbNotes.size(); i++) {
            if (dbNotes.get(i).getRow() == dbNote.getRow()) {
                rowCount++;
            }
        }

        ActiveAndroid.beginTransaction();
        if (rowCount > 0) {

            // decrement the column of the notes to the right of the deleted note
            for (int i = position; i < dbNotes.size(); i++) {
                if (dbNotes.get(i).getRow() == dbNote.getRow()) {
                    DbNote note = dbNotes.get(i);
                    note.setColumn(note.getColumn() - 1);
                    NoteDbHandler.insertNote(note);
                }
            }
        } else {

            // delete the section on this row
            DbSection dbSection = getSectionByRow(dbNote.getRow());
            if (dbSection != null) {

                // remove section
                dbSections.remove(dbSection);
                SectionDbHandler.deleteSection(dbSection);

                // remove views
                parentLayout.removeView(cellLine.getSectionCell().getTextView());
                cellLine.setSectionCell(null);
            }

            // decrement the row of the notes below the deleted note
            for (int i = position; i < dbNotes.size(); i++) {
                if (dbNotes.get(i).getRow() > dbNote.getRow()) {
                    DbNote note = dbNotes.get(i);
                    note.setRow(note.getRow() - 1);
                    NoteDbHandler.insertNote(note);
                }
            }

            // decrement the row of the sections below the deleted note
            decrementSections(dbNote.getRow());

            // remove the row layout
            parentLayout.removeView(cellLine.getLayout());
            cellLines.remove(cellLine);
        }
        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();

        songActivityListener.onNotesChanged(dbNotes);
    }

    @Override
    public void onSectionAdded(DbSection dbSection, CellLine cellLine) {
        dbSections.add(dbSection);
        SectionDbHandler.insertSection(dbSection);
        addSectionCell(dbSection, cellLine);
    }

    @Override
    public void onSectionEdit(DbSection dbSection, CellLine cellLine) {
        SectionDbHandler.insertSection(dbSection);
        TextView sectionTextView = cellLine.getSectionCell().getTextView();
        sectionTextView.setText(dbSection.getName());
    }

    private void onAddNoteCommand(CellLine cellLine, Cell cell) {
        DbNote dbNote = new DbNote();
        dbNote.setRow(cellLines.indexOf(cellLine));
        dbNote.setColumn(cellLine.getCells().indexOf(cell));
        dbNote.setSongId(dbSong.getId());
        NotePickerDialog dialog = new NotePickerDialog();
        dialog.setDbNote(dbNote);
        dialog.setListener(TablatureView.this);
        dialog.setEditMode(false);
        dialog.setNewRow(false);
        dialog.setCellLine(cellLine);
        dialog.setCell(cell);
        dialog.show(((Activity) context).getFragmentManager(), Constants.TAG_HARMONICA_NOTES_DIALOG);
    }

    private void onDeleteRowCommand(CellLine cellLine) {

        int row = cellLines.indexOf(cellLine);
        ActiveAndroid.beginTransaction();

        // delete all notes on the row
        Iterator<DbNote> iterator = dbNotes.iterator();
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

            // remove section
            dbSections.remove(dbSection);
            SectionDbHandler.deleteSection(dbSection);

            // remove views
            parentLayout.removeView(cellLine.getSectionCell().getTextView());
            cellLine.setSectionCell(null);
        }

        // decrement rows and sections
        decrementRows(row);
        decrementSections(row);
        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();

        // remove cellLine
        parentLayout.removeView(cellLine.getLayout());
        cellLines.remove(cellLine);
    }

    private void onNewRowCommand(CellLine cellLine) {
        DbNote dbNote = new DbNote();
        dbNote.setRow(cellLines.indexOf(cellLine) + 1);
        dbNote.setColumn(0);
        dbNote.setSongId(dbSong.getId());
        NotePickerDialog dialog = new NotePickerDialog();
        dialog.setDbNote(dbNote);
        dialog.setListener(TablatureView.this);
        dialog.setEditMode(false);
        dialog.setNewRow(true);
        dialog.setCellLine(cellLine);
        dialog.show(((Activity) context).getFragmentManager(), Constants.TAG_HARMONICA_NOTES_DIALOG);
    }

    private void onCopyRowCommand(CellLine cellLine) {
        copiedNotes.clear();
        for (Cell cell : cellLine.getCells()) {
            if (cell.getDbNote() != null) {
                copiedNotes.add(new DbNote(cell.getDbNote()));
            }
        }
        int copedNotesNr = copiedNotes.size();
        if (copedNotesNr == 1) {
            CustomToast.makeText(context, copedNotesNr + " " + context.getString(R.string.song_activity_toast_note_copied), Toast.LENGTH_SHORT).show();
        } else {
            CustomToast.makeText(context, copedNotesNr + " " + context.getString(R.string.song_activity_toast_notes_copied), Toast.LENGTH_SHORT).show();
        }
    }

    private void onPasteRowCommand(CellLine cellLine) {
        if (copiedNotes.isEmpty()) {
            Toast.makeText(context, R.string.song_activity_toast_no_notes_copied, Toast.LENGTH_SHORT).show();
        } else {

            // add notes into db
            ActiveAndroid.beginTransaction();
            int currentColumn = cellLine.getCells().size() - 1;
            int row = cellLines.indexOf(cellLine);
            List<DbNote> pastedNotes = new ArrayList<DbNote>();
            for (DbNote copiedNote : copiedNotes) {
                DbNote pastedNote = new DbNote(copiedNote);
                pastedNote.setRow(row);
                pastedNote.setColumn(currentColumn);
                dbNotes.add(pastedNote);
                pastedNotes.add(pastedNote);
                NoteDbHandler.insertNote(pastedNote);
                currentColumn++;
            }
            ActiveAndroid.setTransactionSuccessful();
            ActiveAndroid.endTransaction();
            Collections.sort(dbNotes, notesComparator);

            // add cells
            for (DbNote pastedNote : pastedNotes) {
                addNoteCell(pastedNote, cellLine);
            }
            addLastPlus(cellLine);
        }
    }

    private void onAddSectionCommand(CellLine cellLine) {
        SectionDialog sectionDialog = new SectionDialog();
        DbSection dbSection = new DbSection();
        dbSection.setRow(cellLines.indexOf(cellLine));
        dbSection.setSongId(dbSong.getId());
        sectionDialog.setDbSection(dbSection);
        sectionDialog.setListener(TablatureView.this);
        sectionDialog.setEditMode(false);
        sectionDialog.setCellLine(cellLine);
        sectionDialog.show(((Activity) context).getFragmentManager(), Constants.TAG_SECTION_DIALOG);
    }

    private void onEditSectionCommand(CellLine cellLine) {
        DbSection dbSection = cellLine.getSectionCell().getDbSection();
        if (dbSection != null) {
            SectionDialog sectionDialog = new SectionDialog();
            sectionDialog.setDbSection(dbSection);
            sectionDialog.setListener(TablatureView.this);
            sectionDialog.setEditMode(true);
            sectionDialog.setCellLine(cellLine);
            sectionDialog.show(((Activity) context).getFragmentManager(), Constants.TAG_SECTION_DIALOG);
        }
    }

    private void onDeleteSectionCommand(CellLine cellLine) {
        DbSection dbSection = cellLine.getSectionCell().getDbSection();
        if (dbSection != null) {

            // remove section
            dbSections.remove(dbSection);
            SectionDbHandler.deleteSection(dbSection);

            // remove views
            parentLayout.removeView(cellLine.getSectionCell().getTextView());
            cellLine.setSectionCell(null);
        }
    }

    private void addNoteCell(final DbNote dbNote, final CellLine cellLine) {

        // set note layout properties
        LinearLayout noteLayout = new LinearLayout(context);
        LinearLayout.LayoutParams noteLayoutParams = new LinearLayout.LayoutParams(MEASURE_CELL_WIDTH, MEASURE_CELL_HEIGHT);
        noteLayoutParams.setMargins(MEASURE_CELL_MARGIN, MEASURE_CELL_MARGIN, MEASURE_CELL_MARGIN, MEASURE_CELL_MARGIN);
        noteLayout.setLayoutParams(noteLayoutParams);
        noteLayout.setGravity(Gravity.CENTER_VERTICAL);
        noteLayout.setOrientation(LinearLayout.VERTICAL);
        noteLayout.setBackground(CustomizationUtils.createNoteBackground(context, dbNote.isBlow() ? blowBackgroundColor : drawBackgroundColor));

        // set note properties
        TextView noteTextView = new TextView(context);
        LinearLayout.LayoutParams noteTextLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        noteTextLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        noteTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        noteTextView.setLayoutParams(noteTextLayoutParams);
        noteTextView.setTextSize(MEASURE_NOTE_TEXT_SIZE);
        noteTextView.setMaxLines(1);
        if (dbNote.isBlow()) {
            CustomizationUtils.styleNoteText(noteTextView, dbNote.getHole(), dbNote.getBend(), blowSign, blowStyle, blowTextColor);
        } else {
            CustomizationUtils.styleNoteText(noteTextView, dbNote.getHole(), dbNote.getBend(), drawSign, drawStyle, drawTextColor);
        }
        noteLayout.addView(noteTextView);

        // set word properties
        TextView wordText = new TextView(context);
        LinearLayout.LayoutParams wordTextLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        wordTextLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        wordText.setGravity(Gravity.CENTER_HORIZONTAL);
        wordText.setLayoutParams(wordTextLayoutParams);
        wordText.setTextSize(MEASURE_NOTE_WORD_SIZE);
        wordText.setMaxLines(1);
        wordText.setText(dbNote.getWord());
        wordText.setTextColor(dbNote.isBlow() ? blowTextColor : drawTextColor);
        wordText.setVisibility(dbNote.getWord().isEmpty() ? View.GONE : View.VISIBLE);
        noteLayout.addView(wordText);

        // add note to parent
        cellLine.getLayout().addView(noteLayout, dbNote.getColumn());
        final Cell cell = new Cell(dbNote, noteLayout);
        cellLine.addCell(cell, dbNote.getColumn());

        // set click listener
        noteLayout.setClickable(true);
        noteLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                NotePickerDialog dialog = new NotePickerDialog();
                dialog.setDbNote(dbNote);
                dialog.setListener(TablatureView.this);
                dialog.setEditMode(true);
                dialog.setCell(cell);
                dialog.setCellLine(cellLine);
                dialog.show(((Activity) context).getFragmentManager(), Constants.TAG_HARMONICA_NOTES_DIALOG);
            }
        });
    }

    private void addSectionCell(DbSection dbSection, CellLine cellLine) {

        // set section view properties
        TextView sectionText = new TextView(context);
        sectionText.setPadding(MEASURE_SECTION_PADDING_LEFT, MEASURE_SECTION_PADDING_TOP, 0, MEASURE_SECTION_PADDING_BOTTOM);
        sectionText.setTextSize(MEASURE_SECTION_SIZE);
        sectionText.setText(dbSection.getName());
        CustomizationUtils.styleSectionText(sectionText, sectionStyle, sectionTextColor);
        parentLayout.addView(sectionText, parentLayout.indexOfChild(cellLine.getLayout()));
        SectionCell sectionCell = new SectionCell(dbSection, sectionText);
        cellLine.setSectionCell(sectionCell);
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

    private void addLastPlus(CellLine cellLine) {
        if (cellLines.indexOf(cellLine) == (cellLines.size() - 1)) {

            // add a plus note on next row
            LinearLayout lastRow = new LinearLayout(context);
            lastRow.setOrientation(LinearLayout.HORIZONTAL);
            parentLayout.addView(lastRow);
            CellLine lastCellLine = new CellLine(new ArrayList<Cell>(), lastRow);
            cellLines.add(lastCellLine);
            addPlusCell(lastCellLine);
        }
    }

    /**
     * Increments rows by 1, starting with the specified row and going up.
     *
     * @param row The row from where to start the incrementation.
     */
    private void incrementRows(int row) {
        for (DbNote note : dbNotes) {
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
        for (DbNote note : dbNotes) {
            int noteRow = note.getRow();
            if (noteRow >= row) {
                note.setRow(noteRow - 1);
                NoteDbHandler.insertNote(note);
            }
        }
    }

    private void incrementSections(int row) {
        for (DbSection dbSection : dbSections) {
            if (dbSection.getRow() >= row) {
                dbSection.setRow(dbSection.getRow() + 1);
                SectionDbHandler.insertSection(dbSection);
            }
        }
    }

    private void decrementSections(int row) {
        if (dbNotes.isEmpty()) {

            // delete all sections
            for (DbSection dbSection : dbSections) {
                SectionDbHandler.deleteSection(dbSection);
            }
            dbSections.clear();

        } else {

            // decrement sections after the specified row
            for (DbSection dbSection : dbSections) {
                if (dbSection.getRow() > row) {
                    dbSection.setRow(dbSection.getRow() - 1);
                    SectionDbHandler.insertSection(dbSection);
                }
            }

            // search for possible multiple sections on the same row
            List<DbSection> duplicateSections = new ArrayList<DbSection>();
            for (DbSection dbSection : dbSections) {
                if (dbSection.getRow() == row) {
                    duplicateSections.add(dbSection);
                }
            }

            // leave only 1 section on the row and delete the others
            if (duplicateSections.size() > 1) {
                for (int i = 1; i < duplicateSections.size(); i++) {
                    dbSections.remove(duplicateSections.get(i));
                    SectionDbHandler.deleteSection(duplicateSections.get(i));
                }
            }
        }
    }

    public void setMediaPadding(boolean media) {
        setPadding(0, MEASURE_TABLATURE_TOP_PADDING, 0, media ? MEASURE_TABLATURE_BOTTOM_PADDING_WITH_MEDIA : MEASURE_TABLATURE_BOTTOM_PADDING);
    }

}
