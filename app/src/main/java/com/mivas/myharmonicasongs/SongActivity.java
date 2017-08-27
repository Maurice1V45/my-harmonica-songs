package com.mivas.myharmonicasongs;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.mivas.myharmonicasongs.animation.SlideAnimation;
import com.mivas.myharmonicasongs.database.handler.NoteDbHandler;
import com.mivas.myharmonicasongs.database.handler.SectionDbHandler;
import com.mivas.myharmonicasongs.database.handler.SongDbHandler;
import com.mivas.myharmonicasongs.database.model.DbNote;
import com.mivas.myharmonicasongs.database.model.DbSection;
import com.mivas.myharmonicasongs.database.model.DbSong;
import com.mivas.myharmonicasongs.dialog.NotePickerDialog;
import com.mivas.myharmonicasongs.dialog.NotesShiftDialog;
import com.mivas.myharmonicasongs.dialog.SectionDialog;
import com.mivas.myharmonicasongs.exception.MediaPlayerException;
import com.mivas.myharmonicasongs.listener.NotesShiftListener;
import com.mivas.myharmonicasongs.listener.SongActivityListener;
import com.mivas.myharmonicasongs.model.RowViewHolder;
import com.mivas.myharmonicasongs.model.NoteViewHolder;
import com.mivas.myharmonicasongs.util.Constants;
import com.mivas.myharmonicasongs.util.CustomToast;
import com.mivas.myharmonicasongs.util.CustomizationUtils;
import com.mivas.myharmonicasongs.util.DimensionUtils;
import com.mivas.myharmonicasongs.util.ExportHelper;
import com.mivas.myharmonicasongs.util.NotesShiftUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Activity that displays notes.
 */
public class SongActivity extends AppCompatActivity implements SongActivityListener, NotesShiftListener {

    private LinearLayout notesLayout;
    private DbSong dbSong;
    private List<DbNote> notes = new ArrayList<DbNote>();
    private List<DbSection> sections = new ArrayList<DbSection>();
    private Comparator notesComparator;
    private TextView noNotesText;
    private List<RowViewHolder> rowViewHolders = new ArrayList<RowViewHolder>();
    private List<DbNote> copiedNotes = new ArrayList<DbNote>();
    private View backgroundView;
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
    private int backgroundColor;
    private MediaPlayer mediaPlayer;
    private ImageView playButton;
    private SeekBar songProgressBar;
    private Handler mediaHandler;
    private TextView mediaCurrentTimeText;
    private TextView mediaTotalTimeText;
    private boolean mediaViewDisplayed = false;
    private View mediaView;
    private BroadcastReceiver customizationReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            initCustomizations();
            drawNotes();
            backgroundView.setBackgroundColor(backgroundColor);
        }
    };
    private Runnable updateSongViewsRunnable;

    private static final int REQUEST_CODE_ADD_AUDIO_FILE = 1;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 2;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initMeasures();
        initViews();
        initListeners();
        initCustomizations();
        initComparator();

        dbSong = SongDbHandler.getSongById(getIntent().getLongExtra(Constants.EXTRA_SONG_ID, 0));
        notes = NoteDbHandler.getNotesBySongId(dbSong.getId());
        sections = SectionDbHandler.getSectionsBySongId(dbSong.getId());
        getSupportActionBar().setTitle(dbSong.getTitle());
        backgroundView.setBackgroundColor(backgroundColor);
        drawNotes();
        try {
            initMediaPlayer();
        } catch (MediaPlayerException e) {
            CustomToast.makeText(SongActivity.this, R.string.song_activity_toast_media_player_init_error, Toast.LENGTH_SHORT).show();
        }
        registerReceiver(customizationReceiver, new IntentFilter(Constants.INTENT_CUSTOMIZATIONS_UPDATED));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_song_activity, menu);
        MenuItem toggleMedia = menu.findItem(R.id.action_toggle_media);
        MenuItem addAudioFile = menu.findItem(R.id.action_add_audio_file);
        MenuItem removeAudioFile = menu.findItem(R.id.action_remove_audio_file);
        if (dbSong.getAudioFile() == null) {
            toggleMedia.setVisible(false);
            addAudioFile.setVisible(true);
            removeAudioFile.setVisible(false);
        } else {
            toggleMedia.setVisible(true);
            addAudioFile.setVisible(false);
            removeAudioFile.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_add_audio_file:
                if (ContextCompat.checkSelfPermission(SongActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SongActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
                } else {
                    launchAddAudioFileActivity();
                }
                return true;
            case R.id.action_remove_audio_file:
                ExportHelper.getInstance().removeAudioFile(SongActivity.this, dbSong);
                dbSong.setAudioFile(null);
                SongDbHandler.insertSong(dbSong);
                CustomToast.makeText(SongActivity.this, R.string.song_activity_toast_audio_file_removed, Toast.LENGTH_SHORT).show();
                if (mediaView.getVisibility() == View.VISIBLE) {
                    animateMediaView(false);
                }
                invalidateOptionsMenu();
                releaseMediaPlayer();
                sendSongsUpdatedBroadcast();
                return true;
            case R.id.action_customize:
                Intent intent = new Intent(SongActivity.this, CustomizeActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_toggle_media:
                if (dbSong.getAudioFile() == null) {
                    CustomToast.makeText(SongActivity.this, R.string.song_activity_toast_no_audio_file, Toast.LENGTH_SHORT).show();
                } else {
                    animateMediaView(!mediaViewDisplayed);
                }
                return true;
            case R.id.action_share:
                List<DbSong> dbSongs = new ArrayList<DbSong>();
                dbSongs.add(dbSong);
                String fileName = (dbSong.getTitle() + ".mhs");
                ExportHelper.getInstance().launchExportIntent(SongActivity.this, dbSongs, fileName, getString(R.string.song_activity_text_share_to));
                return true;
            case R.id.action_shift_notes:
                NotesShiftDialog dialog = new NotesShiftDialog();
                dialog.setDbNotes(notes);
                dialog.setListener(SongActivity.this);
                dialog.show(getFragmentManager(), Constants.TAG_NOTES_SHIFT_DIALOG);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initMeasures() {
        MEASURE_CELL_WIDTH = DimensionUtils.dpToPx(SongActivity.this, 48);
        MEASURE_CELL_HEIGHT = DimensionUtils.dpToPx(SongActivity.this, 66);
        MEASURE_CELL_MARGIN = DimensionUtils.dpToPx(SongActivity.this, 3);
        MEASURE_NOTE_TEXT_SIZE = 24;
        MEASURE_NOTE_WORD_SIZE = 10;
        MEASURE_PLUS_SIZE = DimensionUtils.dpToPx(SongActivity.this, 42);
        MEASURE_SECTION_PADDING_TOP = DimensionUtils.dpToPx(SongActivity.this, 24);
        MEASURE_SECTION_PADDING_LEFT = DimensionUtils.dpToPx(SongActivity.this, 12);
        MEASURE_SECTION_PADDING_BOTTOM = DimensionUtils.dpToPx(SongActivity.this, 4);
        MEASURE_SECTION_SIZE = 24;
    }

    private void animateMediaView(boolean expand) {
        SlideAnimation slideAnimation = new SlideAnimation(mediaView, 100, expand ? SlideAnimation.EXPAND : SlideAnimation.COLLAPSE);
        slideAnimation.setHeight(DimensionUtils.dpToPx(SongActivity.this, 72));
        mediaView.startAnimation(slideAnimation);
        mediaViewDisplayed = !mediaViewDisplayed;
    }

    /**
     * Views initializer.
     */
    private void initViews() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        notesLayout = (LinearLayout) findViewById(R.id.list_notes);
        noNotesText = (TextView) findViewById(R.id.text_no_notes);
        backgroundView = findViewById(R.id.view_background);
        backgroundView.setBackgroundColor(backgroundColor);
        playButton = (ImageView) findViewById(R.id.button_play);
        songProgressBar = (SeekBar) findViewById(R.id.seek_bar_progress);
        mediaCurrentTimeText = (TextView) findViewById(R.id.text_current_time);
        mediaTotalTimeText = (TextView) findViewById(R.id.text_total_time);
        mediaView = findViewById(R.id.view_media);
    }

    /**
     * Listeners initializer.
     */
    private void initListeners() {
        playButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    playButton.setImageResource(R.drawable.selector_play_button);
                } else {
                    mediaPlayer.start();
                    playButton.setImageResource(R.drawable.selector_pause_button);
                }
            }
        });
    }

    private void initMediaPlayer() throws MediaPlayerException {
        if (dbSong.getAudioFile() != null) {

            // prepare song
            Uri songUri = ExportHelper.getInstance().getAudioFileUri(SongActivity.this, dbSong);
            mediaPlayer = MediaPlayer.create(SongActivity.this, songUri);
            if (mediaPlayer == null) {
                throw new MediaPlayerException();
            }

            // init progress bar
            final int finalTime = mediaPlayer.getDuration();
            songProgressBar.setMax(finalTime);
            songProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        mediaPlayer.seekTo(progress);
                        updateTimeText(mediaCurrentTimeText, progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            // init time texts and play button
            updateTimeText(mediaCurrentTimeText, mediaPlayer.getCurrentPosition());
            updateTimeText(mediaTotalTimeText, finalTime);
            playButton.setImageResource(R.drawable.selector_play_button);

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    playButton.setImageResource(R.drawable.selector_play_button);
                }
            });

            // init update handler
            mediaHandler = new Handler();
            updateSongViewsRunnable = new Runnable() {
                public void run() {
                    int currentTime = mediaPlayer.getCurrentPosition();
                    songProgressBar.setProgress(currentTime);
                    updateTimeText(mediaCurrentTimeText, currentTime > finalTime ? finalTime : currentTime);
                    mediaHandler.postDelayed(this, 100);
                }
            };
            mediaHandler.postDelayed(updateSongViewsRunnable, 100);

        }
    }

    private void updateTimeText(TextView textView, int time) {
        textView.setText(String.format("%d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(time),
                TimeUnit.MILLISECONDS.toSeconds(time) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))));
    }

    /**
     * Redraws the whole view of notes.
     */
    private void drawNotes() {

        // clear all views first
        rowViewHolders.clear();
        notesLayout.removeAllViews();
        if (notes.size() == 0) {

            // display only 1 plus note
            LinearLayout rowLayout = new LinearLayout(SongActivity.this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            notesLayout.addView(rowLayout);
            RowViewHolder rowViewHolder = new RowViewHolder(new ArrayList<NoteViewHolder>(), rowLayout);
            rowViewHolders.add(rowViewHolder);
            addPlusToNotesView(rowViewHolder);
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
            RowViewHolder rowViewHolder = new RowViewHolder(new ArrayList<NoteViewHolder>(), rowLayout);
            rowViewHolders.add(rowViewHolder);

            while (i < notes.size()) {

                // add the current note to the horizontal linear layout
                addNoteToNotesView(thisNote, rowViewHolder);

                // if there is no next note or if this note is the last on this row
                if (nextNote == null || (nextNote.getRow() != thisNote.getRow())) {

                    // add a plus note
                    addPlusToNotesView(rowViewHolder);
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
                    rowViewHolder = new RowViewHolder(new ArrayList<NoteViewHolder>(), rowLayout);
                    rowViewHolders.add(rowViewHolder);

                    // if there are no more notes
                    if (i == notes.size() - 1) {

                        // add a last plus
                        addPlusToNotesView(rowViewHolder);
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
     */
    private void addNoteToNotesView(final DbNote dbNote, RowViewHolder rowViewHolder) {

        // set note layout properties
        LinearLayout noteLayout = new LinearLayout(SongActivity.this);
        LinearLayout.LayoutParams noteLayoutParams = new LinearLayout.LayoutParams(MEASURE_CELL_WIDTH, MEASURE_CELL_HEIGHT);
        noteLayoutParams.setMargins(MEASURE_CELL_MARGIN, MEASURE_CELL_MARGIN, MEASURE_CELL_MARGIN, MEASURE_CELL_MARGIN);
        noteLayout.setLayoutParams(noteLayoutParams);
        noteLayout.setGravity(Gravity.CENTER_VERTICAL);
        noteLayout.setOrientation(LinearLayout.VERTICAL);
        noteLayout.setBackground(CustomizationUtils.createNoteBackground(SongActivity.this, dbNote.isBlow() ? blowBackgroundColor : drawBackgroundColor));

        // set note properties
        TextView noteTextView = new TextView(SongActivity.this);
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
        TextView wordText = new TextView(SongActivity.this);
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
        rowViewHolder.getLayout().addView(noteLayout, dbNote.getColumn());
        final NoteViewHolder noteViewHolder = new NoteViewHolder(dbNote, noteLayout);
        rowViewHolder.addNoteViewHolder(noteViewHolder, dbNote.getColumn());

        // set click listener
        noteLayout.setClickable(true);
        noteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotePickerDialog dialog = new NotePickerDialog();
                dialog.setDbNote(dbNote);
                dialog.setListener(SongActivity.this);
                dialog.setEditMode(true);
                dialog.setNoteViewHolder(noteViewHolder);
                dialog.show(getFragmentManager(), Constants.TAG_HARMONICA_NOTES_DIALOG);
            }
        });

    }

    /**
     * Adds a plus button to the notes view.
     */
    private void addPlusToNotesView(final RowViewHolder rowViewHolder) {

        // set add note layout properties
        final RelativeLayout addNoteLayout = new RelativeLayout(SongActivity.this);
        LinearLayout.LayoutParams addNoteLayoutParams = new LinearLayout.LayoutParams(MEASURE_CELL_WIDTH, MEASURE_CELL_HEIGHT);
        addNoteLayoutParams.setMargins(MEASURE_CELL_MARGIN, MEASURE_CELL_MARGIN, MEASURE_CELL_MARGIN, MEASURE_CELL_MARGIN);
        addNoteLayout.setLayoutParams(addNoteLayoutParams);
        addNoteLayout.setClickable(true);

        // set plus image properties
        ImageView plusImage = new ImageView(SongActivity.this);
        RelativeLayout.LayoutParams plusImageLayoutParams = new RelativeLayout.LayoutParams(MEASURE_PLUS_SIZE, MEASURE_PLUS_SIZE);
        plusImageLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        plusImage.setLayoutParams(plusImageLayoutParams);
        plusImage.setScaleType(ImageView.ScaleType.FIT_XY);
        plusImage.setImageResource(R.drawable.selector_round_plus);
        addNoteLayout.addView(plusImage);

        // add view holder
        rowViewHolder.getLayout().addView(addNoteLayout);
        final NoteViewHolder noteViewHolder = new NoteViewHolder(null, addNoteLayout);
        rowViewHolder.addNoteViewHolder(noteViewHolder);

        addNoteLayout.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                int row = 0;
                int column = 0;
                boolean isLastPlus = false;

                List<NoteViewHolder> noteViewHolders = rowViewHolder.getNoteViewHolders();
                if (noteViewHolders.size() <= 1) {

                    // this is the last plus in the tab
                    isLastPlus = true;
                    row = rowViewHolders.size() - 1;
                } else {

                    // get last note in the row
                    NoteViewHolder lastNoteHolder = noteViewHolders.get(noteViewHolders.size() - 2);
                    row = lastNoteHolder.getDbNote().getRow();
                    column = lastNoteHolder.getDbNote().getColumn() + 1;
                }

                DbSection dbSection = getSectionByRow(row);

                // set long press listener with options menu
                final MenuBuilder menuBuilder = new MenuBuilder(SongActivity.this);
                menuBuilder.setOptionalIconsVisible(true);
                MenuInflater inflater = new MenuInflater(SongActivity.this);
                inflater.inflate(R.menu.menu_note_options, menuBuilder);
                final MenuPopupHelper optionsMenu = new MenuPopupHelper(SongActivity.this, menuBuilder, addNoteLayout);
                menuBuilder.findItem(R.id.action_delete_row).setVisible(column != 0);
                menuBuilder.findItem(R.id.action_insert_row).setVisible(!isLastPlus);
                menuBuilder.findItem(R.id.action_copy_row).setVisible(column != 0);
                menuBuilder.findItem(R.id.action_paste_row).setVisible(copiedNotes.size() != 0);
                menuBuilder.findItem(R.id.action_add_section).setVisible(dbSection == null && !isLastPlus);
                menuBuilder.findItem(R.id.action_edit_section).setVisible(dbSection != null);
                menuBuilder.findItem(R.id.action_delete_section).setVisible(dbSection != null);
                final int finalRow = row;
                final int finalColumn = column;
                menuBuilder.setCallback(new MenuBuilder.Callback() {

                    @Override
                    public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_add_note:
                                onAddNoteCommand(finalRow, finalColumn, rowViewHolder, noteViewHolder);
                                break;
                            case R.id.action_delete_row:
                                onDeleteRowCommand(finalRow);
                                break;
                            case R.id.action_insert_row:
                                onNewRowCommand(finalRow);
                                break;
                            case R.id.action_copy_row:
                                onCopyRowCommand(finalRow);
                                break;
                            case R.id.action_paste_row:
                                onPasteRowCommand(finalRow, finalColumn);
                                break;
                            case R.id.action_add_section:
                                onAddSectionCommand(finalRow);
                                break;
                            case R.id.action_edit_section:
                                onEditSectionCommand(finalRow);
                                break;
                            case R.id.action_delete_section:
                                onDeleteSectionCommand(finalRow);
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
        addNoteLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int row = 0;
                int column = 0;

                List<NoteViewHolder> noteViewHolders = rowViewHolder.getNoteViewHolders();
                if (noteViewHolders.size() <= 1) {

                    // this is the last plus in the tab
                    row = rowViewHolders.size() - 1;
                } else {

                    // get last note in the row
                    NoteViewHolder lastNoteHolder = noteViewHolders.get(noteViewHolders.size() - 2);
                    row = lastNoteHolder.getDbNote().getRow();
                    column = lastNoteHolder.getDbNote().getColumn() + 1;
                }
                onAddNoteCommand(row, column, rowViewHolder, noteViewHolder);
            }
        });
    }

    /**
     * Adds a section to the notes view.
     *
     * @param dbSection
     */
    private void addSectionToNotesView(DbSection dbSection, LinearLayout parent) {

        // set section view properties
        TextView sectionText = new TextView(SongActivity.this);
        sectionText.setPadding(MEASURE_SECTION_PADDING_LEFT, MEASURE_SECTION_PADDING_TOP, 0, MEASURE_SECTION_PADDING_BOTTOM);
        sectionText.setTextSize(MEASURE_SECTION_SIZE);
        sectionText.setText(dbSection.getName());
        CustomizationUtils.styleSectionText(sectionText, sectionStyle, sectionTextColor);
        parent.addView(sectionText);
    }

    /**
     * Adds a note to the position of the plus button.
     *
     * @param row
     * @param column
     */
    private void onAddNoteCommand(int row, int column, RowViewHolder rowViewHolder, NoteViewHolder noteViewHolder) {
        DbNote dbNote = new DbNote();
        dbNote.setRow(row);
        dbNote.setColumn(column);
        dbNote.setSongId(dbSong.getId());
        NotePickerDialog dialog = new NotePickerDialog();
        dialog.setDbNote(dbNote);
        dialog.setListener(SongActivity.this);
        dialog.setEditMode(false);
        dialog.setNewRow(false);
        dialog.setRowViewHolder(rowViewHolder);
        dialog.setNoteViewHolder(noteViewHolder);
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

        ActiveAndroid.beginTransaction();
        try {

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
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
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
            CustomToast.makeText(SongActivity.this, copedNotesNr + " " + getString(R.string.song_activity_toast_note_copied), Toast.LENGTH_SHORT).show();
        } else {
            CustomToast.makeText(SongActivity.this, copedNotesNr + " " + getString(R.string.song_activity_toast_notes_copied), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(SongActivity.this, R.string.song_activity_toast_no_notes_copied, Toast.LENGTH_SHORT).show();
        } else {
            ActiveAndroid.beginTransaction();
            try {
                int currentColumn = column;
                for (DbNote copiedNote : copiedNotes) {
                    DbNote pastedNote = new DbNote(copiedNote);
                    pastedNote.setRow(row);
                    pastedNote.setColumn(currentColumn);
                    notes.add(pastedNote);
                    NoteDbHandler.insertNote(pastedNote);
                    currentColumn++;
                }
                ActiveAndroid.setTransactionSuccessful();
            } finally {
                ActiveAndroid.endTransaction();
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
    public void onNoteAdded(DbNote dbNote, boolean newRow, RowViewHolder rowViewHolder) {
        if (newRow) {
            ActiveAndroid.beginTransaction();
            try {
                incrementRows(dbNote.getRow());
                incrementSections(dbNote.getRow());
                ActiveAndroid.setTransactionSuccessful();
            } finally {
                ActiveAndroid.endTransaction();
            }
        }
        notes.add(dbNote);
        NoteDbHandler.insertNote(dbNote);
        Collections.sort(notes, notesComparator);
        addNoteToNotesView(dbNote, rowViewHolder);
    }

    @Override
    public void onNoteEdited(DbNote dbNote, NoteViewHolder noteViewHolder) {
        NoteDbHandler.insertNote(dbNote);
        TextView noteTextView = (TextView) ((LinearLayout) noteViewHolder.getView()).getChildAt(0);
        if (dbNote.isBlow()) {
            CustomizationUtils.styleNoteText(noteTextView, dbNote.getHole(), dbNote.getBend(), blowSign, blowStyle, blowTextColor);
        } else {
            CustomizationUtils.styleNoteText(noteTextView, dbNote.getHole(), dbNote.getBend(), drawSign, drawStyle, drawTextColor);
        }
        TextView wordTextView = (TextView) ((LinearLayout) noteViewHolder.getView()).getChildAt(1);
        wordTextView.setText(dbNote.getWord());
        wordTextView.setVisibility(dbNote.getWord().isEmpty() ? View.GONE : View.VISIBLE);

    }

    @Override
    public void onNoteDeleted(DbNote dbNote, NoteViewHolder noteViewHolder, RowViewHolder rowViewHolder) {

        // delete the note
        int position = notes.indexOf(dbNote);
        notes.remove(dbNote);
        NoteDbHandler.deleteNote(dbNote);

        // remove the view
        rowViewHolder.getLayout().removeView(noteViewHolder.getView());
        rowViewHolder.getNoteViewHolders().remove(noteViewHolder);

        // check the remaining notes on the row
        int rowCount = 0;
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getRow() == dbNote.getRow()) {
                rowCount++;
            }
        }

        ActiveAndroid.beginTransaction();
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

            // remove the row layout
            notesLayout.removeView(rowViewHolder.getLayout());
            rowViewHolders.remove(rowViewHolder);
        }
        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();

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
     * TODO maybe delete this
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

    private void initCustomizations() {
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
        backgroundColor = CustomizationUtils.getBackgroundColor();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == REQUEST_CODE_ADD_AUDIO_FILE && resultCode == RESULT_OK) {
            CustomToast.makeText(SongActivity.this, R.string.song_activity_toast_adding_audio_file, Toast.LENGTH_SHORT).show();
            Thread thread = new Thread() {

                @Override
                public void run() {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(data.getData());
                        String fileName = ExportHelper.getInstance().getFileName(SongActivity.this, data.getData());
                        String finalName = dbSong.getId() + Constants.SEPARATOR_AUDIO_FILE + fileName;
                        ExportHelper.getInstance().saveToInternalStorage(SongActivity.this, finalName, inputStream);
                        dbSong.setAudioFile(finalName);
                        SongDbHandler.insertSong(dbSong);
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    CustomToast.makeText(SongActivity.this, R.string.song_activity_toast_audio_file_added, Toast.LENGTH_SHORT).show();
                                    initMediaPlayer();
                                    if (mediaView.getVisibility() == View.GONE) {
                                        animateMediaView(true);
                                    }
                                    invalidateOptionsMenu();
                                    sendSongsUpdatedBroadcast();
                                } catch (Exception e) {
                                    handleAddAudioFileException();
                                }
                            }
                        });
                    } catch (Exception e) {
                        handleAddAudioFileException();
                    }
                }
            };
            thread.start();
        }
    }

    private void handleAddAudioFileException() {
        ExportHelper.getInstance().removeAudioFile(SongActivity.this, dbSong);
        dbSong.setAudioFile(null);
        SongDbHandler.insertSong(dbSong);
        releaseMediaPlayer();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomToast.makeText(SongActivity.this, R.string.song_activity_toast_add_audio_file_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_STORAGE_PERMISSION: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    launchAddAudioFileActivity();
                } else {
                    CustomToast.makeText(SongActivity.this, R.string.song_activity_toast_permission_needed, Toast.LENGTH_LONG).show();
                }
                return;
            }
            default:
                break;
        }
    }

    private void launchAddAudioFileActivity() {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, REQUEST_CODE_ADD_AUDIO_FILE);
        } catch (ActivityNotFoundException e) {
            CustomToast.makeText(SongActivity.this, R.string.song_activity_toast_error_no_filepicker_app, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(customizationReceiver);
        releaseMediaPlayer();
        super.onDestroy();
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaHandler.removeCallbacks(updateSongViewsRunnable);
            mediaPlayer.release();
        }
    }

    private void sendSongsUpdatedBroadcast() {
        Intent intent = new Intent(Constants.INTENT_SONGS_UPDATED);
        sendBroadcast(intent);
    }

    @Override
    public void onNotesShiftedUp() {
        shiftNotes(true);
    }

    @Override
    public void onNotesShiftedDown() {
        shiftNotes(false);
    }

    @Override
    public void onNotesShiftConfirmationRequested(final boolean shiftUp, int eligibleNotes, int allNotes) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SongActivity.this);
        builder.setTitle(String.format(getString(R.string.song_activity_shift_warning_dialog_title), eligibleNotes));
        builder.setMessage(String.format(getString(R.string.song_activity_shift_warning_dialog_description), (allNotes - eligibleNotes), allNotes, eligibleNotes));
        builder.setPositiveButton(R.string.button_shift, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                shiftNotes(shiftUp);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void shiftNotes(boolean increase) {
        for (DbNote dbNote : notes) {
            if (increase) {
                NotesShiftUtils.increase(dbNote);
            } else {
                NotesShiftUtils.decrease(dbNote);
            }
        }
        ActiveAndroid.beginTransaction();
        NoteDbHandler.insertNotes(notes);
        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();
        drawNotes();
        CustomToast.makeText(SongActivity.this, R.string.song_activity_toast_notes_shifted, Toast.LENGTH_SHORT).show();
    }
}
