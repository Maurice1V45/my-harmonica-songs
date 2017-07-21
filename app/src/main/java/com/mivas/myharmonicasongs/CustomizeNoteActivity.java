package com.mivas.myharmonicasongs;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.mivas.myharmonicasongs.adapter.SignPickerAdapter;
import com.mivas.myharmonicasongs.adapter.StylePickerAdapter;
import com.mivas.myharmonicasongs.listener.CustomizeNoteActivityListener;
import com.mivas.myharmonicasongs.listener.StylePickerListener;
import com.mivas.myharmonicasongs.util.Constants;
import com.mivas.myharmonicasongs.util.CustomizationUtils;
import com.mivas.myharmonicasongs.util.PreferencesUtils;

/**
 * Customize Note Activity.
 */
public class CustomizeNoteActivity extends AppCompatActivity implements CustomizeNoteActivityListener, StylePickerListener {

    private boolean blow;
    private int blowSign;
    private int blowStyle;
    private int blowTextColor;
    private int blowBackgroundColor;
    private int drawSign;
    private int drawStyle;
    private int drawTextColor;
    private int drawBackgroundColor;
    private RecyclerView signList;
    private RecyclerView styleList;
    private SignPickerAdapter signAdapter;
    private StylePickerAdapter styleAdapter;
    private View textColorView;
    private View textColorColorView;
    private View backgroundColorView;
    private View backgroundColorColorView;
    private View previewView;
    private TextView previewText;
    private boolean changesMade = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_note);

        blow = getIntent().getBooleanExtra(Constants.EXTRA_BLOW, true);
        initStyles();
        initViews();
        initListeners();
        refreshPreview();
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
    }

    /**
     * Views initializer.
     */
    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(blow ? R.string.customize_note_activity_text_blow_title : R.string.customize_note_activity_text_draw_title);
        signList = (RecyclerView) findViewById(R.id.list_signs);
        signList.setLayoutManager(new LinearLayoutManager(CustomizeNoteActivity.this, LinearLayout.HORIZONTAL, false));
        signAdapter = new SignPickerAdapter(CustomizeNoteActivity.this, CustomizeNoteActivity.this, blow ? blowSign : drawSign);
        signList.setAdapter(signAdapter);
        styleList = (RecyclerView) findViewById(R.id.list_styles);
        styleList.setLayoutManager(new LinearLayoutManager(CustomizeNoteActivity.this, LinearLayout.HORIZONTAL, false));
        styleAdapter = new StylePickerAdapter(CustomizeNoteActivity.this, R.layout.list_item_note_style_picker, CustomizeNoteActivity.this, blow ? blowStyle : drawStyle, "4");
        styleList.setAdapter(styleAdapter);
        textColorView = findViewById(R.id.view_text_color);
        textColorColorView = findViewById(R.id.view_text_color_color);
        textColorColorView.setBackground(CustomizationUtils.createSimpleBackground(CustomizeNoteActivity.this, 6, blow ? blowTextColor : drawTextColor));
        backgroundColorView = findViewById(R.id.view_background_color);
        backgroundColorColorView = findViewById(R.id.view_background_color_color);
        backgroundColorColorView.setBackground(CustomizationUtils.createSimpleBackground(CustomizeNoteActivity.this, 6, blow ? blowBackgroundColor : drawBackgroundColor));
        previewView = findViewById(R.id.view_preview);
        previewText = (TextView) findViewById(R.id.text_preview);
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
        if (blow) {
            CustomizationUtils.styleNoteText(previewText, 4, 0, blowSign, blowStyle, blowTextColor);
            previewView.setBackground(CustomizationUtils.createSimpleBackground(CustomizeNoteActivity.this, 6, blowBackgroundColor));
        } else {
            CustomizationUtils.styleNoteText(previewText, 4, 0, drawSign, drawStyle, drawTextColor);
            previewView.setBackground(CustomizationUtils.createSimpleBackground(CustomizeNoteActivity.this, 6, drawBackgroundColor));
        }
    }
}
