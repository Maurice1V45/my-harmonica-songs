package com.mivas.myharmonicasongs.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mivas.myharmonicasongs.MHSApplication;
import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.listener.CustomizeActivityListener;
import com.mivas.myharmonicasongs.listener.SettingsActivityListener;

/**
 * Dialog with a warning before restoring songs.
 */
public class RestoreWarningDialog extends DialogFragment {

    private TextView titleText;
    private Button yesButton;
    private Button noButton;
    private SettingsActivityListener listener;

    /**
     * Default constructor
     */
    public RestoreWarningDialog() {
        // empty constructor
    }

    /**
     * Views initializer
     *
     * @param rootView
     */
    private void initViews(View rootView) {
        titleText = (TextView) rootView.findViewById(R.id.text_title);
        titleText.setText(getString(R.string.restore_warning_dialog_title));
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
                listener.onRestoreConfirmed();
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
        View view = inflater.inflate(R.layout.dialog_generic, container);
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

    public void setListener(SettingsActivityListener listener) {
        this.listener = listener;
    }

}
