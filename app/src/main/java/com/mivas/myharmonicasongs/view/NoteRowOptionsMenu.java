package com.mivas.myharmonicasongs.view;

import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.PopupMenu;

import com.mivas.myharmonicasongs.R;

public class NoteRowOptionsMenu extends PopupMenu {
    public NoteRowOptionsMenu(Context context, View anchor, int notesOnRow, int copiedNotes, boolean insertRow) {
        super(context, anchor);
        init(context, notesOnRow, copiedNotes, insertRow);
    }

    public NoteRowOptionsMenu(Context context, View anchor, int gravity, int notesOnRow, int copiedNotes, boolean insertRow) {
        super(context, anchor, gravity);
        init(context, notesOnRow, copiedNotes, insertRow);
    }

    public NoteRowOptionsMenu(Context context, View anchor, int gravity, int popupStyleAttr, int popupStyleRes, int notesOnRow, int copiedNotes, boolean insertRow) {
        super(context, anchor, gravity, popupStyleAttr, popupStyleRes);
        init(context, notesOnRow, copiedNotes, insertRow);
    }

    private void init(Context context, int notesOnRow, int copiedNotes, boolean insertRow) {
        getMenu().add(Menu.NONE, 0, Menu.NONE, context.getString(R.string.menu_add_note));
        if (insertRow) {
            getMenu().add(Menu.NONE, 1, Menu.NONE, context.getString(R.string.menu_insert_row));
        }
        if (notesOnRow != 0) {
            getMenu().add(Menu.NONE, 2, Menu.NONE, context.getString(R.string.menu_delete_row));
            getMenu().add(Menu.NONE, 3, Menu.NONE, context.getString(R.string.menu_copy_row));
        }
        if (copiedNotes != 0) {
            getMenu().add(Menu.NONE, 4, Menu.NONE, context.getString(R.string.menu_paste_row));
        }
    }
}
