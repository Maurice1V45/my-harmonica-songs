package com.mivas.myharmonicasongs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mivas.myharmonicasongs.adapter.SongsListAdapter;
import com.mivas.myharmonicasongs.animation.SlideAnimation;
import com.mivas.myharmonicasongs.database.handler.NoteDbHandler;
import com.mivas.myharmonicasongs.database.handler.SectionDbHandler;
import com.mivas.myharmonicasongs.database.handler.SongDbHandler;
import com.mivas.myharmonicasongs.database.model.DbSong;
import com.mivas.myharmonicasongs.dialog.SongDialog;
import com.mivas.myharmonicasongs.listener.MainActivityListener;
import com.mivas.myharmonicasongs.util.Constants;
import com.mivas.myharmonicasongs.util.CustomToast;
import com.mivas.myharmonicasongs.util.DimensionUtils;
import com.mivas.myharmonicasongs.util.FirstRunUtils;
import com.mivas.myharmonicasongs.util.KeyboardUtils;
import com.mivas.myharmonicasongs.util.PreferencesUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Main Activity.
 */
public class MainActivity extends AppCompatActivity implements MainActivityListener {

    private class SongsUpdatedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(Constants.EXTRA_SONGS_UPDATED_COUNT)) {
                int songsImported = intent.getIntExtra(Constants.EXTRA_SONGS_UPDATED_COUNT, 0);
                if (songsImported == 0) {
                    CustomToast.makeText(MainActivity.this, songsImported + " " + getString(R.string.settings_activity_toast_no_song_imported), Toast.LENGTH_SHORT).show();
                } else if (songsImported == 1) {
                    CustomToast.makeText(MainActivity.this, songsImported + " " + getString(R.string.settings_activity_toast_song_imported), Toast.LENGTH_SHORT).show();
                } else {
                    CustomToast.makeText(MainActivity.this, songsImported + " " + getString(R.string.settings_activity_toast_songs_imported), Toast.LENGTH_SHORT).show();
                }
            }
            dbSongs = SongDbHandler.getSongs();
            refreshSongsList();
        }
    }

    private RecyclerView songsListView;
    private SongsListAdapter songsListAdapter;
    private List<DbSong> dbSongs;
    private List<DbSong> displayedSongs;
    private TextView noSongsText;
    private View searchView;
    private boolean searchMode = false;
    private EditText searchField;
    private SongsUpdatedReceiver songsUpdatedReceiver = new SongsUpdatedReceiver();
    private Comparator<DbSong> songsComparator = new Comparator<DbSong>() {

        @Override
        public int compare(DbSong song1, DbSong song2) {
            return song1.getTitle().compareTo(song2.getTitle());
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initListeners();
        if (PreferencesUtils.getPreferences().getBoolean(Constants.PREF_FIRST_RUN, true)) {
            PreferencesUtils.storePreference(Constants.PREF_FIRST_RUN, false);
            FirstRunUtils.addSampleSong();
        }
        dbSongs = SongDbHandler.getSongs();
        displayedSongs = new ArrayList<DbSong>();
        songsListAdapter = new SongsListAdapter(MainActivity.this, dbSongs, MainActivity.this);
        songsListView.setAdapter(songsListAdapter);
        toggleNoSongsText();
        registerReceiver(songsUpdatedReceiver, new IntentFilter(Constants.INTENT_SONGS_UPDATED));
    }

    private void initViews() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        songsListView = (RecyclerView) findViewById(R.id.list_songs);
        songsListView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayout.VERTICAL, false));
        noSongsText = (TextView) findViewById(R.id.text_no_songs);
        searchView = findViewById(R.id.view_search);
        searchField = (EditText) findViewById(R.id.field_search);
    }

    private void initListeners() {
        searchField.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                refreshDisplayedSongs();
                songsListAdapter.setSongs(displayedSongs);
                songsListAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void refreshDisplayedSongs() {
        String text = searchField.getText().toString();
        displayedSongs.clear();
        for (DbSong dbSong : dbSongs) {
            if (dbSong.getTitle().toLowerCase().contains(text.toLowerCase()) || dbSong.getAuthor().toLowerCase().contains(text.toLowerCase())) {
                displayedSongs.add(dbSong);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_search_song:
                if (!searchMode) {
                    searchView.setVisibility(View.VISIBLE);
                    KeyboardUtils.focusEditText(MainActivity.this, searchField);
                    SlideAnimation slideAnimation = new SlideAnimation(searchView, 200, SlideAnimation.EXPAND);
                    slideAnimation.setHeight(DimensionUtils.dpToPx(MainActivity.this, 66));
                    searchView.startAnimation(slideAnimation);
                } else {
                    KeyboardUtils.closeKeyboard(MainActivity.this);
                    searchField.setText("");
                    SlideAnimation slideAnimation = new SlideAnimation(searchView, 200, SlideAnimation.COLLAPSE);
                    searchView.startAnimation(slideAnimation);
                    refreshSongsList();
                }
                searchMode = !searchMode;
                return true;
            case R.id.action_add_song:
                SongDialog songDialog = new SongDialog();
                songDialog.setListener(MainActivity.this);
                songDialog.show(getFragmentManager(), Constants.TAG_SONG_DIALOG);
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSongAdded(DbSong dbSong) {
        dbSongs.add(dbSong);
        Collections.sort(dbSongs, songsComparator);
        SongDbHandler.insertSong(dbSong);
        refreshSongsList();

        // open song
        Intent intent = new Intent(MainActivity.this, SongActivity.class);
        intent.putExtra(Constants.EXTRA_SONG_ID, dbSong.getId());
        startActivity(intent);
    }

    @Override
    public void onSongEdit(DbSong dbSong) {
        SongDialog songDialog = new SongDialog();
        songDialog.setSong(dbSong);
        songDialog.setListener(MainActivity.this);
        songDialog.show(getFragmentManager(), Constants.TAG_SONG_DIALOG);
    }

    @Override
    public void onSongEditConfirmed(DbSong dbSong) {
        SongDbHandler.insertSong(dbSong);
        Collections.sort(dbSongs, songsComparator);
        refreshSongsList();
    }

    @Override
    public void onSongDelete(final DbSong dbSong) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(String.format(getString(R.string.main_activity_delete_dialog_description), dbSong.getTitle()));
                builder.setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbSongs.remove(dbSong);
                                NoteDbHandler.deleteNotesBySongId(dbSong.getId());
                                SectionDbHandler.deleteSectionsBySongId(dbSong.getId());
                                SongDbHandler.deleteSongById(dbSong.getId());
                                refreshSongsList();
                                CustomToast.makeText(MainActivity.this, R.string.main_activity_toast_song_deleted, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onSongSelected(DbSong dbSong) {
        Intent intent = new Intent(MainActivity.this, SongActivity.class);
        intent.putExtra(Constants.EXTRA_SONG_ID, dbSong.getId());
        startActivity(intent);
    }

    private void refreshSongsList() {
        refreshDisplayedSongs();
        songsListAdapter.setSongs(displayedSongs);
        songsListAdapter.notifyDataSetChanged();
        toggleNoSongsText();
    }

    private void toggleNoSongsText() {
        if (dbSongs.isEmpty()) {
            songsListView.setVisibility(View.GONE);
            noSongsText.setVisibility(View.VISIBLE);
        } else {
            songsListView.setVisibility(View.VISIBLE);
            noSongsText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {

        if (searchMode) {
            searchMode = false;
            searchField.setText("");
            SlideAnimation slideAnimation = new SlideAnimation(searchView, 200, SlideAnimation.COLLAPSE);
            searchView.startAnimation(slideAnimation);
            refreshSongsList();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(songsUpdatedReceiver);
        try {
            File exportDir = new File(getFilesDir(), "Songs");
            for (File file : exportDir.listFiles()) {
                file.delete();
            }
        } catch (NullPointerException e) {
        }
        super.onDestroy();
    }

}
