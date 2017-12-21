package com.mivas.myharmonicasongs;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.mivas.myharmonicasongs.adapter.ButtonPickerAdapter;
import com.mivas.myharmonicasongs.adapter.SignPickerAdapter;
import com.mivas.myharmonicasongs.adapter.StylePickerAdapter;
import com.mivas.myharmonicasongs.listener.ButtonPickerListener;
import com.mivas.myharmonicasongs.listener.SignPickerListener;
import com.mivas.myharmonicasongs.listener.StylePickerListener;
import com.mivas.myharmonicasongs.util.Constants;
import com.mivas.myharmonicasongs.util.CustomizationUtils;
import com.mivas.myharmonicasongs.util.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Customize Note Activity.
 */
public class CustomizeNoteActivity extends AppCompatActivity implements SignPickerListener, StylePickerListener, ButtonPickerListener {

    private boolean blow;
    private int blowSign;
    private int blowStyle;
    private int blowTextColor;
    private int blowBackgroundColor;
    private int drawSign;
    private int drawStyle;
    private int drawTextColor;
    private int drawBackgroundColor;
    private int buttonStyle;
    private RecyclerView signList;
    private RecyclerView styleList;
    private RecyclerView buttonList;
    private SignPickerAdapter signAdapter;
    private StylePickerAdapter styleAdapter;
    private ButtonPickerAdapter buttonAdapter;
    private View textColorView;
    private View textColorColorView;
    private View backgroundColorView;
    private View backgroundColorColorView;
    private View buttonLayout;
    private View previewView;
    private TextView previewText;
    private int harpType;
    private boolean numbers16ChromaticNotation;
    private boolean changesMade = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_note);

        blow = getIntent().getBooleanExtra(Constants.EXTRA_BLOW, true);
        harpType = getIntent().getIntExtra(Constants.EXTRA_HARP_TYPE, 0);
        initStyles();
        initViews();
        initListeners();
        refreshPreview();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initStyles() {
        if (blow) {
            blowSign = CustomizationUtils.getBlowSign();
            blowStyle = CustomizationUtils.getBlowStyle();
            blowTextColor = CustomizationUtils.getBlowTextColor();
            blowBackgroundColor = CustomizationUtils.getBlowBackgroundColor();
        } else {
            drawSign = CustomizationUtils.getDrawSign();
            drawStyle = CustomizationUtils.getDrawStyle();
            drawTextColor = CustomizationUtils.getDrawTextColor();
            drawBackgroundColor = CustomizationUtils.getDrawBackgroundColor();
        }
        if (harpType != 0) {
            buttonStyle = CustomizationUtils.getButtonStyle();
        }
        if (harpType == 2) {
            numbers16ChromaticNotation = CustomizationUtils.get16NumbersChromaticNotation();
        }
    }

    /**
     * Views initializer.
     */
    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(blow ? R.string.customize_note_activity_text_blow_title : R.string.customize_note_activity_text_draw_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        signList = findViewById(R.id.list_signs);
        signList.setLayoutManager(new LinearLayoutManager(CustomizeNoteActivity.this, LinearLayout.HORIZONTAL, false));
        String noteText = "";
        if (harpType == 0) {
            noteText = "4";
        } else if (harpType == 1) {
            noteText = "5";
        } else if (harpType == 2) {
            noteText = numbers16ChromaticNotation ? "9" : "5";
        }
        signAdapter = new SignPickerAdapter(CustomizeNoteActivity.this, CustomizeNoteActivity.this, noteText , blow ? blowSign : drawSign);
        signList.setAdapter(signAdapter);
        styleList = findViewById(R.id.list_styles);
        styleList.setLayoutManager(new LinearLayoutManager(CustomizeNoteActivity.this, LinearLayout.HORIZONTAL, false));
        List<String> texts = new ArrayList<String>();
        for (int i = 0; i < 3; i++) {
            texts.add(noteText);
        }
        styleAdapter = new StylePickerAdapter(CustomizeNoteActivity.this, R.layout.list_item_note_style_picker, CustomizeNoteActivity.this, blow ? blowStyle : drawStyle, texts);
        styleList.setAdapter(styleAdapter);
        textColorView = findViewById(R.id.view_text_color);
        textColorColorView = findViewById(R.id.view_text_color_color);
        textColorColorView.setBackground(CustomizationUtils.createSimpleBackground(CustomizeNoteActivity.this, 6, blow ? blowTextColor : drawTextColor));
        backgroundColorView = findViewById(R.id.view_background_color);
        backgroundColorColorView = findViewById(R.id.view_background_color_color);
        backgroundColorColorView.setBackground(CustomizationUtils.createSimpleBackground(CustomizeNoteActivity.this, 6, blow ? blowBackgroundColor : drawBackgroundColor));
        previewView = findViewById(R.id.view_preview);
        previewText = findViewById(R.id.text_preview);
        buttonLayout = findViewById(R.id.layout_button);
        if (harpType != 0) {
            buttonLayout.setVisibility(View.VISIBLE);
            buttonList = findViewById(R.id.list_button);
            buttonList.setLayoutManager(new LinearLayoutManager(CustomizeNoteActivity.this, LinearLayout.HORIZONTAL, false));
            buttonAdapter = new ButtonPickerAdapter(CustomizeNoteActivity.this, CustomizeNoteActivity.this, buttonStyle);
            buttonList.setAdapter(buttonAdapter);
        } else {
            buttonLayout.setVisibility(View.GONE);
        }
    }

    /**
     * Listeners initializer.
     */
    private void initListeners() {
        textColorView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ColorPickerDialogBuilder
                        .with(CustomizeNoteActivity.this)
                        .setTitle(getString(R.string.customize_note_activity_text_color_title))
                        .initialColor(blow ? blowTextColor : drawTextColor)
                        .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                        .density(12)
                        .setPositiveButton(R.string.button_ok, new ColorPickerClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int selectedColor, Integer[] integers) {
                                changesMade = true;
                                if (blow) {
                                    blowTextColor = selectedColor;
                                    PreferencesUtils.storePreference(Constants.PREF_CURRENT_NOTE_BLOW_TEXT_COLOR, selectedColor);
                                } else {
                                    drawTextColor = selectedColor;
                                    PreferencesUtils.storePreference(Constants.PREF_CURRENT_NOTE_DRAW_TEXT_COLOR, selectedColor);
                                }
                                textColorColorView.setBackground(CustomizationUtils.createSimpleBackground(CustomizeNoteActivity.this, 6, selectedColor));
                                refreshPreview();
                            }
                        })
                        .build()
                        .show();
            }
        });
        backgroundColorView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ColorPickerDialogBuilder
                        .with(CustomizeNoteActivity.this)
                        .setTitle(getString(R.string.customize_note_activity_background_color_title))
                        .initialColor(blow ? blowBackgroundColor : drawBackgroundColor)
                        .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                        .density(12)
                        .setPositiveButton(R.string.button_ok, new ColorPickerClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int selectedColor, Integer[] integers) {
                                changesMade = true;
                                if (blow) {
                                    blowBackgroundColor = selectedColor;
                                    PreferencesUtils.storePreference(Constants.PREF_CURRENT_NOTE_BLOW_BACKGROUND_COLOR, selectedColor);
                                } else {
                                    drawBackgroundColor = selectedColor;
                                    PreferencesUtils.storePreference(Constants.PREF_CURRENT_NOTE_DRAW_BACKGROUND_COLOR, selectedColor);
                                }
                                backgroundColorColorView.setBackground(CustomizationUtils.createSimpleBackground(CustomizeNoteActivity.this, 6, selectedColor));
                                refreshPreview();
                            }
                        })
                        .build()
                        .show();
            }
        });
    }

    @Override
    public void onSignSelected(int position) {
        changesMade = true;
        if (blow) {
            blowSign = position;
            PreferencesUtils.storePreference(Constants.PREF_CURRENT_NOTE_BLOW_SIGN, position);
        } else {
            drawSign = position;
            PreferencesUtils.storePreference(Constants.PREF_CURRENT_NOTE_DRAW_SIGN, position);
        }
        signAdapter.setSelectedSign(position);
        signAdapter.notifyDataSetChanged();
        refreshPreview();
    }

    @Override
    public void onStyleSelected(int position) {
        changesMade = true;
        if (blow) {
            blowStyle = position;
            PreferencesUtils.storePreference(Constants.PREF_CURRENT_NOTE_BLOW_STYLE, position);
        } else {
            drawStyle = position;
            PreferencesUtils.storePreference(Constants.PREF_CURRENT_NOTE_DRAW_STYLE, position);
        }
        styleAdapter.setSelectedStyle(position);
        styleAdapter.notifyDataSetChanged();
        refreshPreview();
    }

    @Override
    protected void onDestroy() {
        if (changesMade) {

            // send broadcast receiver
            Intent intent = new Intent(Constants.INTENT_CUSTOMIZATIONS_UPDATED);
            sendBroadcast(intent);
        }
        super.onDestroy();
    }

    private void refreshPreview() {
        switch (harpType) {
            case 0:
                if (blow) {
                    CustomizationUtils.styleDiatonic10NoteText(previewText, 4, 0, blowSign, blowStyle, blowTextColor);
                    previewView.setBackground(CustomizationUtils.createSimpleBackground(CustomizeNoteActivity.this, 6, blowBackgroundColor));
                } else {
                    CustomizationUtils.styleDiatonic10NoteText(previewText, 4, 0, drawSign, drawStyle, drawTextColor);
                    previewView.setBackground(CustomizationUtils.createSimpleBackground(CustomizeNoteActivity.this, 6, drawBackgroundColor));
                }
                break;
            case 1:
                if (blow) {
                    CustomizationUtils.styleChromatic12NoteText(previewText, 5, 0.5f, blowSign, blowStyle, buttonStyle, blowTextColor);
                    previewView.setBackground(CustomizationUtils.createSimpleBackground(CustomizeNoteActivity.this, 6, blowBackgroundColor));
                } else {
                    CustomizationUtils.styleChromatic12NoteText(previewText, 5, -0.5f, drawSign, drawStyle, buttonStyle, drawTextColor);
                    previewView.setBackground(CustomizationUtils.createSimpleBackground(CustomizeNoteActivity.this, 6, drawBackgroundColor));
                }
                break;
            case 2:
                if (blow) {
                    CustomizationUtils.styleChromatic16NoteText(previewText, 9, 0.5f, blowSign, blowStyle, buttonStyle, numbers16ChromaticNotation, blowTextColor);
                    previewView.setBackground(CustomizationUtils.createSimpleBackground(CustomizeNoteActivity.this, 6, blowBackgroundColor));
                } else {
                    CustomizationUtils.styleChromatic16NoteText(previewText, 9, -0.5f, drawSign, drawStyle, buttonStyle, numbers16ChromaticNotation, drawTextColor);
                    previewView.setBackground(CustomizationUtils.createSimpleBackground(CustomizeNoteActivity.this, 6, drawBackgroundColor));
                }
                break;
        }

    }

    @Override
    public void onButtonSelected(int position) {
        changesMade = true;
        buttonStyle = position;
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_BUTTON_STYLE, position);
        buttonAdapter.setSelectedButton(position);
        buttonAdapter.notifyDataSetChanged();
        refreshPreview();
    }
}
