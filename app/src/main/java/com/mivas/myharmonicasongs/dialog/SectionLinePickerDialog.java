package com.mivas.myharmonicasongs.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;

import com.mivas.myharmonicasongs.MHSApplication;
import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.database.model.DbScrollTimer;
import com.mivas.myharmonicasongs.listener.SectionLinePickerListener;
import com.mivas.myharmonicasongs.listener.TimePickerListener;
import com.mivas.myharmonicasongs.util.TimeUtils;

public class SectionLinePickerDialog extends DialogFragment {

    private NumberPicker numberPicker;
    private Button okButton;
    private SectionLinePickerListener listener;
    private DbScrollTimer dbScrollTimer;

    /**
     * Default constructor
     */
    public SectionLinePickerDialog() {
        // empty constructor
    }

    /**
     * Views initializer
     *
     * @param rootView
     */
    private void initViews(View rootView) {
        numberPicker = rootView.findViewById(R.id.numberpicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(99);
        okButton = rootView.findViewById(R.id.button_ok);
        if (dbScrollTimer != null) {
            numberPicker.setValue(dbScrollTimer.getSectionLine() + 1);
        }
    }

    /**
     * Listeners initializer
     */
    private void initListeners() {
        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onSectionLinePicked(numberPicker.getValue() - 1);
                    dismiss();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_section_line_picker, container);
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

    public void setListener(SectionLinePickerListener listener) {
        this.listener = listener;
    }

    public void setDbScrollTimer(DbScrollTimer dbScrollTimer) {
        this.dbScrollTimer = dbScrollTimer;
    }
}
