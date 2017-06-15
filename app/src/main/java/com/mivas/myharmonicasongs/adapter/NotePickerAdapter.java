package com.mivas.myharmonicasongs.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.database.model.DbNote;
import com.mivas.myharmonicasongs.listener.NotePickerDialogListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Adapter for note picker.
 */
public class NotePickerAdapter extends RecyclerView.Adapter<NotePickerAdapter.NoteViewHolder> {

    private Context context;
    private NotePickerDialogListener listener;
    private DbNote selectedNote;
    private boolean showBendings = false;
    private static final Map<Integer, Float> BENDINGS_MAP = new HashMap<Integer, Float>();
    static {
        BENDINGS_MAP.put(1, -0.5f);
        BENDINGS_MAP.put(2, -1f);
        BENDINGS_MAP.put(3, -1.5f);
        BENDINGS_MAP.put(4, -0.5f);
        BENDINGS_MAP.put(5, 0f);
        BENDINGS_MAP.put(6, -0.5f);
        BENDINGS_MAP.put(7, 0f);
        BENDINGS_MAP.put(8, 0.5f);
        BENDINGS_MAP.put(9, 0.5f);
        BENDINGS_MAP.put(10, 1f);
    }

    public NotePickerAdapter(Context context, NotePickerDialogListener listener, DbNote selectedNote, boolean showBendings) {
        this.context = context;
        this.listener = listener;
        this.selectedNote = selectedNote;
        this.showBendings = showBendings;
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
        holder.upperNote.setText(String.format("%s", note));
        if (selectedNote != null && selectedNote.getHole() == position + 1 && selectedNote.isBlow() && selectedNote.getBend() == 0f) {
            holder.upperNote.setBackgroundResource(R.drawable.shape_harmonica_note_pressed);
            holder.upperNote.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            holder.upperNote.setBackgroundResource(R.drawable.selector_harmonica_note);
            holder.upperNote.setTextColor(ContextCompat.getColorStateList(context, R.color.selector_harmonica_note_text));
        }
        holder.lowerNote.setText(String.format("-%s", note));
        if (selectedNote != null && selectedNote.getHole() == position + 1 && !selectedNote.isBlow() && selectedNote.getBend() == 0f) {
            holder.lowerNote.setBackgroundResource(R.drawable.shape_harmonica_note_pressed);
            holder.lowerNote.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            holder.lowerNote.setBackgroundResource(R.drawable.selector_harmonica_note);
            holder.lowerNote.setTextColor(ContextCompat.getColorStateList(context, R.color.selector_harmonica_note_text));
        }
        if (showBendings) {
            holder.upperNoteBend2.setText(String.format("%s''", note));
            if (selectedNote != null && selectedNote.getHole() == position + 1 && selectedNote.isBlow() && selectedNote.getBend() == 1f) {
                holder.upperNoteBend2.setBackgroundResource(R.drawable.shape_harmonica_note_pressed);
                holder.upperNoteBend2.setTextColor(ContextCompat.getColor(context, R.color.white));
            } else {
                holder.upperNoteBend2.setBackgroundResource(R.drawable.selector_harmonica_note);
                holder.upperNoteBend2.setTextColor(ContextCompat.getColorStateList(context, R.color.selector_harmonica_note_text));
            }
            holder.upperNoteBend1.setText(String.format("%s'", note));
            if (selectedNote != null && selectedNote.getHole() == position + 1 && selectedNote.isBlow() && selectedNote.getBend() == 0.5f) {
                holder.upperNoteBend1.setBackgroundResource(R.drawable.shape_harmonica_note_pressed);
                holder.upperNoteBend1.setTextColor(ContextCompat.getColor(context, R.color.white));
            } else {
                holder.upperNoteBend1.setBackgroundResource(R.drawable.selector_harmonica_note);
                holder.upperNoteBend1.setTextColor(ContextCompat.getColorStateList(context, R.color.selector_harmonica_note_text));
            }
            holder.lowerNoteBend1.setText(String.format("-%s'", note));
            if (selectedNote != null && selectedNote.getHole() == position + 1 && !selectedNote.isBlow() && selectedNote.getBend() == -0.5f) {
                holder.lowerNoteBend1.setBackgroundResource(R.drawable.shape_harmonica_note_pressed);
                holder.lowerNoteBend1.setTextColor(ContextCompat.getColor(context, R.color.white));
            } else {
                holder.lowerNoteBend1.setBackgroundResource(R.drawable.selector_harmonica_note);
                holder.lowerNoteBend1.setTextColor(ContextCompat.getColorStateList(context, R.color.selector_harmonica_note_text));
            }
            holder.lowerNoteBend2.setText(String.format("-%s''", note));
            if (selectedNote != null && selectedNote.getHole() == position + 1 && !selectedNote.isBlow() && selectedNote.getBend() == -1f) {
                holder.lowerNoteBend2.setBackgroundResource(R.drawable.shape_harmonica_note_pressed);
                holder.lowerNoteBend2.setTextColor(ContextCompat.getColor(context, R.color.white));
            } else {
                holder.lowerNoteBend2.setBackgroundResource(R.drawable.selector_harmonica_note);
                holder.lowerNoteBend2.setTextColor(ContextCompat.getColorStateList(context, R.color.selector_harmonica_note_text));
            }
            holder.lowerNoteBend3.setText(String.format("-%s'''", note));
            if (selectedNote != null && selectedNote.getHole() == position + 1 && !selectedNote.isBlow() && selectedNote.getBend() == -1.5f) {
                holder.lowerNoteBend3.setBackgroundResource(R.drawable.shape_harmonica_note_pressed);
                holder.lowerNoteBend3.setTextColor(ContextCompat.getColor(context, R.color.white));
            } else {
                holder.lowerNoteBend3.setBackgroundResource(R.drawable.selector_harmonica_note);
                holder.lowerNoteBend3.setTextColor(ContextCompat.getColorStateList(context, R.color.selector_harmonica_note_text));
            }
        }

        // set hole listeners
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
        if (showBendings) {
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
        }

        showBendingNotes(holder, position + 1);
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

    private void showBendingNotes(NoteViewHolder holder, int hole) {
        if (showBendings) {
            float maxBend = BENDINGS_MAP.get(hole);
            holder.upperNoteBend2.setVisibility(maxBend >= 1f ? View.VISIBLE : View.INVISIBLE);
            holder.upperNoteBend1.setVisibility(maxBend >= 0.5f ? View.VISIBLE : View.INVISIBLE);
            holder.lowerNoteBend1.setVisibility(maxBend <= -0.5f ? View.VISIBLE : View.INVISIBLE);
            holder.lowerNoteBend2.setVisibility(maxBend <= -1f ? View.VISIBLE : View.INVISIBLE);
            holder.lowerNoteBend3.setVisibility(maxBend <= -1.5f ? View.VISIBLE : View.INVISIBLE);
        } else {
            holder.upperNoteBend2.setVisibility(View.GONE);
            holder.upperNoteBend1.setVisibility(View.GONE);
            holder.lowerNoteBend1.setVisibility(View.GONE);
            holder.lowerNoteBend2.setVisibility(View.GONE);
            holder.lowerNoteBend3.setVisibility(View.GONE);
        }
    }

    private void showBorders(NoteViewHolder holder, int hole) {
        holder.borderLeft.setVisibility(hole == 1 ? View.VISIBLE : View.GONE);
        holder.borderRight.setVisibility(hole == 10 ? View.VISIBLE : View.GONE);
    }

    public void setShowBendings(boolean showBendings) {
        this.showBendings = showBendings;
    }
}
