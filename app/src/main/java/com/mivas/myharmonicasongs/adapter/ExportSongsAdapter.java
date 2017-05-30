package com.mivas.myharmonicasongs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.database.model.DbSong;
import com.mivas.myharmonicasongs.listener.MainActivityListener;
import com.mivas.myharmonicasongs.view.SongOptionsMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for picking songs to export.
 */
public class ExportSongsAdapter extends RecyclerView.Adapter<ExportSongsAdapter.SongViewHolder> {

    private List<DbSong> songs;
    private List<DbSong> selectedSongs;
    private Context context;

    public ExportSongsAdapter(Context context, List<DbSong> songs) {
        this.context = context;
        this.songs = songs;
        this.selectedSongs = new ArrayList<DbSong>();
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_export_song, parent, false);
        SongViewHolder viewHolder = new SongViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final SongViewHolder holder, int position) {
        final DbSong dbSong = songs.get(position);
        holder.checkbox.setText(dbSong.getTitle());
        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedSongs.add(dbSong);
                } else {
                    selectedSongs.remove(dbSong);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkbox;

        SongViewHolder(View itemView) {
            super(itemView);
            checkbox = (CheckBox) itemView.findViewById(R.id.checkbox_song);
        }
    }

    public List<DbSong> getSelectedSongs() {
        return selectedSongs;
    }
}
