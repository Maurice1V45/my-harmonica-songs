package com.mivas.myharmonicasongs.database.handler;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.mivas.myharmonicasongs.database.model.DbScrollTimer;

import java.util.List;

/**
 * Db handler for scroll timer.
 */
public class ScrollTimerDbHandler {

    public static void insertScrollTimers(List<DbScrollTimer> dbScrollTimers) {
        for (DbScrollTimer dbScrollTimer : dbScrollTimers) {
            dbScrollTimer.save();
        }
    }

    public static List<DbScrollTimer> getScrollTimers() {
        return new Select()
                .from(DbScrollTimer.class)
                .execute();
    }

    public static List<DbScrollTimer> getScrollTimersBySongId(long songId) {
        return new Select()
                .from(DbScrollTimer.class)
                .where("song_id = ?", songId)
                .orderBy("time asc")
                .execute();
    }

    public static void deleteScrollTimers() {
        new Delete()
                .from(DbScrollTimer.class)
                .execute();
    }

    public static void deleteScrollTimer(DbScrollTimer dbScrollTimer) {
        dbScrollTimer.delete();
    }

    public static void deleteScrollTimersBySongId(long songId) {
        new Delete()
                .from(DbScrollTimer.class)
                .where("song_id = ?", songId)
                .execute();
    }

    public static void deleteScrollTimerBySectionId(long sectionId) {
        new Delete()
                .from(DbScrollTimer.class)
                .where("section_id = ?", sectionId)
                .execute();
    }

}
