package com.mivas.myharmonicasongs.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.listener.SignPickerListener;
import com.mivas.myharmonicasongs.util.NoteSignUtils;

public class SignPickerAdapter extends RecyclerView.Adapter<SignPickerAdapter.SignViewHolder> {

    private Context context;
    private SignPickerListener listener;
    private int selectedSign;
    private String note;

    public SignPickerAdapter(Context context, SignPickerListener listener, String note, int selectedSign) {
        this.context = context;
        this.listener = listener;
        this.note = note;
        this.selectedSign = selectedSign;
    }

    @Override
    public SignViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_sign_picker, parent, false);
        return new SignViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SignViewHolder holder, final int position) {

        // set text
        holder.signText.setText(String.format(NoteSignUtils.getFormat(position), note));

        // set background
        if (selectedSign == position) {
            holder.signText.setBackgroundResource(R.drawable.shape_harmonica_note_pressed);
            holder.signText.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            holder.signText.setBackgroundResource(R.drawable.selector_harmonica_note);
            holder.signText.setTextColor(ContextCompat.getColorStateList(context, R.color.selector_harmonica_note_text));
        }

        // set listener
        holder.signText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onSignSelected(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    static class SignViewHolder extends RecyclerView.ViewHolder {
        TextView signText;

        SignViewHolder(View itemView) {
            super(itemView);
            signText = itemView.findViewById(R.id.text_sign);
        }
    }

    public void setSelectedSign(int selectedSign) {
        this.selectedSign = selectedSign;
    }
}
