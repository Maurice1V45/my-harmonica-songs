package com.mivas.myharmonicasongs.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.listener.StylePickerListener;
import com.mivas.myharmonicasongs.util.StyleUtils;

/**
 * Adapter for style picker.
 */
public class StylePickerAdapter extends RecyclerView.Adapter<StylePickerAdapter.SignViewHolder> {

    private Context context;
    private StylePickerListener listener;
    private int selectedStyle;
    private String text;
    private int listItem;

    public StylePickerAdapter(Context context, int listItem, StylePickerListener listener, int selectedStyle, String text) {
        this.context = context;
        this.listItem = listItem;
        this.listener = listener;
        this.selectedStyle = selectedStyle;
        this.text = text;
    }

    @Override
    public SignViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(listItem, parent, false);
        return new SignViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SignViewHolder holder, final int position) {

        // set text
        holder.styleText.setText(text);

        // set style
        StyleUtils.setStyle(holder.styleText, position);

        // set background
        if (selectedStyle == position) {
            holder.styleText.setBackgroundResource(R.drawable.shape_harmonica_note_pressed);
            holder.styleText.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            holder.styleText.setBackgroundResource(R.drawable.selector_harmonica_note);
            holder.styleText.setTextColor(ContextCompat.getColorStateList(context, R.color.selector_harmonica_note_text));
        }

        // set listener
        holder.styleText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onStyleSelected(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    static class SignViewHolder extends RecyclerView.ViewHolder {
        TextView styleText;

        SignViewHolder(View itemView) {
            super(itemView);
            styleText = (TextView) itemView.findViewById(R.id.text_style);
        }
    }

    public void setSelectedStyle(int selectedStyle) {
        this.selectedStyle = selectedStyle;
    }
}
