package com.mivas.myharmonicasongs.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.database.model.DbSection;
import com.mivas.myharmonicasongs.listener.CustomizeNoteActivityListener;
import com.mivas.myharmonicasongs.listener.SectionPickerAdapterListener;
import com.mivas.myharmonicasongs.util.NoteSignUtils;

import java.util.List;

public class SectionPickerAdapter extends RecyclerView.Adapter<SectionPickerAdapter.SectionViewHolder> {

    private Context context;
    private SectionPickerAdapterListener listener;
    private List<DbSection> dbSections;

    public SectionPickerAdapter(Context context, List<DbSection> dbSections, SectionPickerAdapterListener listener) {
        this.context = context;
        this.dbSections = dbSections;
        this.listener = listener;
    }

    @Override
    public SectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_section_picker, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SectionViewHolder holder, final int position) {

        // set text
        holder.sectionText.setText(dbSections.get(position).getName());

        // set listener
        holder.parentView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onSectionSelected(dbSections.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dbSections.size();
    }

    static class SectionViewHolder extends RecyclerView.ViewHolder {
        TextView sectionText;
        View parentView;

        SectionViewHolder(View itemView) {
            super(itemView);
            sectionText = itemView.findViewById(R.id.text_section);
            parentView = itemView.findViewById(R.id.view_parent);
        }
    }

}
