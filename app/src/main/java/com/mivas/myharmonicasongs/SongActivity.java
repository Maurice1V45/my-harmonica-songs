package com.mivas.myharmonicasongs;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.mivas.myharmonicasongs.animation.SectionBarAnimation;
import com.mivas.myharmonicasongs.animation.SlideAnimation;
import com.mivas.myharmonicasongs.database.handler.NoteDbHandler;
import com.mivas.myharmonicasongs.database.handler.SectionDbHandler;
import com.mivas.myharmonicasongs.database.handler.SongDbHandler;
import com.mivas.myharmonicasongs.database.model.DbNote;
import com.mivas.myharmonicasongs.database.model.DbSection;
import com.mivas.myharmonicasongs.database.model.DbSong;
import com.mivas.myharmonicasongs.dialog.NotesShiftDialog;
import com.mivas.myharmonicasongs.exception.MediaPlayerException;
import com.mivas.myharmonicasongs.listener.MediaPlayerListener;
import com.mivas.myharmonicasongs.listener.NotesShiftDialogListener;
import com.mivas.myharmonicasongs.listener.SectionBarListener;
import com.mivas.myharmonicasongs.listener.TablatureListener;
import com.mivas.myharmonicasongs.model.CellLine;
import com.mivas.myharmonicasongs.util.Constants;
import com.mivas.myharmonicasongs.util.CustomToast;
import com.mivas.myharmonicasongs.util.CustomizationUtils;
import com.mivas.myharmonicasongs.util.DimensionUtils;
import com.mivas.myharmonicasongs.util.ExportHelper;
import com.mivas.myharmonicasongs.util.NotesShiftUtils;
import com.mivas.myharmonicasongs.util.SongKeyUtils;
import com.mivas.myharmonicasongs.view.MediaPlayerView;
import com.mivas.myharmonicasongs.view.NotePickerView;
import com.mivas.myharmonicasongs.view.SectionBarView;
import com.mivas.myharmonicasongs.view.TablatureView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity that displays dbNotes.
 */
public class SongActivity extends AppCompatActivity implements TablatureListener, NotesShiftDialogListener, SectionBarListener, MediaPlayerListener {

    private TablatureView tablatureView;
    private MediaPlayerView mediaPlayerView;
    private SectionBarView sectionBarView;
    private DbSong dbSong;
    private List<DbNote> dbNotes = new ArrayList<DbNote>();
    private List<DbSection> dbSections = new ArrayList<DbSection>();
    private TextView noNotesText;
    private View backgroundView;
    private boolean showSectionBar;
    private BroadcastReceiver customizationReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            tablatureView.initCustomizations();
            tablatureView.initialize();
            showSectionBar = CustomizationUtils.getShowSectionBar();
            sectionBarView.initCustomizations();
            showSectionBar();
            backgroundView.setBackgroundColor(CustomizationUtils.getBackgroundColor());
        }
    };

    private BroadcastReceiver audioFileReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            invalidateOptionsMenu();
            if (dbSong.getAudioFile() != null) {
                try {
                    mediaPlayerView.release();
                    mediaPlayerView.initialize();
                    if (!mediaPlayerView.isDisplayed()) {
                        tablatureView.setMediaPadding(true);
                        mediaPlayerView.show(true);
                    }
                } catch (MediaPlayerException e) {

                }
            } else {
                if (mediaPlayerView.isDisplayed()) {
                    tablatureView.setMediaPadding(false);
                    mediaPlayerView.show(false);
                }
                mediaPlayerView.release();
            }
        }
    };

    private BroadcastReceiver scrollTimersReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (dbSong.getAudioFile() != null) {
                mediaPlayerView.refreshScrollTimers();
            }
        }
    };

    private void showSectionBar() {
        if (showSectionBar && dbSections.size() > 0) {
            sectionBarView.setVisibility(View.VISIBLE);
            sectionBarView.setCellLines(tablatureView.getCellLines());
            sectionBarView.initialize();
        } else {
            sectionBarView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initViews();

        dbSong = SongDbHandler.getSongById(getIntent().getLongExtra(Constants.EXTRA_SONG_ID, 0));
        dbNotes = NoteDbHandler.getNotesBySongId(dbSong.getId());
        dbSections = SectionDbHandler.getSectionsBySongId(dbSong.getId());
        setSongTitle();
        backgroundView.setBackgroundColor(CustomizationUtils.getBackgroundColor());

        // init tablature
        tablatureView.setDbSong(dbSong);
        tablatureView.setDbNotes(dbNotes);
        tablatureView.setDbSections(dbSections);
        tablatureView.setListener(SongActivity.this);
        tablatureView.initialize();

        // init section bar
        sectionBarView.setListener(SongActivity.this);
        showSectionBar = CustomizationUtils.getShowSectionBar();
        showSectionBar();

        // init media player
        try {
            mediaPlayerView.setDbSong(dbSong);
            mediaPlayerView.setListener(SongActivity.this);
            mediaPlayerView.initialize();
            if (dbSong.getAudioFile() != null && CustomizationUtils.getShowMediaPlayer()) {
                tablatureView.setMediaPadding(true);
                mediaPlayerView.show(true);
            }
        } catch (MediaPlayerException e) {
            CustomToast.makeText(SongActivity.this, R.string.song_activity_toast_media_player_init_error, Toast.LENGTH_SHORT).show();
        }

        registerReceiver(customizationReceiver, new IntentFilter(Constants.INTENT_CUSTOMIZATIONS_UPDATED));
        registerReceiver(audioFileReceiver, new IntentFilter(Constants.INTENT_AUDIO_FILE_UPDATED));
        registerReceiver(scrollTimersReceiver, new IntentFilter(Constants.INTENT_SCROLL_TIMERS_UPDATED));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_song_activity, menu);
        MenuItem toggleMedia = menu.findItem(R.id.action_toggle_media);
        if (dbSong.getAudioFile() == null) {
            toggleMedia.setVisible(false);
        } else {
            toggleMedia.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        tablatureView.getNotePickerView().closeTextMode(false);
        tablatureView.getNotePickerView().closeSectionTextMode(false);
        closeNotePicker();

        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_instrumental:
                Intent instrumentalIntent = new Intent(SongActivity.this, InstrumentalActivity.class);
                instrumentalIntent.putExtra(Constants.EXTRA_SONG_ID, dbSong.getId());
                startActivity(instrumentalIntent);
                return true;
            case R.id.action_customize:
                Intent intent = new Intent(SongActivity.this, CustomizeActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_toggle_media:
                if (dbSong.getAudioFile() == null) {
                    CustomToast.makeText(SongActivity.this, R.string.song_activity_toast_no_audio_file, Toast.LENGTH_SHORT).show();
                } else {
                    tablatureView.setMediaPadding(!mediaPlayerView.isDisplayed());
                    mediaPlayerView.animate(!mediaPlayerView.isDisplayed());
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
                dialog.setDbNotes(dbNotes);
                dialog.setListener(SongActivity.this);
                dialog.show(getFragmentManager(), Constants.TAG_NOTES_SHIFT_DIALOG);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Views initializer.
     */
    private void initViews() {
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        noNotesText = findViewById(R.id.text_no_notes);
        backgroundView = findViewById(R.id.view_background);
        sectionBarView = findViewById(R.id.view_section_bar);
        tablatureView = findViewById(R.id.view_tablature);
        mediaPlayerView = findViewById(R.id.view_media);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(customizationReceiver);
        unregisterReceiver(audioFileReceiver);
        unregisterReceiver(scrollTimersReceiver);
        mediaPlayerView.release();
        super.onDestroy();
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
        for (DbNote dbNote : dbNotes) {
            if (increase) {
                NotesShiftUtils.increase(dbNote);
            } else {
                NotesShiftUtils.decrease(dbNote);
            }
        }
        ActiveAndroid.beginTransaction();
        NoteDbHandler.insertNotes(dbNotes);
        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();
        tablatureView.initialize();
        CustomToast.makeText(SongActivity.this, R.string.song_activity_toast_notes_shifted, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNotesChanged(List<DbNote> dbNotes) {
        if (dbNotes.isEmpty()) {
            noNotesText.setVisibility(View.VISIBLE);
        } else {
            noNotesText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        NotePickerView notePickerView = tablatureView.getNotePickerView();
        if (notePickerView.isNotePickerDisplayed()) {
            if (notePickerView.getTextLayout().getVisibility() == View.VISIBLE) {
                notePickerView.getTextLayout().setVisibility(View.GONE);
                notePickerView.getNotesLayout().setVisibility(View.VISIBLE);
                notePickerView.animate(true);
            } else if (notePickerView.getSectionTextLayout().getVisibility() == View.VISIBLE) {
                notePickerView.getSectionTextLayout().setVisibility(View.GONE);
                notePickerView.getNotesLayout().setVisibility(View.VISIBLE);
                notePickerView.animate(true);
            } else {
                closeNotePicker();
            }
        } else {
            super.onBackPressed();
        }
    }

    private void closeNotePicker() {
        if (tablatureView.getNotePickerView().isNotePickerDisplayed()) {
            tablatureView.getNotePickerView().animate(false);
            tablatureView.onNotePickerClosed();
        }
    }

    private void setSongTitle() {
        StringBuilder builder = new StringBuilder();
        builder.append(dbSong.getTitle()).append(" (").append(SongKeyUtils.getKey(dbSong.getKey()));
        if (dbSong.getKey() > 11) {
            builder.append("min");
        }
        builder.append(")");
        getSupportActionBar().setTitle(builder.toString());
    }

    @Override
    public void onSectionSelected(CellLine cellLine) {
        tablatureView.smoothScrollToCellLine(cellLine, null);
    }

    @Override
    public void onSectionsChanged(List<DbSection> dbSections) {
        if (showSectionBar) {
            if (!dbSections.isEmpty()) {
                sectionBarView.setCellLines(tablatureView.getCellLines());
                sectionBarView.initialize();
                if (sectionBarView.getVisibility() == View.GONE) {
                    SectionBarAnimation sectionBarAnimation = new SectionBarAnimation(sectionBarView, 300, SlideAnimation.EXPAND);
                    sectionBarAnimation.setHeight(DimensionUtils.dpToPx(SongActivity.this, 30));
                    sectionBarView.startAnimation(sectionBarAnimation);
                }
            } else {
                SectionBarAnimation sectionBarAnimation = new SectionBarAnimation(sectionBarView, 200, SlideAnimation.COLLAPSE);
                sectionBarAnimation.setHeight(DimensionUtils.dpToPx(SongActivity.this, 30));
                sectionBarView.startAnimation(sectionBarAnimation);
            }
        }
    }

    @Override
    public void onScrollToSection(long sectionId, int sectionLine) {
        tablatureView.smoothScrollToSection(sectionId, sectionLine);
    }
}
