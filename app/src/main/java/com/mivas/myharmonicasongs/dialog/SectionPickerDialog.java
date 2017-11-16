package com.mivas.myharmonicasongs.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.mivas.myharmonicasongs.CustomizeNoteActivity;
import com.mivas.myharmonicasongs.MHSApplication;
import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.adapter.SectionPickerAdapter;
import com.mivas.myharmonicasongs.database.model.DbScrollTimer;
import com.mivas.myharmonicasongs.database.model.DbSection;
import com.mivas.myharmonicasongs.listener.SectionPickerAdapterListener;
import com.mivas.myharmonicasongs.listener.SectionPickerListener;
import com.mivas.myharmonicasongs.listener.TimePickerListener;
import com.mivas.myharmonicasongs.util.TimeUtils;

import java.util.List;

public class SectionPickerDialog extends DialogFragment implements SectionPickerAdapterListener {

    private List<DbSection> dbSections;
    private RecyclerView sectionsList;
    private SectionPickerAdapter sectionPickerAdapter;
    private SectionPickerListener listener;

    /**
     * Default constructor
     */
    public SectionPickerDialog() {
        // empty constructor
    }

    /**
     * Views initializer
     *
     * @param rootView
     */
    private void initViews(View rootView) {
        sectionsList = rootView.findViewById(R.id.list_sections);
        sectionsList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));
        sectionPickerAdapter = new SectionPickerAdapter(getActivity(), dbSections, SectionPickerDialog.this);
        sectionsList.setAdapter(sectionPickerAdapter);
    }

    /**
     * Listeners initializer
     */
    private void initListeners() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_section_picker,  container);
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

    public void setDbSections(List<DbSection> dbSections) {
        this.dbSections = dbSections;
    }

    public void setListener(SectionPickerListener listener) {
        this.listener = listener;
    }

    @Override
    public void onSectionSelected(DbSection dbSection) {
        listener.onSectionSelected(dbSection);
        dismiss();
    }
}
