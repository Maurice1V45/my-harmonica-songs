package com.mivas.myharmonicasongs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.database.model.DbNote;
import com.mivas.myharmonicasongs.listener.NotePickerAdapterListener;
import com.mivas.myharmonicasongs.util.Constants;
import com.mivas.myharmonicasongs.util.CustomizationUtils;

public class Chromatic16NotePickerAdapterBends extends RecyclerView.Adapter<Chromatic16NotePickerAdapterBends.NoteViewHolder> {

    private Context context;
    private NotePickerAdapterListener listener;
    private DbNote selectedNote;
    private int blowSign;
    private int blowStyle;
    private int blowTextColor;
    private int blowBackgroundColor;
    private int drawSign;
    private int drawStyle;
    private int drawTextColor;
    private int drawBackgroundColor;
    private int buttonStyle;
    private boolean numbers16Notation;

    public Chromatic16NotePickerAdapterBends(Context context, NotePickerAdapterListener listener, DbNote selectedNote) {
        this.context = context;
        this.listener = listener;
        this.selectedNote = selectedNote;
        blowSign = CustomizationUtils.getBlowSign();
        blowStyle = CustomizationUtils.getBlowStyle();
        blowTextColor = CustomizationUtils.getBlowTextColor();
        blowBackgroundColor = CustomizationUtils.getBlowBackgroundColor();
        drawSign = CustomizationUtils.getDrawSign();
        drawStyle = CustomizationUtils.getDrawStyle();
        drawTextColor = CustomizationUtils.getDrawTextColor();
        drawBackgroundColor = CustomizationUtils.getDrawBackgroundColor();
        buttonStyle = CustomizationUtils.getButtonStyle();
        numbers16Notation = CustomizationUtils.get16NumbersChromaticNotation();
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_note_picker_slides, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NoteViewHolder holder, int position) {
        final int note = position + 1;

        // set hole text
        CustomizationUtils.styleChromatic16NoteText(holder.upperNoteButton, note, 0.5f, blowSign, blowStyle, buttonStyle, numbers16Notation, CustomizationUtils.createNotePickerTextColor(context, blowTextColor));
        CustomizationUtils.styleChromatic16NoteText(holder.upperNote, note, 0f, blowSign, blowStyle, buttonStyle, numbers16Notation,CustomizationUtils.createNotePickerTextColor(context, blowTextColor));
        CustomizationUtils.styleChromatic16NoteText(holder.lowerNote, note, 0f, drawSign, drawStyle, buttonStyle, numbers16Notation,CustomizationUtils.createNotePickerTextColor(context, drawTextColor));
        CustomizationUtils.styleChromatic16NoteText(holder.lowerNoteButton, note, -0.5f, drawSign, drawStyle, buttonStyle, numbers16Notation,CustomizationUtils.createNotePickerTextColor(context, drawTextColor));

        // set hole background
        holder.upperNoteButton.setBackground(CustomizationUtils.createNotePickerBackground(context, blowBackgroundColor));
        holder.upperNote.setBackground(CustomizationUtils.createNotePickerBackground(context, blowBackgroundColor));
        holder.lowerNote.setBackground(CustomizationUtils.createNotePickerBackground(context, drawBackgroundColor));
        holder.lowerNoteButton.setBackground(CustomizationUtils.createNotePickerBackground(context, drawBackgroundColor));

        if (selectedNote != null && selectedNote.getHole() == position + 1) {
            TextView selectedView = null;
            if (selectedNote.getBend() == 0f) {
                selectedView = selectedNote.isBlow() ? holder.upperNote : holder.lowerNote;
            } else if (selectedNote.getBend() == 0.5f) {
                selectedView = holder.upperNoteButton;
            } else if (selectedNote.getBend() == -0.5f) {
                selectedView = holder.lowerNoteButton;
            }
            if (selectedView != null) {
                CustomizationUtils.styleChromatic16NoteText(selectedView, note, selectedNote.getBend(), selectedNote.isBlow() ? blowSign : drawSign, selectedNote.isBlow() ? blowStyle : drawStyle, buttonStyle, numbers16Notation, Constants.DEFAULT_COLOR_WHITE);
                selectedView.setBackground(CustomizationUtils.createSimpleBackground(context, 12, Constants.DEFAULT_COLOR_PRIMARY));
            }
        }

        // set hole listeners
        holder.upperNoteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onNoteSelected(note, true, 0.5f, false);
            }
        });
        holder.upperNote.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onNoteSelected(note, true, 0f, false);
            }
        });
        holder.lowerNote.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onNoteSelected(note, false, 0f, false);
            }
        });
        holder.lowerNoteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onNoteSelected(note, false, -0.5f, false);
            }
        });

        showBorders(holder, position + 1);
    }

    @Override
    public int getItemCount() {
        return 16;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView upperNoteButton;
        TextView upperNote;
        TextView lowerNote;
        TextView lowerNoteButton;
        View borderLeft;
        View borderRight;

        NoteViewHolder(View itemView) {
            super(itemView);
            upperNoteButton = itemView.findViewById(R.id.text_upper_note_button);
            upperNote = itemView.findViewById(R.id.text_upper_note);
            lowerNote = itemView.findViewById(R.id.text_lower_note);
            lowerNoteButton = itemView.findViewById(R.id.text_lower_note_button);
            borderLeft = itemView.findViewById(R.id.border_left);
            borderRight = itemView.findViewById(R.id.border_right);
        }
    }

    private void showBorders(NoteViewHolder holder, int hole) {
        holder.borderLeft.setVisibility(hole == 1 ? View.VISIBLE : View.GONE);
        holder.borderRight.setVisibility(hole == 16 ? View.VISIBLE : View.GONE);
    }

    public void setSelectedNote(DbNote selectedNote) {
        this.selectedNote = selectedNote;
    }

}
