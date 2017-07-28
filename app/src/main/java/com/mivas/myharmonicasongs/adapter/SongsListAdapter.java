package com.mivas.myharmonicasongs.adapter;

import android.content.Context;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.database.model.DbSong;
import com.mivas.myharmonicasongs.listener.MainActivityListener;
import com.mivas.myharmonicasongs.util.SongKeyUtils;

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
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SongViewHolder holder, int position) {
        final DbSong dbSong = songs.get(position);

        // set song key, audio file, title and author
        holder.keyText.setText(SongKeyUtils.getKey(dbSong.getKey()));
        int key = dbSong.getKey();
        if (key == 1 || key == 3 || key == 6 || key == 8 || key == 10) {
            holder.keyText.setTextSize(30);
        } else {
            holder.keyText.setTextSize(35);
        }
        holder.audioFileIcon.setVisibility(dbSong.getAudioFile() == null ? View.GONE : View.VISIBLE);
        holder.titleText.setText(dbSong.getTitle());
        if (dbSong.getAuthor().isEmpty()) {
            holder.authorText.setVisibility(View.GONE);
        } else {
            holder.authorText.setVisibility(View.VISIBLE);
            String author = "by " + dbSong.getAuthor();
            holder.authorText.setText(author);
        }

        // add song options menu
        final MenuBuilder menuBuilder = new MenuBuilder(context);
        menuBuilder.setOptionalIconsVisible(true);
        MenuInflater inflater = new MenuInflater(context);
        inflater.inflate(R.menu.menu_song_options, menuBuilder);
        final MenuPopupHelper optionsMenu = new MenuPopupHelper(context, menuBuilder, holder.moreButton);
        menuBuilder.setCallback(new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_edit_song:
                        listener.onSongEdit(dbSong);
                        break;
                    case R.id.action_delete_song:
                        listener.onSongDelete(dbSong);
                        break;
                    default:
                        break;
                }
                return false;
            }

            @Override
            public void onMenuModeChange(MenuBuilder menu) {

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

    static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        TextView authorText;
        TextView keyText;
        View moreButton;
        View songView;
        View audioFileIcon;

        SongViewHolder(View itemView) {
            super(itemView);
            titleText = (TextView) itemView.findViewById(R.id.text_title);
            authorText = (TextView) itemView.findViewById(R.id.text_author);
            keyText = (TextView) itemView.findViewById(R.id.text_key);
            moreButton = itemView.findViewById(R.id.button_more);
            songView = itemView.findViewById(R.id.view_song);
            audioFileIcon = itemView.findViewById(R.id.icon_audio_file);
        }
    }

    public void setSongs(List<DbSong> songs) {
        this.songs = songs;
    }
}
