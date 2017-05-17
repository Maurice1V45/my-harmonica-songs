package com.mivas.myharmonicasongs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.database.model.DbSong;
import com.mivas.myharmonicasongs.listener.HarmonicaNotesDialogListener;
import com.mivas.myharmonicasongs.listener.MainActivityListener;
import com.mivas.myharmonicasongs.listener.SongActivityListener;
import com.mivas.myharmonicasongs.view.SongOptionsMenu;

import java.util.List;


public class HarmonicaNotesAdapter extends RecyclerView.Adapter<HarmonicaNotesAdapter.SongViewHolder> {

    private Context context;
    private HarmonicaNotesDialogListener listener;

    public HarmonicaNotesAdapter(Context context, HarmonicaNotesDialogListener listener) {
        this.context = context;
        this.listener = listener;
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
        holder.upperNote.setText(String.valueOf(note));
        holder.lowerNote.setText("-" + String.valueOf(note));
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
