package com.mivas.myharmonicasongs.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.google.common.base.Charsets;
import com.google.common.io.CharSink;
import com.google.common.io.Files;
import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.database.handler.NoteDbHandler;
import com.mivas.myharmonicasongs.database.handler.SectionDbHandler;
import com.mivas.myharmonicasongs.database.handler.SongDbHandler;
import com.mivas.myharmonicasongs.database.model.DbNote;
import com.mivas.myharmonicasongs.database.model.DbSection;
import com.mivas.myharmonicasongs.database.model.DbSong;
import com.mivas.myharmonicasongs.listener.MainActivityListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
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
            CustomToast.makeText(context, R.string.error_exporting_song, Toast.LENGTH_SHORT).show();
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

    public void saveJsonSongsToDb(final String jsonString, final MainActivityListener listener) {
        Thread thread = new Thread() {

            @Override
            public void run() {
                List<DbSong> dbSongs = new ArrayList<DbSong>();
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray songsArray = jsonObject.getJSONArray("songs");
                    for (int i = 0; i < songsArray.length(); i++) {

                        // save song
                        JSONObject songJson = songsArray.getJSONObject(i);
                        DbSong dbSong = new DbSong();
                        dbSong.setTitle(songJson.getString("title"));
                        dbSong.setAuthor(songJson.getString("author"));
                        dbSong.setFavourite(songJson.getBoolean("favorite"));
                        dbSong.setKey(songJson.getInt("key"));
                        SongDbHandler.insertSong(dbSong);
                        dbSongs.add(dbSong);

                        // save notes
                        JSONArray notesArray = songJson.getJSONArray("notes");
                        for (int j = 0; j < notesArray.length(); j++) {
                            JSONObject noteJson = notesArray.getJSONObject(j);
                            DbNote dbNote = new DbNote();
                            dbNote.setHole(noteJson.getInt("hole"));
                            dbNote.setBlow(noteJson.getBoolean("blow"));
                            dbNote.setWord(noteJson.getString("word"));
                            dbNote.setRow(noteJson.getInt("row"));
                            dbNote.setColumn(noteJson.getInt("column"));
                            dbNote.setBend((float) noteJson.getDouble("bend"));
                            dbNote.setSongId(dbSong.getId());
                            NoteDbHandler.insertNote(dbNote);
                        }

                        // save sections
                        JSONArray sectionsArray = songJson.getJSONArray("sections");
                        for (int j = 0; j < sectionsArray.length(); j++) {
                            JSONObject sectionJson = sectionsArray.getJSONObject(j);
                            DbSection dbSection = new DbSection();
                            dbSection.setName(sectionJson.getString("name"));
                            dbSection.setRow(sectionJson.getInt("row"));
                            dbSection.setSongId(dbSong.getId());
                            SectionDbHandler.insertSection(dbSection);
                        }
                    }
                    listener.onSongsImported(dbSongs);
                } catch (JSONException e) {
                    listener.onSongsImportedError();
                }
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
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.export_to)));
    }
}
