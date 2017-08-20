package com.mivas.myharmonicasongs.util;


import com.mivas.myharmonicasongs.database.model.DbNote;

public class NotesShiftUtils {

    public static boolean isIncreasePossible(DbNote dbNote) {
        int hole = dbNote.getHole();
        boolean blow = dbNote.isBlow();
        float bend = dbNote.getBend();
        if (hole == 1 && blow && bend == 0f) {
            return true;
        } else if (hole == 1 && !blow && bend == 0f) {
            return true;
        } else if (hole == 1 && !blow && bend == -0.5f) {
            return true;
        } else if (hole == 2 && blow && bend == 0f) {
            return true;
        } else if (hole == 2 && !blow && bend == 0f) {
            return true;
        } else if (hole == 2 && !blow && bend == -1f) {
            return true;
        } else if (hole == 3 && blow && bend == 0f) {
            return true;
        } else if (hole == 3 && !blow && bend == 0f) {
            return true;
        } else if (hole == 3 && !blow && bend == -1f) {
            return true;
        } else if (hole == 3 && !blow && bend == -1.5f) {
            return true;
        } else if (hole == 4 && blow && bend == 0f) {
            return true;
        } else if (hole == 4 && !blow && bend == 0f) {
            return true;
        } else if (hole == 5 && blow && bend == 0f) {
            return true;
        } else if (hole == 5 && !blow && bend == 0f) {
            return true;
        } else if (hole == 6 && blow && bend == 0f) {
            return true;
        } else if (hole == 6 && !blow && bend == 0f) {
            return true;
        } else if (hole == 7 && blow && bend == 0f) {
            return true;
        } else if (hole == 7 && !blow && bend == 0f) {
            return true;
        }
        return false;
    }

    public static void increase(DbNote dbNote) {
        int hole = dbNote.getHole();
        boolean blow = dbNote.isBlow();
        float bend = dbNote.getBend();
        if (hole == 1 && blow && bend == 0f) {
            dbNote.setHole(4);
        } else if (hole == 1 && !blow && bend == 0f) {
            dbNote.setHole(4);
        } else if (hole == 1 && !blow && bend == -0.5f) {
            dbNote.setHole(4);
        } else if (hole == 2 && blow && bend == 0f) {
            dbNote.setHole(5);
        } else if (hole == 2 && !blow && bend == 0f) {
            dbNote.setHole(6);
            dbNote.setBlow(true);
        } else if (hole == 2 && !blow && bend == -1f) {
            dbNote.setHole(5);
            dbNote.setBend(0f);
        } else if (hole == 3 && blow && bend == 0f) {
            dbNote.setHole(6);
        } else if (hole == 3 && !blow && bend == 0f) {
            dbNote.setHole(7);
        } else if (hole == 3 && !blow && bend == -1f) {
            dbNote.setHole(6);
            dbNote.setBend(0f);
        } else if (hole == 3 && !blow && bend == -1.5f) {
            dbNote.setHole(6);
            dbNote.setBend(-0.5f);
        } else if (hole == 4 && blow && bend == 0f) {
            dbNote.setHole(7);
        } else if (hole == 4 && !blow && bend == 0f) {
            dbNote.setHole(8);
        } else if (hole == 5 && blow && bend == 0f) {
            dbNote.setHole(8);
        } else if (hole == 5 && !blow && bend == 0f) {
            dbNote.setHole(9);
        } else if (hole == 6 && blow && bend == 0f) {
            dbNote.setHole(9);
        } else if (hole == 6 && !blow && bend == 0f) {
            dbNote.setHole(10);
        } else if (hole == 7 && blow && bend == 0f) {
            dbNote.setHole(10);
        } else if (hole == 7 && !blow && bend == 0f) {
            dbNote.setHole(10);
            dbNote.setBlow(true);
            dbNote.setBend(0.5f);
        }
    }

    public static boolean isDecreasePossible(DbNote dbNote) {
        int hole = dbNote.getHole();
        boolean blow = dbNote.isBlow();
        float bend = dbNote.getBend();
        if (hole == 10 && blow && bend == 0f) {
            return true;
        } else if (hole == 10 && blow && bend == 0.5f) {
            return true;
        } else if (hole == 10 && !blow && bend == 0f) {
            return true;
        } else if (hole == 9 && blow && bend == 0f) {
            return true;
        } else if (hole == 9 && !blow && bend == 0f) {
            return true;
        } else if (hole == 8 && blow && bend == 0f) {
            return true;
        } else if (hole == 8 && !blow && bend == 0f) {
            return true;
        } else if (hole == 7 && blow && bend == 0f) {
            return true;
        } else if (hole == 7 && !blow && bend == 0f) {
            return true;
        } else if (hole == 6 && blow && bend == 0f) {
            return true;
        } else if (hole == 6 && !blow && bend == 0f) {
            return true;
        } else if (hole == 6 && !blow && bend == -0.5f) {
            return true;
        } else if (hole == 5 && blow && bend == 0f) {
            return true;
        } else if (hole == 5 && !blow && bend == 0f) {
            return true;
        } else if (hole == 4 && blow && bend == 0f) {
            return true;
        } else if (hole == 4 && !blow && bend == 0f) {
            return true;
        } else if (hole == 4 && !blow && bend == -0.5f) {
            return true;
        }
        return false;
    }

    public static void decrease(DbNote dbNote) {
        int hole = dbNote.getHole();
        boolean blow = dbNote.isBlow();
        float bend = dbNote.getBend();
        if (hole == 10 && blow && bend == 0f) {
            dbNote.setHole(7);
        } else if (hole == 10 && blow && bend == 0.5f) {
            dbNote.setHole(7);
            dbNote.setBlow(false);
            dbNote.setBend(0f);
        } else if (hole == 10 && !blow && bend == 0f) {
            dbNote.setHole(6);
        } else if (hole == 9 && blow && bend == 0f) {
            dbNote.setHole(6);
        } else if (hole == 9 && !blow && bend == 0f) {
            dbNote.setHole(5);
        } else if (hole == 8 && blow && bend == 0f) {
            dbNote.setHole(5);
        } else if (hole == 8 && !blow && bend == 0f) {
            dbNote.setHole(4);
        } else if (hole == 7 && blow && bend == 0f) {
            dbNote.setHole(4);
        } else if (hole == 7 && !blow && bend == 0f) {
            dbNote.setHole(3);
        } else if (hole == 6 && blow && bend == 0f) {
            dbNote.setHole(3);
        } else if (hole == 6 && !blow && bend == 0f) {
            dbNote.setHole(3);
            dbNote.setBend(-1f);
        } else if (hole == 6 && !blow && bend == -0.5f) {
            dbNote.setHole(3);
            dbNote.setBend(-1.5f);
        } else if (hole == 5 && blow && bend == 0f) {
            dbNote.setHole(2);
        } else if (hole == 5 && !blow && bend == 0f) {
            dbNote.setHole(2);
            dbNote.setBend(-1f);
        } else if (hole == 4 && blow && bend == 0f) {
            dbNote.setHole(1);
        } else if (hole == 4 && !blow && bend == 0f) {
            dbNote.setHole(1);
        } else if (hole == 4 && !blow && bend == -0.5f) {
            dbNote.setHole(1);
        }
    }
}
