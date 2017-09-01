package com.mivas.myharmonicasongs;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.mivas.myharmonicasongs.database.handler.NoteDbHandler;
import com.mivas.myharmonicasongs.database.handler.SectionDbHandler;
import com.mivas.myharmonicasongs.database.handler.SongDbHandler;
import com.mivas.myharmonicasongs.database.model.DbNote;
import com.mivas.myharmonicasongs.database.model.DbSection;
import com.mivas.myharmonicasongs.database.model.DbSong;
import com.mivas.myharmonicasongs.dialog.NotesShiftDialog;
import com.mivas.myharmonicasongs.exception.MediaPlayerException;
import com.mivas.myharmonicasongs.listener.NotesShiftListener;
import com.mivas.myharmonicasongs.listener.SongActivityListener;
import com.mivas.myharmonicasongs.util.Constants;
import com.mivas.myharmonicasongs.util.CustomToast;
import com.mivas.myharmonicasongs.util.CustomizationUtils;
import com.mivas.myharmonicasongs.util.ExportHelper;
import com.mivas.myharmonicasongs.util.NotesShiftUtils;
import com.mivas.myharmonicasongs.view.MediaPlayerView;
import com.mivas.myharmonicasongs.view.TablatureView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity that displays dbNotes.
 */
public class SongActivity extends AppCompatActivity implements SongActivityListener, NotesShiftListener {

    private TablatureView tablatureView;
    private MediaPlayerView mediaPlayerView;
    private DbSong dbSong;
    private List<DbNote> dbNotes = new ArrayList<DbNote>();
    private List<DbSection> dbSections = new ArrayList<DbSection>();
    private TextView noNotesText;
    private View backgroundView;
    private BroadcastReceiver customizationReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            tablatureView.initCustomizations();
            tablatureView.initialize();
            backgroundView.setBackgroundColor(CustomizationUtils.getBackgroundColor());
        }
    };

    private static final int REQUEST_CODE_ADD_AUDIO_FILE = 1;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initViews();

        dbSong = SongDbHandler.getSongById(getIntent().getLongExtra(Constants.EXTRA_SONG_ID, 0));
        dbNotes = NoteDbHandler.getNotesBySongId(dbSong.getId());
        dbSections = SectionDbHandler.getSectionsBySongId(dbSong.getId());
        getSupportActionBar().setTitle(dbSong.getTitle());
        backgroundView.setBackgroundColor(CustomizationUtils.getBackgroundColor());

        // init tablature
        tablatureView.setDbSong(dbSong);
        tablatureView.setDbNotes(dbNotes);
        tablatureView.setDbSections(dbSections);
        tablatureView.setSongActivityListener(SongActivity.this);
        tablatureView.initialize();

        try {
            mediaPlayerView.setDbSong(dbSong);
            mediaPlayerView.initialize();
            if (dbSong.getAudioFile() != null) {
                tablatureView.setMediaPadding(true);
                mediaPlayerView.animate(true);
            }
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
                if (mediaPlayerView.isDisplayed()) {
                    tablatureView.setMediaPadding(false);
                    mediaPlayerView.animate(false);
                }
                invalidateOptionsMenu();
                mediaPlayerView.release();
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
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        noNotesText = (TextView) findViewById(R.id.text_no_notes);
        backgroundView = findViewById(R.id.view_background);
        tablatureView = (TablatureView) findViewById(R.id.view_tablature);
        mediaPlayerView = (MediaPlayerView) findViewById(R.id.view_media);
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
                                    mediaPlayerView.initialize();
                                    if (!mediaPlayerView.isDisplayed()) {
                                        tablatureView.setMediaPadding(true);
                                        mediaPlayerView.animate(true);
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
        mediaPlayerView.release();
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
        mediaPlayerView.release();
        super.onDestroy();
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
}
