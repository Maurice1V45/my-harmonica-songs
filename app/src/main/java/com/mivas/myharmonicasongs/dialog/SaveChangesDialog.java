package com.mivas.myharmonicasongs.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.database.model.DbSong;
import com.mivas.myharmonicasongs.listener.MainActivityListener;
import com.mivas.myharmonicasongs.listener.SongActivityListener;

/**
 * Dialog for saving changes to a song.
 */
public class SaveChangesDialog extends DialogFragment {

    private TextView titleText;
    private Button yesButton;
    private Button noButton;
    private DbSong dbSong;
    private SongActivityListener listener;

    /**
     * Default constructor
     */
    public SaveChangesDialog() {
        // empty constructor
    }

    /**
     * Views initializer
     *
     * @param rootView
     */
    private void initViews(View rootView) {
        titleText = (TextView) rootView.findViewById(R.id.text_title);
        yesButton = (Button) rootView.findViewById(R.id.button_yes);
        noButton = (Button) rootView.findViewById(R.id.button_no);
    }

    /**
     * Listeners initializer
     */
    private void initListeners() {
        yesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onSaveChangesSelected();
                getDialog().dismiss();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onNotSaveChangesSelected();
                getDialog().dismiss();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_save_changes, container);
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

    public void setListener(SongActivityListener listener) {
        this.listener = listener;
    }

}
