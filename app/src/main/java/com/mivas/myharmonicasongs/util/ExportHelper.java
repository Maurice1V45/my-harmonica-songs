package com.mivas.myharmonicasongs.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.google.common.base.Charsets;
import com.google.common.io.CharSink;
import com.google.common.io.Files;
import com.mivas.myharmonicasongs.MHSApplication;
import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.database.handler.NoteDbHandler;
import com.mivas.myharmonicasongs.database.handler.SectionDbHandler;
import com.mivas.myharmonicasongs.database.handler.SongDbHandler;
import com.mivas.myharmonicasongs.database.model.DbNote;
import com.mivas.myharmonicasongs.database.model.DbSection;
import com.mivas.myharmonicasongs.database.model.DbSong;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for exporting songs.
 */
public class ExportHelper {

    private static ExportHelper instance = new ExportHelper();

    private ExportHelper() {

    }

    public static ExportHelper getInstance() {
        return instance;
    }

    /**
     * Saves a temporary file to the internal storage.
     *
     * @param context
     * @param fileName
     * @param text
     * @return
     */
    private File saveTempFile(Context context, String fileName, String text) {
        File exportDir = new File(context.getFilesDir(), "Songs");
        if (!exportDir.exists()) {
            exportDir.mkdir();
        }
        File file = new File(exportDir, fileName);
        try {
            CharSink sink = Files.asCharSink(file, Charsets.UTF_8);
            sink.write(text);
        } catch (IOException e) {
            e.printStackTrace();
            CustomToast.makeText(context, R.string.settings_activity_toast_backup_error, Toast.LENGTH_SHORT).show();
        }
        return file;
    }

    /**
     * Converts a list of songs to a json.
     *
     * @param dbSongs
     * @return
     */
    public JSONObject convertSongsToJson(List<DbSong> dbSongs) {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray songsArray = new JSONArray();
            for (DbSong dbSong : dbSongs) {

                // add song
                JSONObject songJson = new JSONObject();
                songJson.put("title", dbSong.getTitle());
                songJson.put("author", dbSong.getAuthor());
                songJson.put("favorite", dbSong.isFavourite());
                songJson.put("key", dbSong.getKey());

                // add notes
                JSONArray notesArray = new JSONArray();
                List<DbNote> dbNotes = NoteDbHandler.getNotesBySongId(dbSong.getId());
                for (DbNote dbNote : dbNotes) {
                    JSONObject noteJson = new JSONObject();
                    noteJson.put("hole", dbNote.getHole());
                    noteJson.put("blow", dbNote.isBlow());
                    noteJson.put("word", dbNote.getWord());
                    noteJson.put("row", dbNote.getRow());
                    noteJson.put("column", dbNote.getColumn());
                    noteJson.put("bend", dbNote.getBend());
                    notesArray.put(noteJson);
                }
                songJson.put("notes", notesArray);

                // add sections
                JSONArray sectionsArray = new JSONArray();
                List<DbSection> dbSections = SectionDbHandler.getSectionsBySongId(dbSong.getId());
                for (DbSection dbSection : dbSections) {
                    JSONObject sectionJson = new JSONObject();
                    sectionJson.put("name", dbSection.getName());
                    sectionJson.put("row", dbSection.getRow());
                    sectionsArray.put(sectionJson);
                }
                songJson.put("sections", sectionsArray);
                songsArray.put(songJson);
                jsonObject.put("songs", songsArray);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void restoreSongs(final String songsJson) {
        Thread thread = new Thread() {

            @Override
            public void run() {

                // clear database
                ActiveAndroid.beginTransaction();
                SectionDbHandler.deleteSections();
                NoteDbHandler.deleteNotes();
                SongDbHandler.deleteSongs();

                // init list of dbSongs and songJsons
                List<DbSong> dbSongs = new ArrayList<DbSong>();
                List<JSONObject> songJsons = new ArrayList<JSONObject>();

                // start parsing the songs
                try {
                    JSONObject jsonObject = new JSONObject(songsJson);
                    JSONArray songsArray = jsonObject.getJSONArray("songs");
                    for (int i = 0; i < songsArray.length(); i++) {
                        JSONObject songJson = songsArray.getJSONObject(i);
                        songJsons.add(songJson);

                        // populate the song
                        DbSong dbSong = new DbSong();
                        dbSong.setTitle(songJson.getString("title"));
                        dbSong.setAuthor(songJson.getString("author"));
                        dbSong.setFavourite(songJson.getBoolean("favorite"));
                        dbSong.setKey(songJson.getInt("key"));
                        dbSongs.add(dbSong);

                        // add the song to the transaction
                        SongDbHandler.insertSong(dbSong);
                    }

                    // set the transaction successful
                    ActiveAndroid.setTransactionSuccessful();
                } catch (JSONException e) {
                    return;
                } finally {
                    ActiveAndroid.endTransaction();
                }

                // start parsing the notes and the sections
                try {
                    ActiveAndroid.beginTransaction();
                    for (int i = 0; i < songJsons.size(); i++) {
                        JSONObject songJson = songJsons.get(i);
                        JSONArray notesArray = songJson.getJSONArray("notes");
                        for (int j = 0; j < notesArray.length(); j++) {
                            JSONObject noteJson = notesArray.getJSONObject(j);

                            // populate the note
                            DbNote dbNote = new DbNote();
                            dbNote.setHole(noteJson.getInt("hole"));
                            dbNote.setBlow(noteJson.getBoolean("blow"));
                            dbNote.setWord(noteJson.getString("word"));
                            dbNote.setRow(noteJson.getInt("row"));
                            dbNote.setColumn(noteJson.getInt("column"));
                            dbNote.setBend((float) noteJson.getDouble("bend"));
                            dbNote.setSongId(dbSongs.get(i).getId());

                            // add the note to the transaction
                            NoteDbHandler.insertNote(dbNote);
                        }

                        // save sections
                        JSONArray sectionsArray = songJson.getJSONArray("sections");
                        for (int j = 0; j < sectionsArray.length(); j++) {
                            JSONObject sectionJson = sectionsArray.getJSONObject(j);

                            // populate the section
                            DbSection dbSection = new DbSection();
                            dbSection.setName(sectionJson.getString("name"));
                            dbSection.setRow(sectionJson.getInt("row"));
                            dbSection.setSongId(dbSongs.get(i).getId());

                            // add the section to the transaction
                            SectionDbHandler.insertSection(dbSection);
                        }
                    }

                    // set the transaction successful
                    ActiveAndroid.setTransactionSuccessful();
                } catch (JSONException e) {
                    return;
                } finally {
                    ActiveAndroid.endTransaction();
                }

                // send broadcast receiver
                Intent intent = new Intent(Constants.INTENT_SONGS_UPDATED);
                intent.putExtra(Constants.EXTRA_SONGS_UPDATED_COUNT, dbSongs.size());
                MHSApplication.getInstance().getApplicationContext().sendBroadcast(intent);
            }
        };
        thread.start();
    }

    public void launchExportIntent(Context context, List<DbSong> dbSongs, String fileName) {
        File songFile = saveTempFile(context, fileName, ExportHelper.getInstance().convertSongsToJson(dbSongs).toString());
        Uri contentUri = FileProvider.getUriForFile(context, "com.mivas.myharmonicasongs.fileprovider", songFile);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(contentUri, context.getContentResolver().getType(contentUri));
        intent.setType("file/*");
        intent.putExtra(Intent.EXTRA_STREAM, contentUri);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.settings_activity_text_export_to)));
    }

    public void saveToInternalStorage(Context context, String fileName, InputStream inputStream) throws Exception {
        File instrumentalsDir = new File(context.getFilesDir() + "/Instrumentals");
        if (!instrumentalsDir.exists()) {
            instrumentalsDir.mkdir();
        }
        File file = new File(instrumentalsDir, fileName);
        OutputStream output = new FileOutputStream(file);
        try {
            byte[] buffer = new byte[4 * 1024];
            int read;

            while ((read = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }
            output.flush();
        } finally {
            output.close();
            inputStream.close();
        }
    }

    public Uri getInstrumentalUri(Context context, DbSong dbSong) {
        File instrumentalsDir = new File(context.getFilesDir() + "/Instrumentals");
        if (!instrumentalsDir.exists()) {
            instrumentalsDir.mkdir();
        }
        File file = new File(instrumentalsDir, dbSong.getInstrumental());
        return Uri.fromFile(file);
    }

    public String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
