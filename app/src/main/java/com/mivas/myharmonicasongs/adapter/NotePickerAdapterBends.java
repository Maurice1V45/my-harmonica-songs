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

import java.util.HashMap;
import java.util.Map;

/**
 * Adapter for note picker.
 */
public class NotePickerAdapterBends extends RecyclerView.Adapter<NotePickerAdapterBends.NoteViewHolder> {

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
    private static final Map<Integer, Float> BENDS_MAP = new HashMap<>();

    static {
        BENDS_MAP.put(1, -0.5f);
        BENDS_MAP.put(2, -1f);
        BENDS_MAP.put(3, -1.5f);
        BENDS_MAP.put(4, -0.5f);
        BENDS_MAP.put(5, 0f);
        BENDS_MAP.put(6, -0.5f);
        BENDS_MAP.put(7, 0f);
        BENDS_MAP.put(8, 0.5f);
        BENDS_MAP.put(9, 0.5f);
        BENDS_MAP.put(10, 1f);
    }

    public NotePickerAdapterBends(Context context, NotePickerAdapterListener listener, DbNote selectedNote) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_note_picker_bends, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NoteViewHolder holder, int position) {
        final int note = position + 1;

        // set hole text
        CustomizationUtils.styleNoteText(holder.upperNoteBend2, note, 1f, blowSign, blowStyle, CustomizationUtils.createNotePickerTextColor(context, blowTextColor));
        CustomizationUtils.styleNoteText(holder.upperNoteBend1, note, 0.5f, blowSign, blowStyle, CustomizationUtils.createNotePickerTextColor(context, blowTextColor));
        CustomizationUtils.styleNoteText(holder.upperNote, note, 0f, blowSign, blowStyle, CustomizationUtils.createNotePickerTextColor(context, blowTextColor));
        CustomizationUtils.styleNoteText(holder.lowerNote, note, 0f, drawSign, drawStyle, CustomizationUtils.createNotePickerTextColor(context, drawTextColor));
        CustomizationUtils.styleNoteText(holder.lowerNoteBend1, note, -0.5f, drawSign, drawStyle, CustomizationUtils.createNotePickerTextColor(context, drawTextColor));
        CustomizationUtils.styleNoteText(holder.lowerNoteBend2, note, -1f, drawSign, drawStyle, CustomizationUtils.createNotePickerTextColor(context, drawTextColor));
        CustomizationUtils.styleNoteText(holder.lowerNoteBend3, note, -1.5f, drawSign, drawStyle, CustomizationUtils.createNotePickerTextColor(context, drawTextColor));

        // set hole background
        holder.upperNoteBend2.setBackground(CustomizationUtils.createNotePickerBackground(context, blowBackgroundColor));
        holder.upperNoteBend1.setBackground(CustomizationUtils.createNotePickerBackground(context, blowBackgroundColor));
        holder.upperNote.setBackground(CustomizationUtils.createNotePickerBackground(context, blowBackgroundColor));
        holder.lowerNote.setBackground(CustomizationUtils.createNotePickerBackground(context, drawBackgroundColor));
        holder.lowerNoteBend1.setBackground(CustomizationUtils.createNotePickerBackground(context, drawBackgroundColor));
        holder.lowerNoteBend2.setBackground(CustomizationUtils.createNotePickerBackground(context, drawBackgroundColor));
        holder.lowerNoteBend3.setBackground(CustomizationUtils.createNotePickerBackground(context, drawBackgroundColor));

        if (selectedNote != null && selectedNote.getHole() == position + 1) {
            TextView selectedView = null;
            if (selectedNote.getBend() == 0f) {
                selectedView = selectedNote.isBlow() ? holder.upperNote : holder.lowerNote;
            } else if (selectedNote.getBend() == 0.5f) {
                selectedView = holder.upperNoteBend1;
            } else if (selectedNote.getBend() == 1f) {
                selectedView = holder.upperNoteBend2;
            } else if (selectedNote.getBend() == -0.5f) {
                selectedView = holder.lowerNoteBend1;
            } else if (selectedNote.getBend() == -1f) {
                selectedView = holder.lowerNoteBend2;
            } else if (selectedNote.getBend() == -1.5f) {
                selectedView = holder.lowerNoteBend3;
            }
            if (selectedView != null) {
                CustomizationUtils.styleNoteText(selectedView, note, selectedNote.getBend(), selectedNote.isBlow() ? blowSign : drawSign, selectedNote.isBlow() ? blowStyle : drawStyle, Constants.DEFAULT_COLOR_WHITE);
                selectedView.setBackground(CustomizationUtils.createSimpleBackground(context, 12, Constants.DEFAULT_COLOR_PRIMARY));
            }
        }

        // set hole listeners
        holder.upperNoteBend2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onNoteSelected(note, true, 1f);
            }
        });
        holder.upperNoteBend1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onNoteSelected(note, true, 0.5f);
            }
        });
        holder.upperNote.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onNoteSelected(note, true, 0f);
            }
        });
        holder.lowerNote.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onNoteSelected(note, false, 0f);
            }
        });
        holder.lowerNoteBend1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onNoteSelected(note, false, -0.5f);
            }
        });
        holder.lowerNoteBend2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onNoteSelected(note, false, -1f);
            }
        });
        holder.lowerNoteBend3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onNoteSelected(note, false, -1.5f);
            }
        });

        filterBendingNotes(holder, position + 1);
        showBorders(holder, position + 1);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView upperNoteBend2;
        TextView upperNoteBend1;
        TextView upperNote;
        TextView lowerNote;
        TextView lowerNoteBend1;
        TextView lowerNoteBend2;
        TextView lowerNoteBend3;
        View borderLeft;
        View borderRight;

        NoteViewHolder(View itemView) {
            super(itemView);
            upperNoteBend2 = (TextView) itemView.findViewById(R.id.text_upper_note_bend2);
            upperNoteBend1 = (TextView) itemView.findViewById(R.id.text_upper_note_bend1);
            upperNote = (TextView) itemView.findViewById(R.id.text_upper_note);
            lowerNote = (TextView) itemView.findViewById(R.id.text_lower_note);
            lowerNoteBend1 = (TextView) itemView.findViewById(R.id.text_lower_note_bend1);
            lowerNoteBend2 = (TextView) itemView.findViewById(R.id.text_lower_note_bend2);
            lowerNoteBend3 = (TextView) itemView.findViewById(R.id.text_lower_note_bend3);
            borderLeft = itemView.findViewById(R.id.border_left);
            borderRight = itemView.findViewById(R.id.border_right);
        }
    }

    private void filterBendingNotes(NoteViewHolder holder, int hole) {
        float maxBend = BENDS_MAP.get(hole);
        holder.upperNoteBend2.setVisibility(maxBend >= 1f ? View.VISIBLE : View.INVISIBLE);
        holder.upperNoteBend1.setVisibility(maxBend >= 0.5f ? View.VISIBLE : View.INVISIBLE);
        holder.lowerNoteBend1.setVisibility(maxBend <= -0.5f ? View.VISIBLE : View.INVISIBLE);
        holder.lowerNoteBend2.setVisibility(maxBend <= -1f ? View.VISIBLE : View.INVISIBLE);
        holder.lowerNoteBend3.setVisibility(maxBend <= -1.5f ? View.VISIBLE : View.INVISIBLE);
    }

    private void showBorders(NoteViewHolder holder, int hole) {
        holder.borderLeft.setVisibility(hole == 1 ? View.VISIBLE : View.GONE);
        holder.borderRight.setVisibility(hole == 10 ? View.VISIBLE : View.GONE);
    }

    public void setSelectedNote(DbNote selectedNote) {
        this.selectedNote = selectedNote;
    }
}
