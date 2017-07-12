package com.mivas.myharmonicasongs.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.listener.SongDialogListener;
import com.mivas.myharmonicasongs.util.SongKeyUtils;

/**
 * Adapter for note picker.
 */
public class KeyPickerAdapter extends RecyclerView.Adapter<KeyPickerAdapter.KeyViewHolder> {

    private Context context;
    private SongDialogListener listener;
    private int selectedKey;

    public KeyPickerAdapter(Context context, SongDialogListener listener, int selectedKey) {
        this.context = context;
        this.listener = listener;
        this.selectedKey = selectedKey;
    }

    @Override
    public KeyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_key_picker, parent, false);
        return new KeyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final KeyViewHolder holder, final int position) {

        // set text
        holder.keyText.setText(SongKeyUtils.getKey(position));

        // set background
        if (selectedKey == position) {
            holder.keyText.setBackgroundResource(R.drawable.shape_harmonica_note_pressed);
            holder.keyText.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            holder.keyText.setBackgroundResource(R.drawable.selector_harmonica_note);
            holder.keyText.setTextColor(ContextCompat.getColorStateList(context, R.color.selector_harmonica_note_text));
        }

        // set listener
        holder.keyText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onKeySelected(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 12;
    }

    static class KeyViewHolder extends RecyclerView.ViewHolder {
        TextView keyText;

        KeyViewHolder(View itemView) {
            super(itemView);
            keyText = (TextView) itemView.findViewById(R.id.text_key);
        }
    }

    public void setSelectedKey(int selectedKey) {
        this.selectedKey = selectedKey;
    }
}
