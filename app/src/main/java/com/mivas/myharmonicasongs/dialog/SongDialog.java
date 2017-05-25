package com.mivas.myharmonicasongs.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.database.model.DbSong;
import com.mivas.myharmonicasongs.listener.MainActivityListener;
import com.mivas.myharmonicasongs.util.CustomToast;

/**
 * Dialog for adding and editing a song.
 */
public class SongDialog extends DialogFragment {

    private EditText titleField;
    private EditText authorField;
    private Button addSongButton;
    private DbSong dbSong;
    private MainActivityListener listener;

    /**
     * Default constructor
     */
    public SongDialog() {
        // empty constructor
    }

    /**
     * Views initializer
     *
     * @param rootView
     */
    private void initViews(View rootView) {
        titleField = (EditText) rootView.findViewById(R.id.field_title);
        authorField = (EditText) rootView.findViewById(R.id.field_author);
        addSongButton = (Button) rootView.findViewById(R.id.button_ok);
        if (dbSong != null) {
            titleField.setText(dbSong.getTitle());
            authorField.setText(dbSong.getAuthor());
        }
    }

    /**
     * Listeners initializer
     */
    private void initListeners() {
        addSongButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (titleField.getText().toString().isEmpty()) {
                    CustomToast.makeText(getActivity(), R.string.error_no_title, Toast.LENGTH_SHORT).show();
                } else {
                    if (dbSong == null) {

                        // add a song
                        DbSong dbSong = new DbSong();
                        dbSong.setTitle(titleField.getText().toString());
                        dbSong.setAuthor(authorField.getText().toString());
                        listener.onSongAdded(dbSong);
                    } else {

                        // edit a song
                        dbSong.setTitle(titleField.getText().toString());
                        dbSong.setAuthor(authorField.getText().toString());
                        listener.onSongEditConfirmed(dbSong);
                    }
                    dismiss();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_song, container);
        initViews(view);
        initListeners();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // set width to match parent
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    public void setSong(DbSong dbSong) {
        this.dbSong = dbSong;
    }

    public void setListener(MainActivityListener listener) {
        this.listener = listener;
    }

}
