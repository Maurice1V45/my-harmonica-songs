package com.mivas.myharmonicasongs;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.mivas.myharmonicasongs.adapter.SongsListAdapter;
import com.mivas.myharmonicasongs.animation.SlideAnimation;
import com.mivas.myharmonicasongs.database.handler.NoteDbHandler;
import com.mivas.myharmonicasongs.database.handler.SectionDbHandler;
import com.mivas.myharmonicasongs.database.handler.SongDbHandler;
import com.mivas.myharmonicasongs.database.model.DbSong;
import com.mivas.myharmonicasongs.dialog.DeleteSongDialog;
import com.mivas.myharmonicasongs.dialog.ExportSongsDialog;
import com.mivas.myharmonicasongs.dialog.SongDialog;
import com.mivas.myharmonicasongs.listener.MainActivityListener;
import com.mivas.myharmonicasongs.util.Constants;
import com.mivas.myharmonicasongs.util.CustomToast;
import com.mivas.myharmonicasongs.util.DimensionUtils;
import com.mivas.myharmonicasongs.util.ExportHelper;
import com.mivas.myharmonicasongs.util.FirstRunUtils;
import com.mivas.myharmonicasongs.util.KeyboardUtils;
import com.mivas.myharmonicasongs.util.PreferencesUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Main Activity.
 */
public class MainActivity extends AppCompatActivity implements MainActivityListener {

    private RecyclerView songsListView;
    private SongsListAdapter songsListAdapter;
    private List<DbSong> dbSongs;
    private List<DbSong> displayedSongs;
    private TextView noSongsText;
    private View searchView;
    private boolean searchMode = false;
    private EditText searchField;
    private Comparator<DbSong> songsComparator = new Comparator<DbSong>() {

        @Override
        public int compare(DbSong song1, DbSong song2) {
            return song1.getTitle().compareTo(song2.getTitle());
        }
    };

    private static final int REQUEST_CODE_IMPORT_SONG = 1;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 2;

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
        if(menu instanceof MenuBuilder){
            MenuBuilder menuBuilder = (MenuBuilder) menu;
            menuBuilder.setOptionalIconsVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // handle item selection
        switch (item.getItemId()) {
            case R.id.search_song_action:
                if (!searchMode) {
                    searchMode = true;
                    searchView.setVisibility(View.VISIBLE);
                    KeyboardUtils.focusEditText(MainActivity.this, searchField);
                    SlideAnimation slideAnimation = new SlideAnimation(searchView, 200, SlideAnimation.EXPAND);
                    slideAnimation.setHeight(DimensionUtils.dpToPx(MainActivity.this, 66));
                    searchView.startAnimation(slideAnimation);
                } else {
                    searchMode = false;
                    KeyboardUtils.closeKeyboard(MainActivity.this);
                    searchField.setText("");
                    SlideAnimation slideAnimation = new SlideAnimation(searchView, 200, SlideAnimation.COLLAPSE);
                    searchView.startAnimation(slideAnimation);
                    refreshSongsList();
                }
                return true;
            case R.id.add_song_action:
                SongDialog songDialog = new SongDialog();
                songDialog.setListener(MainActivity.this);
                songDialog.show(getFragmentManager(), Constants.TAG_SONG_DIALOG);
                return true;
            case R.id.action_import_songs:
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
                } else {
                    launchImportSongsActivity();
                }
                return true;
            case R.id.action_export_songs:
                ExportSongsDialog exportSongsDialog = new ExportSongsDialog();
                exportSongsDialog.setDbSongs(dbSongs);
                exportSongsDialog.setListener(MainActivity.this);
                exportSongsDialog.show(getFragmentManager(), Constants.TAG_EXPORT_SONG_DIALOG);
                return true;
            case R.id.action_credits:
                Intent intent = new Intent(MainActivity.this, CreditsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void launchImportSongsActivity() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        startActivityForResult(intent, REQUEST_CODE_IMPORT_SONG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_IMPORT_SONG && resultCode == RESULT_OK) {
            try {
                CustomToast.makeText(MainActivity.this, R.string.importing_songs, Toast.LENGTH_SHORT).show();
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                String fileJson = CharStreams.toString(new InputStreamReader(inputStream, Charsets.UTF_8));
                ExportHelper.getInstance().saveJsonSongsToDb(fileJson, MainActivity.this);
            } catch (IOException e) {
                CustomToast.makeText(MainActivity.this, R.string.error_importing_song, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_STORAGE_PERMISSION: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    launchImportSongsActivity();
                } else {
                    CustomToast.makeText(MainActivity.this, R.string.permission_needed, Toast.LENGTH_LONG).show();
                }
                return;
            }
            default:
                break;
        }
    }

    @Override
    public void onSongAdded(DbSong dbSong) {
        dbSongs.add(dbSong);
        Collections.sort(dbSongs, songsComparator);
        SongDbHandler.insertSong(dbSong);
        refreshSongsList();
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
    public void onSongDelete(DbSong dbSong) {
        DeleteSongDialog deleteSongDialog = new DeleteSongDialog();
        deleteSongDialog.setSong(dbSong);
        deleteSongDialog.setListener(MainActivity.this);
        deleteSongDialog.show(getFragmentManager(), Constants.TAG_DELETE_SONG_DIALOG);
    }

    @Override
    public void onSongDeleteConfirmed(DbSong dbSong) {
        dbSongs.remove(dbSong);
        NoteDbHandler.deleteNotesBySongId(dbSong.getId());
        SectionDbHandler.deleteSectionsBySongId(dbSong.getId());
        SongDbHandler.deleteSongById(dbSong.getId());
        refreshSongsList();
    }

    @Override
    public void onSongSelected(DbSong dbSong) {
        Intent intent = new Intent(MainActivity.this, SongActivity.class);
        intent.putExtra(Constants.EXTRA_SONG_ID, dbSong.getId());
        startActivity(intent);
    }

    @Override
    public void onExportSongs(List<DbSong> dbSongs) {
        String fileName = (dbSongs.size() == 1) ? dbSongs.size() + " song.mhs" : dbSongs.size() + " songs.mhs";
        ExportHelper.getInstance().launchExportIntent(MainActivity.this, dbSongs, fileName);
    }

    @Override
    public void onSongsImported(final List<DbSong> dbSongs) {
        this.dbSongs.addAll(dbSongs);
        Collections.sort(dbSongs, songsComparator);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshSongsList();
                if (dbSongs.size() == 0) {
                    CustomToast.makeText(MainActivity.this, dbSongs.size() + " " + getString(R.string.no_song_imported), Toast.LENGTH_SHORT).show();
                } else if (dbSongs.size() == 1) {
                    CustomToast.makeText(MainActivity.this, dbSongs.size() + " " + getString(R.string.song_imported), Toast.LENGTH_SHORT).show();
                } else {
                    CustomToast.makeText(MainActivity.this, dbSongs.size() + " " + getString(R.string.songs_imported), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onSongsImportedError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomToast.makeText(MainActivity.this, R.string.error_importing_song, Toast.LENGTH_LONG).show();
            }
        });
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
