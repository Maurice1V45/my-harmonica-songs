package com.mivas.myharmonicasongs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.database.handler.SectionDbHandler;
import com.mivas.myharmonicasongs.database.model.DbScrollTimer;
import com.mivas.myharmonicasongs.listener.ScrollTimersAdapterListener;
import com.mivas.myharmonicasongs.listener.SectionPickerAdapterListener;
import com.mivas.myharmonicasongs.util.TimeUtils;

import java.util.List;

public class ScrollTimersAdapter extends RecyclerView.Adapter<ScrollTimersAdapter.ScrollTimerViewHolder> {

    private Context context;
    private ScrollTimersAdapterListener listener;
    private List<DbScrollTimer> dbScrollTimers;

    public ScrollTimersAdapter(Context context, List<DbScrollTimer> dbScrollTimers, ScrollTimersAdapterListener listener) {
        this.context = context;
        this.dbScrollTimers = dbScrollTimers;
        this.listener = listener;
    }

    @Override
    public ScrollTimerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_scroll_timer, parent, false);
        return new ScrollTimerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ScrollTimerViewHolder holder, final int position) {

        // set text
        holder.timeText.setText(TimeUtils.toDisplayTime(dbScrollTimers.get(position).getTime()));
        holder.sectionText.setText(SectionDbHandler.getSectionById(dbScrollTimers.get(position).getSectionId()).getName());
        String lineText = "L" + (dbScrollTimers.get(position).getSectionLine() + 1);
        holder.sectionLineText.setText(lineText);

        // set listeners
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onScrollTimerDeleted(dbScrollTimers.get(position));
            }
        });
        holder.timeText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onTimeSelected(dbScrollTimers.get(position));
            }
        });
        holder.sectionText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onSectionSelected(dbScrollTimers.get(position));
            }
        });
        holder.sectionLineText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onSectionLineSelected(dbScrollTimers.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dbScrollTimers.size();
    }

    static class ScrollTimerViewHolder extends RecyclerView.ViewHolder {
        TextView timeText;
        TextView sectionText;
        TextView sectionLineText;
        View deleteButton;

        ScrollTimerViewHolder(View itemView) {
            super(itemView);
            timeText = itemView.findViewById(R.id.text_time);
            sectionText = itemView.findViewById(R.id.text_section);
            sectionLineText = itemView.findViewById(R.id.text_section_line);
            deleteButton = itemView.findViewById(R.id.button_delete);
        }
    }

}
