package com.mivas.myharmonicasongs.view;

import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.PopupMenu;

import com.mivas.myharmonicasongs.R;

public class NoteRowOptionsMenu extends PopupMenu {
    public NoteRowOptionsMenu(Context context, View anchor) {
        super(context, anchor);
        init(context);
    }

    public NoteRowOptionsMenu(Context context, View anchor, int gravity) {
        super(context, anchor, gravity);
        init(context);
    }

    public NoteRowOptionsMenu(Context context, View anchor, int gravity, int popupStyleAttr, int popupStyleRes) {
        super(context, anchor, gravity, popupStyleAttr, popupStyleRes);
        init(context);
    }

    private void init(Context context) {
        getMenu().add(Menu.NONE, 0, Menu.NONE, context.getString(R.string.menu_add_note));
        getMenu().add(Menu.NONE, 1, Menu.NONE, context.getString(R.string.menu_insert_row));
        getMenu().add(Menu.NONE, 2, Menu.NONE, context.getString(R.string.menu_delete_row));
        getMenu().add(Menu.NONE, 3, Menu.NONE, context.getString(R.string.menu_copy_row));
        getMenu().add(Menu.NONE, 4, Menu.NONE, context.getString(R.string.menu_paste_row));
    }
}
