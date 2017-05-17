package com.mivas.myharmonicasongs.view;

import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.PopupMenu;

import com.mivas.myharmonicasongs.R;

public class SongOptionsMenu extends PopupMenu {
    public SongOptionsMenu(Context context, View anchor) {
        super(context, anchor);
        init(context);
    }

    public SongOptionsMenu(Context context, View anchor, int gravity) {
        super(context, anchor, gravity);
        init(context);
    }

    public SongOptionsMenu(Context context, View anchor, int gravity, int popupStyleAttr, int popupStyleRes) {
        super(context, anchor, gravity, popupStyleAttr, popupStyleRes);
        init(context);
    }

    private void init(Context context) {
        getMenu().add(Menu.NONE, 0, Menu.NONE, context.getString(R.string.menu_edit));
        getMenu().add(Menu.NONE, 1, Menu.NONE, context.getString(R.string.menu_delete));
    }
}
