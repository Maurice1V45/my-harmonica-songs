package com.mivas.myharmonicasongs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.database.model.DbNote;
import com.mivas.myharmonicasongs.listener.NotePickerDialogListener;


public class HarmonicaNotesAdapter extends RecyclerView.Adapter<HarmonicaNotesAdapter.SongViewHolder> {

    private Context context;
    private NotePickerDialogListener listener;
    private DbNote selectedNote;

    public HarmonicaNotesAdapter(Context context, NotePickerDialogListener listener, DbNote selectedNote) {
        this.context = context;
        this.listener = listener;
        this.selectedNote = selectedNote;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_harmonica_note, parent, false);
        SongViewHolder viewHolder = new SongViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final SongViewHolder holder, int position) {
        final int note = position + 1;

        // set hole text
        holder.upperNote.setText(String.valueOf(note));
        if (selectedNote != null && selectedNote.getHole() == position + 1 && selectedNote.isBlow()) {
            holder.upperNote.setBackgroundResource(R.drawable.shape_harmonica_note_pressed);
        }
        holder.lowerNote.setText("-" + String.valueOf(note));
        if (selectedNote != null && selectedNote.getHole() == position + 1 && !selectedNote.isBlow()) {
            holder.lowerNote.setBackgroundResource(R.drawable.shape_harmonica_note_pressed);
        }

        // set hole listeners
        holder.upperNote.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onNoteSelected(note, true);
            }
        });
        holder.lowerNote.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onNoteSelected(note, false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView upperNote;
        TextView lowerNote;

        SongViewHolder(View itemView) {
            super(itemView);
            upperNote = (TextView) itemView.findViewById(R.id.text_upper_note);
            lowerNote = (TextView) itemView.findViewById(R.id.text_lower_note);
        }
    }

}
