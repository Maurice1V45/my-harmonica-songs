package com.mivas.myharmonicasongs.dialog;

import android.app.DialogFragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.database.model.DbNote;
import com.mivas.myharmonicasongs.listener.InsertNoteDialogListener;
import com.mivas.myharmonicasongs.listener.NotesShiftDialogListener;
import com.mivas.myharmonicasongs.model.Cell;
import com.mivas.myharmonicasongs.model.CellLine;
import com.mivas.myharmonicasongs.util.NotesShiftUtils;

import java.util.List;

public class InsertNoteDialog extends DialogFragment {

    private InsertNoteDialogListener listener;
    private CellLine cellLine;
    private Cell cell;
    private View leftView;
    private View rightView;
    private View topView;
    private View bottomView;

    /**
     * Default constructor
     */
    public InsertNoteDialog() {
        // empty constructor
    }

    /**
     * Views initializer
     *
     * @param rootView
     */
    private void initViews(View rootView) {
        leftView = rootView.findViewById(R.id.view_note_left);
        rightView = rootView.findViewById(R.id.view_note_right);
        topView = rootView.findViewById(R.id.view_note_top);
        bottomView = rootView.findViewById(R.id.view_note_bottom);
    }

    /**
     * Listeners initializer
     */
    private void initListeners() {
        leftView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onLeftSelected();
                dismiss();
            }
        });
        rightView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onRightSelected();
                dismiss();
            }
        });
        topView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onTopSelected();
                dismiss();
            }
        });
        bottomView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onBottomSelected();
                dismiss();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_insert_note, container);
        initViews(view);
        initListeners();
        initEligibles();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }

    public void setListener(InsertNoteDialogListener listener) {
        this.listener = listener;
    }

    public void setCellLine(CellLine cellLine) {
        this.cellLine = cellLine;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    private void initEligibles() {
        rightView.setEnabled(cellLine.getCells().indexOf(cell) < cellLine.getCells().size() - 1);
        bottomView.setEnabled(cellLine.getCells().size() > 1);
    }
}
