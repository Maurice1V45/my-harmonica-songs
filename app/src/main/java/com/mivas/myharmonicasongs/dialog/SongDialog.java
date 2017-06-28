package com.mivas.myharmonicasongs.dialog;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.database.model.DbSong;
import com.mivas.myharmonicasongs.listener.MainActivityListener;
import com.mivas.myharmonicasongs.util.CustomToast;
import com.mivas.myharmonicasongs.util.KeyboardUtils;

/**
 * Dialog for adding and editing a song.
 */
public class SongDialog extends DialogFragment {

    private EditText titleField;
    private EditText authorField;
    private Button okButton;
    private DbSong dbSong;
    private TextView dialogTitle;
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
        dialogTitle = (TextView) rootView.findViewById(R.id.text_title);
        titleField = (EditText) rootView.findViewById(R.id.field_title);
        authorField = (EditText) rootView.findViewById(R.id.field_author);
        okButton = (Button) rootView.findViewById(R.id.button_ok);
        if (dbSong != null) {
            dialogTitle.setText(R.string.dialog_edit_song);
            titleField.setText(dbSong.getTitle());
            authorField.setText(dbSong.getAuthor());
        } else {
            dialogTitle.setText(R.string.dialog_add_song);
        }
    }

    /**
     * Listeners initializer
     */
    private void initListeners() {
        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (titleField.getText().toString().isEmpty()) {
                    CustomToast.makeText(getActivity(), R.string.error_no_title, Toast.LENGTH_SHORT).show();
                } else {
                    if (dbSong == null) {

                        // add a song
                        dbSong = new DbSong();
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

    @Override
    public void onDismiss(DialogInterface dialog) {
        KeyboardUtils.closeKeyboard(getActivity());
        super.onDismiss(dialog);
    }
}
