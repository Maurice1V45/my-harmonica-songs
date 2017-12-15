package com.mivas.myharmonicasongs.view;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.adapter.Chromatic12NotePickerAdapter;
import com.mivas.myharmonicasongs.adapter.Chromatic12NotePickerAdapterBends;
import com.mivas.myharmonicasongs.adapter.Chromatic16NotePickerAdapter;
import com.mivas.myharmonicasongs.adapter.Chromatic16NotePickerAdapterBends;
import com.mivas.myharmonicasongs.adapter.Diatonic10NotePickerAdapter;
import com.mivas.myharmonicasongs.adapter.Diatonic10NotePickerAdapterBends;
import com.mivas.myharmonicasongs.database.model.DbNote;
import com.mivas.myharmonicasongs.database.model.DbSection;
import com.mivas.myharmonicasongs.database.model.DbSong;
import com.mivas.myharmonicasongs.dialog.InsertNoteDialog;
import com.mivas.myharmonicasongs.listener.InsertNoteDialogListener;
import com.mivas.myharmonicasongs.listener.NotePickerAdapterListener;
import com.mivas.myharmonicasongs.listener.NotePickerViewListener;
import com.mivas.myharmonicasongs.model.Cell;
import com.mivas.myharmonicasongs.model.CellLine;
import com.mivas.myharmonicasongs.util.Constants;
import com.mivas.myharmonicasongs.util.CustomizationUtils;
import com.mivas.myharmonicasongs.util.FrequencyUtils;
import com.mivas.myharmonicasongs.util.KeyboardUtils;

public class NotePickerView extends RelativeLayout implements NotePickerAdapterListener, InsertNoteDialogListener {

    private Context context;
    private DbNote dbNote;
    private DbSong dbSong;
    private RecyclerView notesList;
    private View notesLayout;
    private NotePickerViewListener listener;
    private View textButton;
    private View bendsButton;
    private View copyButton;
    private View pasteButton;
    private View insertButton;
    private View sectionButton;
    private View deleteButton;
    private View textLayout;
    private EditText textField;
    private View cancelButton;
    private View doneButton;
    private View sectionTextLayout;
    private ImageView bendsButtonImage;
    private TextView bendsButtonText;
    private EditText sectionTextField;
    private View sectionCancelButton;
    private View sectionDoneButton;
    private Cell cell;
    private CellLine cellLine;
    private boolean editMode;
    private boolean notePickerDisplayed;
    private boolean bendsAdapter;
    private boolean showBends;
    private boolean copiedNotes;
    private boolean playNoteSound;

    public NotePickerView(Context context) {
        super(context);
        this.context = context;
        initViews();
        initListeners();
    }

    public NotePickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initViews();
        initListeners();
    }

    public NotePickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initViews();
        initListeners();
    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.layout_note_picker, this, true);
        textButton = findViewById(R.id.button_text);
        bendsButton = findViewById(R.id.button_bends);
        bendsButtonImage = findViewById(R.id.image_bends_button);
        bendsButtonText = findViewById(R.id.text_bends_button);
        copyButton = findViewById(R.id.button_copy);
        pasteButton = findViewById(R.id.button_paste);
        insertButton = findViewById(R.id.button_insert);
        sectionButton = findViewById(R.id.button_section);
        deleteButton = findViewById(R.id.button_delete);
        textLayout = findViewById(R.id.layout_text);
        textField = findViewById(R.id.field_text);
        cancelButton = findViewById(R.id.button_cancel);
        doneButton = findViewById(R.id.button_done);
        sectionTextLayout = findViewById(R.id.layout_section_text);
        sectionTextField = findViewById(R.id.field_section_text);
        sectionCancelButton = findViewById(R.id.button_section_cancel);
        sectionDoneButton = findViewById(R.id.button_section_done);
        notesLayout = findViewById(R.id.layout_notes);
        notesList = findViewById(R.id.list_harmonica_notes);
        notesList.setLayoutManager(new LinearLayoutManager(context, LinearLayout.HORIZONTAL, false));
    }

    public void initialize() {
        setEnabled(true);
        if (notesList.getAdapter() == null) {
            setNewAdapter(showBends);
            showBends = dbSong.getHarpType() == 0 ? CustomizationUtils.getShowBends() : CustomizationUtils.getShowButton();
            bendsAdapter = showBends;
            playNoteSound = CustomizationUtils.getPlayNoteSound();
            bendsButtonImage.setImageResource(dbSong.getHarpType() == 0 ? R.drawable.icon_bends : R.drawable.icon_button);
            bendsButtonText.setText(dbSong.getHarpType() == 0 ? R.string.note_picker_view_button_bends : R.string.note_picker_view_button_slide);
        }
        boolean lastPlus = cellLine.getCells().size() <= 1;
        textButton.setVisibility(editMode ? View.VISIBLE : View.INVISIBLE);
        copyButton.setVisibility(lastPlus ? View.INVISIBLE : View.VISIBLE);
        pasteButton.setVisibility(copiedNotes ? View.VISIBLE : View.INVISIBLE);
        sectionButton.setVisibility(lastPlus ? View.INVISIBLE : View.VISIBLE);
        deleteButton.setVisibility(lastPlus ? View.INVISIBLE : View.VISIBLE);
        showBends = showBends || dbNote.getBend() != 0;
        if (showBends) {
            if (bendsAdapter) {
                switch (dbSong.getHarpType()) {
                    case 0:
                        ((Diatonic10NotePickerAdapterBends) notesList.getAdapter()).setSelectedNote(dbNote);
                        break;
                    case 1:
                        ((Chromatic12NotePickerAdapterBends) notesList.getAdapter()).setSelectedNote(dbNote);
                        break;
                    case 2:
                        ((Chromatic16NotePickerAdapterBends) notesList.getAdapter()).setSelectedNote(dbNote);
                        break;
                }
                notesList.getAdapter().notifyDataSetChanged();
            } else {
                bendsAdapter = true;
                setNewAdapter(true);
            }
        } else {
            if (!bendsAdapter) {
                switch (dbSong.getHarpType()) {
                    case 0:
                        ((Diatonic10NotePickerAdapter) notesList.getAdapter()).setSelectedNote(dbNote);
                        break;
                    case 1:
                        ((Chromatic12NotePickerAdapter) notesList.getAdapter()).setSelectedNote(dbNote);
                        break;
                    case 2:
                        ((Chromatic16NotePickerAdapter) notesList.getAdapter()).setSelectedNote(dbNote);
                        break;
                }
                notesList.getAdapter().notifyDataSetChanged();
            } else {
                bendsAdapter = false;
                setNewAdapter(false);
            }
        }
        if (editMode) {
            if (cellLine.getCells().indexOf(cell) == 0) {
                textField.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            } else {
                textField.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            }
            textField.setText(dbNote.getWord() != null && !dbNote.getWord().isEmpty() ? dbNote.getWord() : "");
            textField.setSelection(textField.getText().length());
        } else {
            closeTextMode(true);
        }
        if (cellLine.getSectionCell() != null && cellLine.getSectionCell().getDbSection() != null) {
            sectionTextField.setText(cellLine.getSectionCell().getDbSection().getName());
            textField.setSelection(textField.getText().length());
        } else {
            sectionTextField.setText("");
        }
        if (lastPlus) {
            closeSectionTextMode(true);
        }
    }

    private void initListeners() {
        textButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                textLayout.setVisibility(View.VISIBLE);
                notesLayout.setVisibility(View.GONE);
                textField.setText(dbNote.getWord() == null ? "" : dbNote.getWord());
                KeyboardUtils.focusEditText(context, textField);
            }
        });
        bendsButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showBends = !showBends;
                bendsAdapter = showBends;
                setNewAdapter(showBends);
                listener.onBendsSelected(showBends, cellLine, cell);
            }
        });
        copyButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onNotesCopied(cellLine, cell);
            }
        });
        pasteButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onNotesPasted(cellLine, cell);
            }
        });
        insertButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                InsertNoteDialog dialog = new InsertNoteDialog();
                dialog.setCellLine(cellLine);
                dialog.setCell(cell);
                dialog.setListener(NotePickerView.this);
                dialog.show(((Activity) context).getFragmentManager(), Constants.TAG_INSERT_NOTE_DIALOG);
            }
        });
        sectionButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                sectionTextLayout.setVisibility(View.VISIBLE);
                notesLayout.setVisibility(View.GONE);
                if (cellLine.getSectionCell() == null || cellLine.getSectionCell().getDbSection() == null) {
                    sectionTextField.setText("");
                } else {
                    sectionTextField.setText(cellLine.getSectionCell().getDbSection().getName());
                }
                KeyboardUtils.focusEditText(context, sectionTextField);
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (editMode) {
                    listener.onNoteDeleted(cellLine, cell);
                } else {
                    listener.onRowDeleted(cellLine);
                }
            }
        });
        textField.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    dbNote.setWord(textField.getText().toString());
                    if (cellLine.getCells().indexOf(cell) < cellLine.getCells().size() - 2) {
                        listener.onNoteEdited(cellLine, cell, true, false);
                    } else {
                        listener.onNoteEdited(cellLine, cell, false, true);
                    }
                }
                return true;
            }
        });
        cancelButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dbNote.setWord(null);
                if (cellLine.getCells().indexOf(cell) < cellLine.getCells().size() - 2) {
                    listener.onNoteEdited(cellLine, cell, true, false);
                } else {
                    listener.onNoteEdited(cellLine, cell, false, true);
                }
            }
        });
        doneButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dbNote.setWord(textField.getText().toString());
                closeTextMode(true);
                listener.onNoteEdited(cellLine, cell, false, false);
            }
        });
        sectionTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addEditSection();
                    closeSectionTextMode(true);
                }
                return false;
            }
        });
        sectionCancelButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onSectionDeleted(cellLine);
                closeSectionTextMode(true);
            }
        });
        sectionDoneButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                addEditSection();
                closeSectionTextMode(true);
            }
        });
    }

    private void addEditSection() {
        if (cellLine.getSectionCell() == null || cellLine.getSectionCell().getDbSection() == null) {
            DbSection dbSection = new DbSection();
            dbSection.setName(sectionTextField.getText().toString());
            dbSection.setSongId(dbNote.getSongId());
            listener.onSectionAdded(dbSection, cellLine);
        } else {
            DbSection dbSection = cellLine.getSectionCell().getDbSection();
            dbSection.setName(sectionTextField.getText().toString());
            dbSection.setSongId(dbNote.getSongId());
            listener.onSectionEdited(dbSection, cellLine);
        }
    }

    @Override
    public void onNoteSelected(int note, boolean blow, float bend, boolean button) {
        if (isEnabled()) {

            if (playNoteSound) {
                FrequencyUtils.playSound(FrequencyUtils.getFrequency(note, blow, bend, dbSong));
            }
            dbNote.setHole(note);
            dbNote.setBlow(blow);
            dbNote.setBend(bend);
            if (editMode) {
                listener.onNoteEdited(cellLine, cell, true, false);
            } else {
                listener.onNoteAdded(cellLine, cell, dbNote);
            }
        }
    }

    public void setDbNote(DbNote dbNote) {
        this.dbNote = dbNote;
    }

    public void setDbSong(DbSong dbSong) {
        this.dbSong = dbSong;
    }

    public void setListener(NotePickerViewListener listener) {
        this.listener = listener;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public void setCellLine(CellLine cellLine) {
        this.cellLine = cellLine;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public void setShowBends(boolean showBends) {
        this.showBends = showBends;
    }

    public boolean isNotePickerDisplayed() {
        return notePickerDisplayed;
    }

    public void setNotePickerDisplayed(boolean notePickerDisplayed) {
        this.notePickerDisplayed = notePickerDisplayed;
    }

    public View getTextLayout() {
        return textLayout;
    }

    public View getNotesLayout() {
        return notesLayout;
    }

    public void setNotesList(RecyclerView notesList) {
        this.notesList = notesList;
    }

    public View getSectionTextLayout() {
        return sectionTextLayout;
    }

    public boolean isShowBends() {
        return showBends;
    }

    public void setCopiedNotes(boolean copiedNotes) {
        this.copiedNotes = copiedNotes;
    }

    public void setPlayNoteSound(boolean playNoteSound) {
        this.playNoteSound = playNoteSound;
    }

    public void animate(final boolean expand) {
        Animation animation = AnimationUtils.loadAnimation(context, expand ? R.anim.anim_bottom_up_notepicker : R.anim.anim_bottom_down_notepicker);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!expand) {
                    setVisibility(GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        setVisibility(VISIBLE);
        startAnimation(animation);
        notePickerDisplayed = expand;
    }

    @Override
    public void onLeftSelected() {
        DbNote newNote = new DbNote();
        newNote.setHole(4);
        newNote.setBlow(true);
        newNote.setRow(dbNote.getRow());
        newNote.setColumn(dbNote.getColumn());
        newNote.setSongId(dbNote.getSongId());
        listener.onNoteInserted(cellLine, newNote);
    }

    @Override
    public void onRightSelected() {
        DbNote newNote = new DbNote();
        newNote.setHole(4);
        newNote.setBlow(true);
        newNote.setRow(dbNote.getRow());
        newNote.setColumn(dbNote.getColumn() + 1);
        newNote.setSongId(dbNote.getSongId());
        listener.onNoteInserted(cellLine, newNote);
    }

    @Override
    public void onTopSelected() {
        DbNote newNote = new DbNote();
        newNote.setHole(4);
        newNote.setBlow(true);
        newNote.setColumn(0);
        newNote.setSongId(dbNote.getSongId());
        listener.onRowInserted(cellLine, newNote, true);
    }

    @Override
    public void onBottomSelected() {
        DbNote newNote = new DbNote();
        newNote.setHole(4);
        newNote.setBlow(true);
        newNote.setColumn(0);
        newNote.setSongId(dbNote.getSongId());
        listener.onRowInserted(cellLine, newNote, false);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        textButton.setEnabled(enabled);
        bendsButton.setEnabled(enabled);
        copyButton.setEnabled(enabled);
        pasteButton.setEnabled(enabled);
        insertButton.setEnabled(enabled);
        sectionButton.setEnabled(enabled);
        deleteButton.setEnabled(enabled);
    }

    public void closeTextMode(boolean animateNotePicker) {
        if (textLayout.getVisibility() == View.VISIBLE) {
            KeyboardUtils.closeKeyboard((Activity) context);
            textLayout.setVisibility(View.GONE);
            notesLayout.setVisibility(View.VISIBLE);
            if (animateNotePicker) {
                animate(true);
            }
        }

    }

    public void closeSectionTextMode(boolean animateNotePicker) {
        if (sectionTextLayout.getVisibility() == View.VISIBLE) {
            KeyboardUtils.closeKeyboard((Activity) context);
            sectionTextLayout.setVisibility(View.GONE);
            notesLayout.setVisibility(View.VISIBLE);
            if (animateNotePicker) {
                animate(true);
            }
        }
    }

    private void setNewAdapter(boolean bends) {
        switch (dbSong.getHarpType()) {
            case 0:
                if (bends) {
                    notesList.setAdapter(new Diatonic10NotePickerAdapterBends(context, NotePickerView.this, dbNote));
                } else {
                    notesList.setAdapter(new Diatonic10NotePickerAdapter(context, NotePickerView.this, dbNote));
                }
                break;
            case 1:
                if (bends) {
                    notesList.setAdapter(new Chromatic12NotePickerAdapterBends(context, NotePickerView.this, dbNote));
                } else {
                    notesList.setAdapter(new Chromatic12NotePickerAdapter(context, NotePickerView.this, dbNote));
                }
                break;
            case 2:
                if (bends) {
                    notesList.setAdapter(new Chromatic16NotePickerAdapterBends(context, NotePickerView.this, dbNote));
                } else {
                    notesList.setAdapter(new Chromatic16NotePickerAdapter(context, NotePickerView.this, dbNote));
                }
                break;
        }
    }

    public RecyclerView getNotesList() {
        return notesList;
    }
}
