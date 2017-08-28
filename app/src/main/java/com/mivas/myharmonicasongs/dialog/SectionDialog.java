package com.mivas.myharmonicasongs.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mivas.myharmonicasongs.MHSApplication;
import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.database.model.DbSection;
import com.mivas.myharmonicasongs.listener.SectionDialogListener;
import com.mivas.myharmonicasongs.listener.SongActivityListener;
import com.mivas.myharmonicasongs.model.CellLine;
import com.mivas.myharmonicasongs.util.CustomToast;

/**
 * Dialog for adding and editing a section.
 */
public class SectionDialog extends DialogFragment {

    private EditText sectionNameField;
    private Button okButton;
    private DbSection dbSection;
    private TextView dialogTitle;
    private CellLine cellLine;
    private boolean editMode;
    private SectionDialogListener listener;

    /**
     * Default constructor
     */
    public SectionDialog() {
        // empty constructor
    }

    /**
     * Views initializer
     *
     * @param rootView
     */
    private void initViews(View rootView) {
        dialogTitle = (TextView) rootView.findViewById(R.id.text_title);
        sectionNameField = (EditText) rootView.findViewById(R.id.field_section_name);
        okButton = (Button) rootView.findViewById(R.id.button_ok);
        if (editMode) {
            dialogTitle.setText(R.string.section_dialog_text_edit_section);
            sectionNameField.setText(dbSection.getName());
        } else {
            dialogTitle.setText(R.string.section_dialog_text_add_section);
        }
    }

    /**
     * Listeners initializer
     */
    private void initListeners() {
        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (sectionNameField.getText().toString().isEmpty()) {
                    CustomToast.makeText(getActivity(), R.string.section_dialog_toast_no_section_name, Toast.LENGTH_SHORT).show();
                } else {
                    if (!editMode) {

                        // add a song
                        dbSection.setName(sectionNameField.getText().toString());
                        listener.onSectionAdded(dbSection, cellLine);
                    } else {

                        // edit a song
                        dbSection.setName(sectionNameField.getText().toString());
                        listener.onSectionEdit(dbSection, cellLine);
                    }
                    dismiss();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_section, container);
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

    public void setDbSection(DbSection dbSection) {
        this.dbSection = dbSection;
    }

    public void setListener(SectionDialogListener listener) {
        this.listener = listener;
    }

    public void setCellLine(CellLine cellLine) {
        this.cellLine = cellLine;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }
}
