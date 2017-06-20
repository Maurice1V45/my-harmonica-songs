package com.mivas.myharmonicasongs.database.handler;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.mivas.myharmonicasongs.database.model.DbSection;

import java.util.List;

/**
 * Db handler for section.
 */
public class SectionDbHandler {

    /**
     * Inserts a section into db.
     *
     * @param dbSection The section to be inserted to the db.
     */
    public static void insertSection(DbSection dbSection) {
        dbSection.save();
    }

    /**
     * Inserts a list of sections into db.
     *
     * @param dbSections The list of sections to be inserted into db.
     */
    public static void insertSections(List<DbSection> dbSections) {
        for (DbSection dbSection : dbSections) {
            dbSection.save();
        }
    }

    /**
     * Retrieves all sections from the db.
     *
     * @return A list of sections.
     */
    public static List<DbSection> getSections() {
        return new Select()
                .from(DbSection.class)
                .execute();
    }

    /**
     * Retrieves sections that belong to a song.
     *
     * @param songId The song id.
     * @return A list of sections.
     */
    public static List<DbSection> getSectionsBySongId(long songId) {
        return new Select()
                .from(DbSection.class)
                .where("song_id = ?", songId)
                .execute();
    }

    /**
     * Deletes all sections from db.
     */
    public static void deleteSections() {
        new Delete()
                .from(DbSection.class)
                .execute();
    }

    /**
     * Deletes a section.
     *
     * @param dbSection The section to be deleted.
     */
    public static void deleteSection(DbSection dbSection) {
        dbSection.delete();
    }

    /**
     * Deletes all sections that belong to a song.
     *
     * @param songId The song id.
     */
    public static void deleteSectionsBySongId(long songId) {
        new Delete()
                .from(DbSection.class)
                .where("song_id = ?", songId)
                .execute();
    }

}
