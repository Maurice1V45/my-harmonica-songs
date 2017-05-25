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
import com.mivas.myharmonicasongs.listener.MainActivityListener;
import com.mivas.myharmonicasongs.view.SongOptionsMenu;

import java.util.List;

/**
 * Adapter for songs list.
 */
public class SongsListAdapter extends RecyclerView.Adapter<SongsListAdapter.SongViewHolder> {

    private List<DbSong> songs;
    private Context context;
    private MainActivityListener listener;

    public SongsListAdapter(Context context, List<DbSong> songs, MainActivityListener listener) {
        this.context = context;
        this.songs = songs;
        this.listener = listener;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_song, parent, false);
        SongViewHolder viewHolder = new SongViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final SongViewHolder holder, int position) {
        final DbSong dbSong = songs.get(position);

        // set song title and author
        holder.titleText.setText(dbSong.getTitle());
        if (dbSong.getAuthor().isEmpty()) {
            holder.authorText.setVisibility(View.GONE);
        } else {
            holder.authorText.setVisibility(View.VISIBLE);
            holder.authorText.setText("by " + dbSong.getAuthor());
        }

        // add song options menu
        final SongOptionsMenu optionsMenu = new SongOptionsMenu(context, holder.moreButton);
        optionsMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 0:
                        listener.onSongEdit(dbSong);
                        break;
                    case 1:
                        listener.onSongDelete(dbSong);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        holder.moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionsMenu.show();
            }
        });

        // add click listener
        holder.songView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onSongSelected(dbSong);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        TextView authorText;
        View moreButton;
        View songView;

        SongViewHolder(View itemView) {
            super(itemView);
            titleText = (TextView) itemView.findViewById(R.id.text_title);
            authorText = (TextView) itemView.findViewById(R.id.text_author);
            moreButton = itemView.findViewById(R.id.button_more);
            songView = itemView.findViewById(R.id.view_song);
        }
    }

    public void setSongs(List<DbSong> songs) {
        this.songs = songs;
    }
}
