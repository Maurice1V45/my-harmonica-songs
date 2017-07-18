package com.mivas.myharmonicasongs.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.database.model.DbSection;
import com.mivas.myharmonicasongs.listener.SongActivityListener;
import com.mivas.myharmonicasongs.util.CustomToast;

/**
 * Dialog for adding and editing a section.
 */
public class SectionDialog extends DialogFragment {

    private EditText sectionNameField;
    private Button okButton;
    private DbSection dbSection;
    private TextView dialogTitle;
    private boolean editMode;
    private SongActivityListener listener;

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
                        listener.onSectionAdded(dbSection);
                    } else {

                        // edit a song
                        dbSection.setName(sectionNameField.getText().toString());
                        listener.onSectionEdit(dbSection);
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

        // set width to match parent
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    public void setDbSection(DbSection dbSection) {
        this.dbSection = dbSection;
    }

    public void setListener(SongActivityListener listener) {
        this.listener = listener;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }
}
