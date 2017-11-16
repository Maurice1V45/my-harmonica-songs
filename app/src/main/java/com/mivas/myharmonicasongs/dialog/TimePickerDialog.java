package com.mivas.myharmonicasongs.dialog;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.mivas.myharmonicasongs.MHSApplication;
import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.adapter.KeyPickerAdapter;
import com.mivas.myharmonicasongs.database.model.DbScrollTimer;
import com.mivas.myharmonicasongs.database.model.DbSong;
import com.mivas.myharmonicasongs.listener.MainActivityListener;
import com.mivas.myharmonicasongs.listener.SongDialogListener;
import com.mivas.myharmonicasongs.listener.TimePickerListener;
import com.mivas.myharmonicasongs.util.CustomToast;
import com.mivas.myharmonicasongs.util.KeyboardUtils;
import com.mivas.myharmonicasongs.util.TimeUtils;

public class TimePickerDialog extends DialogFragment {

    private NumberPicker minuteNumberPicker;
    private NumberPicker secondNumberPicker;
    private Button okButton;
    private TimePickerListener listener;
    private DbScrollTimer dbScrollTimer;

    /**
     * Default constructor
     */
    public TimePickerDialog() {
        // empty constructor
    }

    /**
     * Views initializer
     *
     * @param rootView
     */
    private void initViews(View rootView) {
        minuteNumberPicker = rootView.findViewById(R.id.numberpicker_minute);
        minuteNumberPicker.setMaxValue(59);
        secondNumberPicker = rootView.findViewById(R.id.numberpicker_second);
        secondNumberPicker.setMaxValue(59);
        secondNumberPicker.setFormatter(new NumberPicker.Formatter() {

            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });
        okButton = rootView.findViewById(R.id.button_ok);
        if (dbScrollTimer != null) {
            minuteNumberPicker.setValue(TimeUtils.getMinutes(dbScrollTimer.getTime()));
            secondNumberPicker.setValue(TimeUtils.getSeconds(dbScrollTimer.getTime()));
        }
    }

    /**
     * Listeners initializer
     */
    private void initListeners() {
        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onTimePicked(TimeUtils.toMillis(minuteNumberPicker.getValue(), secondNumberPicker.getValue()));
                    dismiss();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_time_picker, container);
        initViews(view);
        initListeners();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() != null) {

            // hide keyboard
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            if (!MHSApplication.getInstance().isTablet()) {

                // set width to match parent
                getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        }
    }

    public void setListener(TimePickerListener listener) {
        this.listener = listener;
    }

    public void setDbScrollTimer(DbScrollTimer dbScrollTimer) {
        this.dbScrollTimer = dbScrollTimer;
    }
}
