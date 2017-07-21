package com.mivas.myharmonicasongs;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.mivas.myharmonicasongs.database.handler.SongDbHandler;
import com.mivas.myharmonicasongs.database.model.DbSong;
import com.mivas.myharmonicasongs.dialog.RestoreWarningDialog;
import com.mivas.myharmonicasongs.listener.SettingsActivityListener;
import com.mivas.myharmonicasongs.util.Constants;
import com.mivas.myharmonicasongs.util.CustomToast;
import com.mivas.myharmonicasongs.util.ExportHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Settings activity.
 */
public class SettingsActivity extends AppCompatActivity implements SettingsActivityListener {

    private View backupView;
    private View restoreView;
    private View creditsView;
    private View rateAppView;

    private static final int REQUEST_CODE_RESTORE_SONGS = 1;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();
        initListeners();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        backupView = findViewById(R.id.view_backup);
        restoreView = findViewById(R.id.view_restore);
        creditsView = findViewById(R.id.view_credits);
        rateAppView = findViewById(R.id.view_rate_app);
    }

    private void initListeners() {
        backupView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                List<DbSong> dbSongs = SongDbHandler.getSongs();
                String fileName = (dbSongs.size() == 1) ? dbSongs.size() + " song.mhs" : dbSongs.size() + " songs.mhs";
                ExportHelper.getInstance().launchExportIntent(SettingsActivity.this, dbSongs, fileName);
            }
        });
        restoreView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RestoreWarningDialog dialog = new RestoreWarningDialog();
                dialog.setListener(SettingsActivity.this);
                dialog.show(getFragmentManager(), Constants.TAG_RESTORE_WARNING_DIALOG);

            }
        });
        creditsView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, CreditsActivity.class);
                startActivity(intent);
            }
        });
        rateAppView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                } catch (android.content.ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });
    }

    private void launchRestoreSongsActivity() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        startActivityForResult(intent, REQUEST_CODE_RESTORE_SONGS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_RESTORE_SONGS && resultCode == RESULT_OK) {
            try {
                CustomToast.makeText(SettingsActivity.this, R.string.settings_activity_toast_importing_songs, Toast.LENGTH_SHORT).show();
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                String fileJson = CharStreams.toString(new InputStreamReader(inputStream, Charsets.UTF_8));
                ExportHelper.getInstance().restoreSongs(fileJson);
            } catch (IOException e) {
                CustomToast.makeText(SettingsActivity.this, R.string.settings_activity_toast_restore_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_STORAGE_PERMISSION: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    launchRestoreSongsActivity();
                } else {
                    CustomToast.makeText(SettingsActivity.this, R.string.settings_activity_toast_permission_needed, Toast.LENGTH_LONG).show();
                }
                return;
            }
            default:
                break;
        }
    }

    @Override
    public void onRestoreConfirmed() {
        if (ContextCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SettingsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
        } else {
            launchRestoreSongsActivity();
        }
    }
}
