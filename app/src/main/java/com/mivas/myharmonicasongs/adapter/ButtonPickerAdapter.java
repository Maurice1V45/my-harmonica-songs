package com.mivas.myharmonicasongs.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mivas.myharmonicasongs.R;
import com.mivas.myharmonicasongs.listener.ButtonPickerListener;
import com.mivas.myharmonicasongs.listener.SignPickerListener;
import com.mivas.myharmonicasongs.util.CustomizationUtils;
import com.mivas.myharmonicasongs.util.NoteSignUtils;

public class ButtonPickerAdapter extends RecyclerView.Adapter<ButtonPickerAdapter.ButtonViewHolder> {

    private Context context;
    private ButtonPickerListener listener;
    private int selectedButton;
    private boolean numbers16Notation;

    public ButtonPickerAdapter(Context context, ButtonPickerListener listener, int selectedButton) {
        this.context = context;
        this.listener = listener;
        this.selectedButton = selectedButton;
        numbers16Notation = CustomizationUtils.get16NumbersChromaticNotation();
    }

    @Override
    public ButtonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_button_picker, parent, false);
        return new ButtonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ButtonViewHolder holder, final int position) {

        // set text
        holder.buttonSignText.setText(String.format(NoteSignUtils.getButton(position), numbers16Notation ? "9" : "5"));

        // set background
        if (selectedButton == position) {
            holder.buttonSignText.setBackgroundResource(R.drawable.shape_harmonica_note_pressed);
            holder.buttonSignText.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            holder.buttonSignText.setBackgroundResource(R.drawable.selector_harmonica_note);
            holder.buttonSignText.setTextColor(ContextCompat.getColorStateList(context, R.color.selector_harmonica_note_text));
        }

        // set listener
        holder.buttonSignText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onButtonSelected(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    static class ButtonViewHolder extends RecyclerView.ViewHolder {
        TextView buttonSignText;

        ButtonViewHolder(View itemView) {
            super(itemView);
            buttonSignText = itemView.findViewById(R.id.text_button_sign);
        }
    }

    public void setSelectedButton(int selectedButton) {
        this.selectedButton = selectedButton;
    }
}
