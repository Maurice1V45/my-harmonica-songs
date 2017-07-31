package com.mivas.myharmonicasongs.dialog;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mivas.myharmonicasongs.MHSApplication;
import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.adapter.KeyPickerAdapter;
import com.mivas.myharmonicasongs.database.model.DbSong;
import com.mivas.myharmonicasongs.listener.MainActivityListener;
import com.mivas.myharmonicasongs.listener.SongDialogListener;
import com.mivas.myharmonicasongs.util.CustomToast;
import com.mivas.myharmonicasongs.util.KeyboardUtils;

/**
 * Dialog for deleting a song.
 */
public class DeleteSongDialog extends DialogFragment {

    private TextView titleText;
    private Button yesButton;
    private Button noButton;
    private DbSong dbSong;
    private MainActivityListener listener;

    /**
     * Default constructor
     */
    public DeleteSongDialog() {
        // empty constructor
    }

    /**
     * Views initializer
     *
     * @param rootView
     */
    private void initViews(View rootView) {
        titleText = (TextView) rootView.findViewById(R.id.text_title);
        titleText.setText(getString(R.string.delete_song_dialog_message) + " " + dbSong.getTitle() + "?");
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
                listener.onSongDeleteConfirmed(dbSong);
                getDialog().dismiss();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_delete_song, container);
        initViews(view);
        initListeners();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // set width to match parent
        if (!MHSApplication.getInstance().isTablet() && getDialog() != null) {
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
