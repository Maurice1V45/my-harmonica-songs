package com.mivas.myharmonicasongs.dialog;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.database.model.DbSong;
import com.mivas.myharmonicasongs.listener.MainActivityListener;


public class DeleteSongDialog extends DialogFragment {

    private MainActivityListener listener;
    private DbSong dbSong;


    /**
     * Constructor
     */
    public DeleteSongDialog() {
        // empty constructor
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // set title
        builder.setTitle(getString(R.string.delete_song_message) + " " + dbSong.getTitle() + "?");
        builder.setPositiveButton(R.string.button_yes, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onSongDeleteConfirmed(dbSong);
                getDialog().dismiss();
            }
        });

            builder.setNegativeButton(R.string.button_no, new OnClickListener() {

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

    public void setSong(DbSong dbSong) {
        this.dbSong = dbSong;
    }

}
