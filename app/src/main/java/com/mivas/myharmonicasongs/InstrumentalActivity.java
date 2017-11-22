package com.mivas.myharmonicasongs;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.mivas.myharmonicasongs.adapter.ScrollTimersAdapter;
import com.mivas.myharmonicasongs.database.handler.ScrollTimerDbHandler;
import com.mivas.myharmonicasongs.database.handler.SectionDbHandler;
import com.mivas.myharmonicasongs.database.handler.SongDbHandler;
import com.mivas.myharmonicasongs.database.model.DbScrollTimer;
import com.mivas.myharmonicasongs.database.model.DbSection;
import com.mivas.myharmonicasongs.database.model.DbSong;
import com.mivas.myharmonicasongs.dialog.SectionLinePickerDialog;
import com.mivas.myharmonicasongs.dialog.SectionPickerDialog;
import com.mivas.myharmonicasongs.dialog.TimePickerDialog;
import com.mivas.myharmonicasongs.listener.ScrollTimersAdapterListener;
import com.mivas.myharmonicasongs.listener.SectionLinePickerListener;
import com.mivas.myharmonicasongs.listener.SectionPickerListener;
import com.mivas.myharmonicasongs.listener.TimePickerListener;
import com.mivas.myharmonicasongs.util.AudioFileUtils;
import com.mivas.myharmonicasongs.util.Constants;
import com.mivas.myharmonicasongs.util.CustomToast;
import com.mivas.myharmonicasongs.util.CustomizationUtils;
import com.mivas.myharmonicasongs.util.ExportHelper;
import com.mivas.myharmonicasongs.util.PreferencesUtils;

import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class InstrumentalActivity extends AppCompatActivity implements ScrollTimersAdapterListener {

    private DbSong dbSong;
    private View addAudioFileView;
    private View removeAudioFileView;
    private TextView audioFileText;
    private ProgressDialog progressDialog;
    private MediaPlayer mediaPlayer;
    private View addScrollTimerView;
    private Comparator sectionsComparator;
    private Comparator scrollTimersComparator;
    private RecyclerView scrollTimersList;
    private Switch enableScrollTimersSwitch;
    private View scrollTimersLayout;
    private ScrollTimersAdapter scrollTimersAdapter;
    private List<DbScrollTimer> dbScrollTimers;
    private List<DbSection> dbSections;
    private boolean scrollTimersChanged = false;

    private static final int REQUEST_CODE_ADD_AUDIO_FILE = 1;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrumental);

        dbSong = SongDbHandler.getSongById(getIntent().getLongExtra(Constants.EXTRA_SONG_ID, 0));
        dbScrollTimers = ScrollTimerDbHandler.getScrollTimersBySongId(dbSong.getId());
        dbSections = SectionDbHandler.getSectionsBySongId(dbSong.getId());
        initComparators();
        Collections.sort(dbScrollTimers, scrollTimersComparator);
        Collections.sort(dbSections, sectionsComparator);
        initViews();
        initListeners();
        refreshAudioFileView();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Views initializer.
     */
    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        addAudioFileView = findViewById(R.id.view_add_audio_file);
        removeAudioFileView = findViewById(R.id.view_remove_audio_file);
        audioFileText = findViewById(R.id.text_audio_file);
        addScrollTimerView = findViewById(R.id.view_add_scroll_timer);
        boolean scrollTimersEnabled = CustomizationUtils.getScrollTimersEnabled();
        enableScrollTimersSwitch = findViewById(R.id.switch_enable_scroll_timers);
        enableScrollTimersSwitch.setChecked(scrollTimersEnabled);
        scrollTimersLayout = findViewById(R.id.layout_scroll_timers);
        scrollTimersLayout.setVisibility(scrollTimersEnabled ? View.VISIBLE : View.GONE);
        scrollTimersList = findViewById(R.id.list_scroll_timers);
        scrollTimersList.setLayoutManager(new LinearLayoutManager(InstrumentalActivity.this, LinearLayout.VERTICAL, false));
        scrollTimersAdapter = new ScrollTimersAdapter(InstrumentalActivity.this, dbScrollTimers, InstrumentalActivity.this);
        scrollTimersList.setAdapter(scrollTimersAdapter);
    }

    /**
     * Listeners initializer.
     */
    private void initListeners() {
        addAudioFileView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(InstrumentalActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(InstrumentalActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
                } else {
                    launchAddAudioFileActivity();
                }
            }
        });
        removeAudioFileView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ExportHelper.getInstance().removeAudioFile(InstrumentalActivity.this, dbSong);
                dbSong.setAudioFile(null);
                SongDbHandler.insertSong(dbSong);
                refreshAudioFileView();
                CustomToast.makeText(InstrumentalActivity.this, R.string.instrumental_activity_toast_audio_file_removed, Toast.LENGTH_SHORT).show();
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
                sendAudioFileUpdatedUpdatedBroadcast();
                sendSongsUpdatedBroadcast();
            }
        });
        addScrollTimerView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (dbSections.isEmpty()) {
                    CustomToast.makeText(InstrumentalActivity.this, R.string.instrumental_activity_toast_no_sections, Toast.LENGTH_SHORT).show();
                } else {
                    final DbScrollTimer dbScrollTimer = new DbScrollTimer();
                    dbScrollTimer.setSongId(dbSong.getId());
                    final TimePickerDialog timePickerDialog = new TimePickerDialog();
                    timePickerDialog.setListener(new TimePickerListener() {

                        @Override
                        public void onTimePicked(int millis) {
                            dbScrollTimer.setTime(millis);
                            SectionPickerDialog sectionPickerDialog = new SectionPickerDialog();
                            sectionPickerDialog.setDbSections(dbSections);
                            sectionPickerDialog.setListener(new SectionPickerListener() {

                                @Override
                                public void onSectionSelected(DbSection dbSection) {
                                    dbScrollTimer.setSectionId(dbSection.getId());

                                    SectionLinePickerDialog sectionLinePickerDialog = new SectionLinePickerDialog();
                                    sectionLinePickerDialog.setListener(new SectionLinePickerListener() {

                                        @Override
                                        public void onSectionLinePicked(int line) {
                                            dbScrollTimer.setSectionLine(line);
                                            dbScrollTimer.save();
                                            dbScrollTimers.add(dbScrollTimer);
                                            Collections.sort(dbScrollTimers, scrollTimersComparator);
                                            scrollTimersAdapter.notifyDataSetChanged();
                                            scrollTimersChanged = true;
                                        }
                                    });
                                    sectionLinePickerDialog.show(getFragmentManager(), Constants.TAG_SECTION_LINE_PICKER_DIALOG);
                                }
                            });
                            sectionPickerDialog.show(getFragmentManager(), Constants.TAG_SECTION_PICKER_DIALOG);
                        }
                    });
                    timePickerDialog.show(getFragmentManager(), Constants.TAG_TIME_PICKER_DIALOG);
                }
            }
        });
        enableScrollTimersSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferencesUtils.storePreference(Constants.PREF_CURRENT_SCROLL_TIMERS_ENABLED, isChecked);
                scrollTimersLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                scrollTimersChanged = true;
            }
        });
    }

    private void refreshAudioFileView() {
        if (dbSong.getAudioFile() == null) {
            addAudioFileView.setVisibility(View.VISIBLE);
            removeAudioFileView.setVisibility(View.GONE);
            audioFileText.setVisibility(View.GONE);
        } else {
            addAudioFileView.setVisibility(View.GONE);
            removeAudioFileView.setVisibility(View.VISIBLE);
            audioFileText.setVisibility(View.VISIBLE);
            audioFileText.setText(AudioFileUtils.getRawFileName(dbSong));
        }
    }

    private void sendSongsUpdatedBroadcast() {
        Intent intent = new Intent(Constants.INTENT_SONGS_UPDATED);
        sendBroadcast(intent);
    }

    private void sendAudioFileUpdatedUpdatedBroadcast() {
        Intent intent = new Intent(Constants.INTENT_AUDIO_FILE_UPDATED);
        sendBroadcast(intent);
    }

    private void launchAddAudioFileActivity() {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, REQUEST_CODE_ADD_AUDIO_FILE);
        } catch (ActivityNotFoundException e) {
            CustomToast.makeText(InstrumentalActivity.this, R.string.instrumental_activity_toast_error_no_filepicker_app, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_STORAGE_PERMISSION: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    launchAddAudioFileActivity();
                } else {
                    CustomToast.makeText(InstrumentalActivity.this, R.string.instrumental_activity_toast_permission_needed, Toast.LENGTH_LONG).show();
                }
                return;
            }
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == REQUEST_CODE_ADD_AUDIO_FILE && resultCode == RESULT_OK) {
            showProgress(getString(R.string.instrumental_activity_text_adding_audio_file));
            Thread thread = new Thread() {

                @Override
                public void run() {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(data.getData());
                        String fileName = ExportHelper.getInstance().getFileName(InstrumentalActivity.this, data.getData());
                        String finalName = AudioFileUtils.getFormattedFileName(dbSong, fileName);
                        ExportHelper.getInstance().saveToInternalStorage(InstrumentalActivity.this, finalName, inputStream);
                        dbSong.setAudioFile(finalName);
                        SongDbHandler.insertSong(dbSong);
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    Uri songUri = ExportHelper.getInstance().getAudioFileUri(InstrumentalActivity.this, dbSong);
                                    mediaPlayer = MediaPlayer.create(InstrumentalActivity.this, songUri);
                                    if (mediaPlayer == null) {
                                        throw new Exception();
                                    } else {
                                        mediaPlayer.release();
                                    }
                                    progressDialog.dismiss();
                                    refreshAudioFileView();
                                    CustomToast.makeText(InstrumentalActivity.this, R.string.instrumental_activity_toast_audio_file_added, Toast.LENGTH_SHORT).show();
                                    sendAudioFileUpdatedUpdatedBroadcast();
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

    private void showProgress(String message) {
        progressDialog = new ProgressDialog(InstrumentalActivity.this);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    private void handleAddAudioFileException() {
        ExportHelper.getInstance().removeAudioFile(InstrumentalActivity.this, dbSong);
        dbSong.setAudioFile(null);
        SongDbHandler.insertSong(dbSong);
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                CustomToast.makeText(InstrumentalActivity.this, R.string.instrumental_activity_toast_add_audio_file_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initComparators() {
        sectionsComparator = new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                DbSection section1 = (DbSection) o1;
                DbSection section2 = (DbSection) o2;
                if (section1.getRow() < section2.getRow()) {
                    return -1;
                } else if (section1.getRow() > section2.getRow()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };
        scrollTimersComparator = new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                DbScrollTimer dbScrollTimer1 = (DbScrollTimer) o1;
                DbScrollTimer dbScrollTimer2 = (DbScrollTimer) o2;
                if (dbScrollTimer1.getTime() < dbScrollTimer2.getTime()) {
                    return -1;
                } else if (dbScrollTimer1.getTime() > dbScrollTimer2.getTime()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };
    }

    @Override
    public void onScrollTimerDeleted(DbScrollTimer dbScrollTimer) {
        dbScrollTimers.remove(dbScrollTimer);
        dbScrollTimer.delete();
        scrollTimersAdapter.notifyDataSetChanged();
        scrollTimersChanged = true;
    }

    @Override
    public void onTimeSelected(final DbScrollTimer dbScrollTimer) {
        TimePickerDialog timePickerDialog = new TimePickerDialog();
        timePickerDialog.setDbScrollTimer(dbScrollTimer);
        timePickerDialog.setListener(new TimePickerListener() {

            @Override
            public void onTimePicked(int millis) {
                dbScrollTimer.setTime(millis);
                dbScrollTimer.save();
                Collections.sort(dbScrollTimers, scrollTimersComparator);
                scrollTimersAdapter.notifyDataSetChanged();
                scrollTimersChanged = true;
            }
        });
        timePickerDialog.show(getFragmentManager(), Constants.TAG_TIME_PICKER_DIALOG);
    }

    @Override
    public void onSectionSelected(final DbScrollTimer dbScrollTimer) {
        SectionPickerDialog sectionPickerDialog = new SectionPickerDialog();
        sectionPickerDialog.setDbSections(dbSections);
        sectionPickerDialog.setListener(new SectionPickerListener() {

            @Override
            public void onSectionSelected(DbSection dbSection) {
                dbScrollTimer.setSectionId(dbSection.getId());
                dbScrollTimer.save();
                scrollTimersAdapter.notifyDataSetChanged();
                scrollTimersChanged = true;
            }
        });
        sectionPickerDialog.show(getFragmentManager(), Constants.TAG_SECTION_PICKER_DIALOG);
    }

    @Override
    public void onSectionLineSelected(final DbScrollTimer dbScrollTimer) {
        SectionLinePickerDialog sectionLinePickerDialog = new SectionLinePickerDialog();
        sectionLinePickerDialog.setDbScrollTimer(dbScrollTimer);
        sectionLinePickerDialog.setListener(new SectionLinePickerListener() {

            @Override
            public void onSectionLinePicked(int line) {
                dbScrollTimer.setSectionLine(line);
                dbScrollTimer.save();
                scrollTimersAdapter.notifyDataSetChanged();
                scrollTimersChanged = true;
            }
        });
        sectionLinePickerDialog.show(getFragmentManager(), Constants.TAG_SECTION_LINE_PICKER_DIALOG);
    }

    @Override
    protected void onDestroy() {
        if (scrollTimersChanged) {
            Intent intent = new Intent(Constants.INTENT_SCROLL_TIMERS_UPDATED);
            sendBroadcast(intent);
        }
        super.onDestroy();
    }
}
