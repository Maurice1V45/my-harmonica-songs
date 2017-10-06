package com.mivas.myharmonicasongs;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.mivas.myharmonicasongs.database.handler.SongDbHandler;
import com.mivas.myharmonicasongs.database.model.DbSong;
import com.mivas.myharmonicasongs.util.CustomToast;
import com.mivas.myharmonicasongs.util.ExportHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Settings activity.
 */
public class SettingsActivity extends AppCompatActivity {

    private View importView;
    private View backupView;
    private View restoreView;
    private View creditsView;
    private View qaView;
    private View rateAppView;
    private View feedbackView;
    private TextView versionText;
    private ProgressDialog progressDialog;

    private static final int REQUEST_CODE_RESTORE_SONGS = 1;
    private static final int REQUEST_CODE_STORAGE_PERMISSION_RESTORE = 2;
    private static final int REQUEST_CODE_IMPORT_SONG = 3;
    private static final int REQUEST_CODE_STORAGE_PERMISSION_IMPORT = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();
        initListeners();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        importView = findViewById(R.id.view_import);
        backupView = findViewById(R.id.view_backup);
        restoreView = findViewById(R.id.view_restore);
        qaView = findViewById(R.id.view_qa);
        creditsView = findViewById(R.id.view_credits);
        rateAppView = findViewById(R.id.view_rate_app);
        feedbackView = findViewById(R.id.view_feedback);
        versionText = (TextView) findViewById(R.id.text_version);
        versionText.setText(BuildConfig.VERSION_NAME);
    }

    private void initListeners() {
        importView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SettingsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION_IMPORT);
                } else {
                    launchImportActivity(REQUEST_CODE_IMPORT_SONG);
                }
            }
        });
        backupView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showProgress(getString(R.string.settings_activity_toast_preparing_backup));
                Thread thread = new Thread() {

                    @Override
                    public void run() {
                        List<DbSong> dbSongs = SongDbHandler.getSongs();
                        String fileName = "Backup" + System.currentTimeMillis() + ".mhs";
                        ExportHelper.getInstance().launchExportIntent(SettingsActivity.this, dbSongs, fileName, getString(R.string.settings_activity_text_export_to));
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                progressDialog.dismiss();
                            }
                        });
                    }
                };
                thread.start();

            }
        });
        restoreView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle(R.string.settings_activity_restore_warning_dialog_title);
                builder.setMessage(R.string.settings_activity_restore_warning_dialog_description);
                builder.setPositiveButton(R.string.button_restore, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ContextCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(SettingsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION_RESTORE);
                        } else {
                            launchImportActivity(REQUEST_CODE_RESTORE_SONGS);
                        }
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
        });
        qaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, QaActivity.class);
                startActivity(intent);
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
        feedbackView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:marius.ivas1@gmail.com"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.settings_activity_feedback_subject));
                    startActivity(Intent.createChooser(intent, getString(R.string.settings_activity_feedback_chooser)));
                } catch (ActivityNotFoundException e) {
                    CustomToast.makeText(SettingsActivity.this, R.string.settings_activity_toast_error_no_email_app, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void launchImportActivity(int reqCode) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, reqCode);
        } catch (ActivityNotFoundException e) {
            CustomToast.makeText(SettingsActivity.this, R.string.settings_activity_toast_error_no_filepicker_app, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == REQUEST_CODE_RESTORE_SONGS && resultCode == RESULT_OK) {
            try {
                if (ExportHelper.getInstance().isMhsFile(SettingsActivity.this, data.getData())) {
                    showProgress(getString(R.string.settings_activity_toast_restoring_songs));
                    Thread thread = new Thread() {

                        @Override
                        public void run() {
                            try {
                                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                                String fileJson = CharStreams.toString(new InputStreamReader(inputStream, Charsets.UTF_8));
                                ExportHelper.getInstance().removeAllAudioFiles(SettingsActivity.this);
                                ExportHelper.getInstance().importSongs(fileJson, true);
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                    }
                                });
                            } catch (Exception e) {
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        CustomToast.makeText(SettingsActivity.this, R.string.settings_activity_toast_restore_error, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    };
                    thread.start();
                } else {
                    CustomToast.makeText(SettingsActivity.this, R.string.settings_activity_toast_not_mhs_file, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                CustomToast.makeText(SettingsActivity.this, R.string.settings_activity_toast_restore_error, Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CODE_IMPORT_SONG && resultCode == RESULT_OK) {
            try {
                if (ExportHelper.getInstance().isMhsFile(SettingsActivity.this, data.getData())) {
                    showProgress(getString(R.string.settings_activity_toast_importing_song));
                    Thread thread = new Thread() {

                        @Override
                        public void run() {
                            try {
                                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                                String fileJson = CharStreams.toString(new InputStreamReader(inputStream, Charsets.UTF_8));
                                ExportHelper.getInstance().importSongs(fileJson, false);
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                    }
                                });
                            } catch (Exception e) {
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        CustomToast.makeText(SettingsActivity.this, R.string.settings_activity_toast_import_error, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    };
                    thread.start();
                } else {
                    CustomToast.makeText(SettingsActivity.this, R.string.settings_activity_toast_not_mhs_file, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                CustomToast.makeText(SettingsActivity.this, R.string.settings_activity_toast_import_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_STORAGE_PERMISSION_RESTORE: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    launchImportActivity(REQUEST_CODE_RESTORE_SONGS);
                } else {
                    CustomToast.makeText(SettingsActivity.this, R.string.settings_activity_toast_permission_needed, Toast.LENGTH_LONG).show();
                }
                return;
            }
            case REQUEST_CODE_STORAGE_PERMISSION_IMPORT: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    launchImportActivity(REQUEST_CODE_IMPORT_SONG);
                } else {
                    CustomToast.makeText(SettingsActivity.this, R.string.settings_activity_toast_permission_needed, Toast.LENGTH_LONG).show();
                }
                return;
            }
            default:
                break;
        }
    }

    private void showProgress(String message) {
        progressDialog = new ProgressDialog(SettingsActivity.this);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

}
