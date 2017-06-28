package com.mivas.myharmonicasongs.database.handler;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.mivas.myharmonicasongs.database.model.DbSong;

import java.util.List;

/**
 * Db handler for songs.
 */
public class SongDbHandler {

    /**
     * Inserts a song into db.
     *
     * @param dbSong The song to be inserted to the db.
     */
    public static void insertSong(DbSong dbSong) {
        dbSong.save();
    }

    /**
     * Inserts a list of songs into db.
     *
     * @param dbSongs The list of songs to be inserted into db.
     */
    public static void insertSongs(List<DbSong> dbSongs) {
        for (DbSong dbSong : dbSongs) {
            dbSong.save();
        }
    }

    /**
     * Retrieves all songs from the db.
     *
     * @return A list of songs.
     */
    public static List<DbSong> getSongs() {
        return new Select()
                .from(DbSong.class)
                .orderBy("title ASC")
                .execute();
    }

    /**
     * Retrieves a songs by the id.
     *
     * @param id The id.
     * @return A song.
     */
    public static DbSong getSongById(long id) {
        return new Select()
                .from(DbSong.class)
                .where("_id = ?", id)
                .executeSingle();
    }

    /**
     * Deletes all songs from db.
     */
    public static void deleteSongs() {
        new Delete()
                .from(DbSong.class)
                .execute();
    }

    /**
     * Deletes a song by the id.
     *
     * @param id The id.
     */
    public static void deleteSongById(long id) {
        new Delete()
                .from(DbSong.class)
                .where("_id = ?", id)
                .execute();
    }
}
