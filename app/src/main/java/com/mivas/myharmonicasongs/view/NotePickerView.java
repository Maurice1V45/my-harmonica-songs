package com.mivas.myharmonicasongs.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.SongActivity;
import com.mivas.myharmonicasongs.adapter.NotePickerAdapter;
import com.mivas.myharmonicasongs.adapter.NotePickerAdapterBends;
import com.mivas.myharmonicasongs.database.model.DbNote;
import com.mivas.myharmonicasongs.database.model.DbSection;
import com.mivas.myharmonicasongs.dialog.InsertNoteDialog;
import com.mivas.myharmonicasongs.dialog.NotesShiftDialog;
import com.mivas.myharmonicasongs.listener.InsertNoteDialogListener;
import com.mivas.myharmonicasongs.listener.NotePickerAdapterListener;
import com.mivas.myharmonicasongs.listener.NotePickerViewListener;
import com.mivas.myharmonicasongs.model.Cell;
import com.mivas.myharmonicasongs.model.CellLine;
import com.mivas.myharmonicasongs.util.Constants;
import com.mivas.myharmonicasongs.util.CustomizationUtils;
import com.mivas.myharmonicasongs.util.KeyboardUtils;

public class NotePickerView extends RelativeLayout implements NotePickerAdapterListener, InsertNoteDialogListener {

    private Context context;
    private DbNote dbNote;
    private RecyclerView notesList;
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
    private View clearButton;
    private View sectionTextLayout;
    private EditText sectionTextField;
    private View sectionClearButton;
    private Cell cell;
    private CellLine cellLine;
    private boolean editMode;
    private boolean newRow;
    private boolean notePickerDisplayed;
    private boolean bendsAdapter;
    private boolean showBends;

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
        copyButton = findViewById(R.id.button_copy);
        pasteButton = findViewById(R.id.button_paste);
        insertButton = findViewById(R.id.button_insert);
        sectionButton = findViewById(R.id.button_section);
        deleteButton = findViewById(R.id.button_delete);
        textLayout = findViewById(R.id.layout_text);
        textField = findViewById(R.id.field_text);
        clearButton = findViewById(R.id.button_clear);
        sectionTextLayout = findViewById(R.id.layout_section_text);
        sectionTextField = findViewById(R.id.field_section_text);
        sectionClearButton = findViewById(R.id.button_section_clear);
        notesList = findViewById(R.id.list_harmonica_notes);
        notesList.setLayoutManager(new LinearLayoutManager(context, LinearLayout.HORIZONTAL, false));
        showBends = CustomizationUtils.getShowBends();
        if (showBends) {
            bendsAdapter = true;
            notesList.setAdapter(new NotePickerAdapterBends(context, NotePickerView.this, dbNote));
        } else {
            bendsAdapter = false;
            notesList.setAdapter(new NotePickerAdapter(context, NotePickerView.this, dbNote));
        }
    }

    public void initialize() {
        textButton.setEnabled(editMode);
        copyButton.setEnabled(cellLine.getCells().size() > 1);
        sectionButton.setEnabled(cellLine.getCells().size() > 1);
        deleteButton.setEnabled(cellLine.getCells().size() > 1);
        showBends = showBends || dbNote.getBend() != 0;
        if (showBends) {
            if (bendsAdapter) {
                ((NotePickerAdapterBends) notesList.getAdapter()).setSelectedNote(dbNote);
                notesList.getAdapter().notifyDataSetChanged();
            } else {
                bendsAdapter = true;
                notesList.setAdapter(new NotePickerAdapterBends(context, NotePickerView.this, dbNote));
            }
        } else {
            if (!bendsAdapter) {
                ((NotePickerAdapter) notesList.getAdapter()).setSelectedNote(dbNote);
                notesList.getAdapter().notifyDataSetChanged();
            } else {
                bendsAdapter = false;
                notesList.setAdapter(new NotePickerAdapter(context, NotePickerView.this, dbNote));
            }
        }
        if (editMode) {
            textField.setText(dbNote.getWord() != null && !dbNote.getWord().isEmpty() ? dbNote.getWord() : "");
            textField.setSelection(textField.getText().length());
        } else {
            KeyboardUtils.closeKeyboard((Activity) context);
            textLayout.setVisibility(View.GONE);
        }
        if (cellLine.getSectionCell() != null && cellLine.getSectionCell().getDbSection() != null) {
            sectionTextField.setText(cellLine.getSectionCell().getDbSection().getName());
            textField.setSelection(textField.getText().length());
        } else {
            sectionTextField.setText("");
        }
    }

    private void initListeners() {
        textButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                textLayout.setVisibility(View.VISIBLE);
                textField.setText(dbNote.getWord() == null ? "" : dbNote.getWord());
                KeyboardUtils.focusEditText(context, textField);
            }
        });
        bendsButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showBends = !showBends;
                bendsAdapter = showBends;
                if (showBends) {
                    notesList.setAdapter(new NotePickerAdapterBends(context, NotePickerView.this, dbNote));
                } else {
                    notesList.setAdapter(new NotePickerAdapter(context, NotePickerView.this, dbNote));
                }
                listener.onBendsSelected(showBends, cellLine);
            }
        });
        copyButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onNotesCopied(cellLine);
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
                dialog.setListener(NotePickerView.this);
                dialog.show(((Activity) context).getFragmentManager(), Constants.TAG_INSERT_NOTE_DIALOG);
            }
        });
        sectionButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                sectionTextLayout.setVisibility(View.VISIBLE);
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
                    listener.onNoteEdited(cellLine, cell);
                }
                return true;
            }
        });
        clearButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dbNote.setWord(null);
                listener.onNoteEdited(cellLine, cell);
            }
        });
        sectionTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (cellLine.getSectionCell() == null || cellLine.getSectionCell().getDbSection() == null) {
                        DbSection dbSection = new DbSection();
                        dbSection.setName(sectionTextField.getText().toString());
                        listener.onSectionAdded(dbSection, cellLine);
                    } else {
                        DbSection dbSection = cellLine.getSectionCell().getDbSection();
                        dbSection.setName(sectionTextField.getText().toString());
                        listener.onSectionEdited(dbSection, cellLine);
                    }
                    KeyboardUtils.closeKeyboard((Activity) context);
                    sectionTextLayout.setVisibility(View.GONE);
                }
                return false;
            }
        });
        sectionClearButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onSectionDeleted(cellLine);
                KeyboardUtils.closeKeyboard((Activity) context);
                sectionTextLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onNoteSelected(int note, boolean blow, float bend) {
        dbNote.setHole(note);
        dbNote.setBlow(blow);
        dbNote.setBend(bend);
        if (editMode) {
            listener.onNoteEdited(cellLine, cell);
        } else {
            listener.onNoteAdded(dbNote, newRow, cellLine);
        }
    }

    public void setDbNote(DbNote dbNote) {
        this.dbNote = dbNote;
    }

    public void setListener(NotePickerViewListener listener) {
        this.listener = listener;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public void setNewRow(boolean newRow) {
        this.newRow = newRow;
    }

    public void setCellLine(CellLine cellLine) {
        this.cellLine = cellLine;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public boolean isNotePickerDisplayed() {
        return notePickerDisplayed;
    }

    public View getTextLayout() {
        return textLayout;
    }

    public View getSectionTextLayout() {
        return sectionTextLayout;
    }

    public boolean isShowBends() {
        return showBends;
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
        notePickerDisplayed = !notePickerDisplayed;
    }

    @Override
    public void onLeftSelected() {

    }

    @Override
    public void onRightSelected() {

    }

    @Override
    public void onTopSelected() {

    }

    @Override
    public void onBottomSelected() {

    }
}
