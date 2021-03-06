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

public class Diatonic10NotePickerAdapter extends RecyclerView.Adapter<Diatonic10NotePickerAdapter.NoteViewHolder> {

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

    public Diatonic10NotePickerAdapter(Context context, NotePickerAdapterListener listener, DbNote selectedNote) {
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
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_note_picker, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NoteViewHolder holder, int position) {
        final int note = position + 1;

        // set hole text
        CustomizationUtils.styleDiatonic10NoteText(holder.upperNote, note, 0f, blowSign, blowStyle, CustomizationUtils.createNotePickerTextColor(context, blowTextColor));
        CustomizationUtils.styleDiatonic10NoteText(holder.lowerNote, note, 0f, drawSign, drawStyle, CustomizationUtils.createNotePickerTextColor(context, drawTextColor));

        // set hole background
        holder.upperNote.setBackground(CustomizationUtils.createNotePickerBackground(context, blowBackgroundColor));
        holder.lowerNote.setBackground(CustomizationUtils.createNotePickerBackground(context, drawBackgroundColor));

        if (selectedNote != null && selectedNote.getHole() == position + 1 && selectedNote.getBend() == 0f) {
            TextView selectedView = selectedNote.isBlow() ? holder.upperNote : holder.lowerNote;
            CustomizationUtils.styleDiatonic10NoteText(selectedView, note, selectedNote.getBend(), selectedNote.isBlow() ? blowSign : drawSign, selectedNote.isBlow() ? blowStyle : drawStyle, Constants.DEFAULT_COLOR_WHITE);
            selectedView.setBackground(CustomizationUtils.createSimpleBackground(context, 12, Constants.DEFAULT_COLOR_PRIMARY));
        }

        // set hole listeners
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

        showBorders(holder, position + 1);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView upperNote;
        TextView lowerNote;
        View borderLeft;
        View borderRight;

        NoteViewHolder(View itemView) {
            super(itemView);
            upperNote = itemView.findViewById(R.id.text_upper_note);
            lowerNote = itemView.findViewById(R.id.text_lower_note);
            borderLeft = itemView.findViewById(R.id.border_left);
            borderRight = itemView.findViewById(R.id.border_right);
        }
    }

    private void showBorders(NoteViewHolder holder, int hole) {
        holder.borderLeft.setVisibility(hole == 1 ? View.VISIBLE : View.GONE);
        holder.borderRight.setVisibility(hole == 10 ? View.VISIBLE : View.GONE);
    }

    public void setSelectedNote(DbNote selectedNote) {
        this.selectedNote = selectedNote;
    }
}
