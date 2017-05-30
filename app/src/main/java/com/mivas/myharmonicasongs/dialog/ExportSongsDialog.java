package com.mivas.myharmonicasongs.dialog;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.adapter.ExportSongsAdapter;
import com.mivas.myharmonicasongs.database.model.DbSong;
import com.mivas.myharmonicasongs.listener.ExportSongsDialogListener;
import com.mivas.myharmonicasongs.listener.MainActivityListener;
import com.mivas.myharmonicasongs.util.CustomToast;

import java.util.List;

/**
 * Dialog for picking songs to export.
 */
public class ExportSongsDialog extends DialogFragment implements ExportSongsDialogListener {

    private MainActivityListener listener;
    private List<DbSong> dbSongs;

    /**
     * Constructor
     */
    public ExportSongsDialog() {
        // empty constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // set title
        builder.setTitle(R.string.select_songs_export);

        // set view
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_export_songs, null);
        builder.setView(view);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_export_songs);
        final ExportSongsAdapter adapter = new ExportSongsAdapter(getActivity(), dbSongs);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        // set buttons listeners
        builder.setPositiveButton(R.string.button_export, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<DbSong> selectedSongs = adapter.getSelectedSongs();
                if (selectedSongs.isEmpty()) {
                    CustomToast.makeText(getActivity(), getString(R.string.no_songs_selected_export), Toast.LENGTH_SHORT).show();
                } else {
                    listener.onExportSongs(selectedSongs);
                }
                getDialog().dismiss();
            }
        });

        builder.setNegativeButton(R.string.button_cancel, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                getDialog().dismiss();
            }
        });
        return builder.create();
    }

    public void setListener(MainActivityListener listener) {
        this.listener = listener;
    }

    public void setDbSongs(List<DbSong> dbSongs) {
        this.dbSongs = dbSongs;
    }
}
