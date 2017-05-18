package com.mivas.myharmonicasongs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.mivas.myharmonicasongs.adapter.SongsListAdapter;
import com.mivas.myharmonicasongs.database.handler.NoteDbHandler;
import com.mivas.myharmonicasongs.database.handler.SongDbHandler;
import com.mivas.myharmonicasongs.database.model.DbSong;
import com.mivas.myharmonicasongs.dialog.DeleteSongDialog;
import com.mivas.myharmonicasongs.dialog.SongDialog;
import com.mivas.myharmonicasongs.listener.MainActivityListener;
import com.mivas.myharmonicasongs.util.Constants;


public class MainActivity extends AppCompatActivity implements MainActivityListener {

    private RecyclerView songsList;
    private SongsListAdapter songsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        songsListAdapter = new SongsListAdapter(MainActivity.this, SongDbHandler.getSongs(), MainActivity.this);
        songsList.setAdapter(songsListAdapter);
    }

    private void initViews() {
        songsList = (RecyclerView) findViewById(R.id.list_songs);
        songsList.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayout.VERTICAL, false));
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
            case R.id.add_song_action:
                SongDialog songDialog = new SongDialog();
                songDialog.setListener(MainActivity.this);
                songDialog.show(getFragmentManager(), Constants.TAG_SONG_DIALOG);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSongAdded(DbSong dbSong) {
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
        NoteDbHandler.deleteNotesBySongId(dbSong.getId());
        SongDbHandler.deleteSongById(dbSong.getId());
        refreshSongsList();
    }

    @Override
    public void onSongSelected(DbSong dbSong) {
        Intent intent = new Intent(MainActivity.this, SongActivity.class);
        intent.putExtra(Constants.EXTRA_SONG_ID, dbSong.getId());
        startActivity(intent);
    }

    private void refreshSongsList() {
        songsListAdapter.setSongs(SongDbHandler.getSongs());
        songsListAdapter.notifyDataSetChanged();
    }

}
