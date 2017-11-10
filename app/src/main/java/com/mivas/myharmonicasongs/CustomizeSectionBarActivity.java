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
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Customize Note Activity.
 */
public class CustomizeSectionBarActivity extends AppCompatActivity implements StylePickerListener {

    private boolean showSectionBar;
    private int style;
    private int textColor;
    private int backgroundcolor;
    private RecyclerView styleList;
    private StylePickerAdapter styleAdapter;
    private View customizationsLayout;
    private View textColorView;
    private View textColorColorView;
    private View backgroundColorView;
    private View backgroundColorColorView;
    private TextView previewText;
    private Switch showSectionBarSwitch;
    private boolean changesMade = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_section_bar);

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
        showSectionBar = CustomizationUtils.getShowSectionBar();
        style = CustomizationUtils.getSectionBarStyle();
        textColor = CustomizationUtils.getSectionBarTextColor();
        backgroundcolor = CustomizationUtils.getSectionBarBackground();
    }

    /**
     * Views initializer.
     */
    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.customize_section_bar_activity_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        showSectionBarSwitch = findViewById(R.id.switch_show_section_bar);
        showSectionBarSwitch.setChecked(showSectionBar);
        customizationsLayout = findViewById(R.id.layout_customizations);
        styleList = findViewById(R.id.list_styles);
        styleList.setLayoutManager(new LinearLayoutManager(CustomizeSectionBarActivity.this, LinearLayout.HORIZONTAL, false));
        List<String> texts = new ArrayList<String>();
        for (int i = 0; i < 3; i++) {
            texts.add(String.valueOf(4));
        }
        styleAdapter = new StylePickerAdapter(CustomizeSectionBarActivity.this, R.layout.list_item_note_style_picker, CustomizeSectionBarActivity.this, style, texts);
        styleList.setAdapter(styleAdapter);
        textColorView = findViewById(R.id.view_text_color);
        textColorColorView = findViewById(R.id.view_text_color_color);
        textColorColorView.setBackground(CustomizationUtils.createSimpleBackground(CustomizeSectionBarActivity.this, 6, textColor));
        backgroundColorView = findViewById(R.id.view_background_color);
        backgroundColorColorView = findViewById(R.id.view_background_color_color);
        backgroundColorColorView.setBackground(CustomizationUtils.createSimpleBackground(CustomizeSectionBarActivity.this, 6, backgroundcolor));
        previewText = findViewById(R.id.text_preview);
        customizationsLayout.setVisibility(showSectionBar ? View.VISIBLE : View.GONE);
    }

    /**
     * Listeners initializer.
     */
    private void initListeners() {
        showSectionBarSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changesMade = true;
                PreferencesUtils.storePreference(Constants.PREF_CURRENT_SHOW_SECTION_BAR, isChecked);
                customizationsLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });
        textColorView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ColorPickerDialogBuilder
                        .with(CustomizeSectionBarActivity.this)
                        .setTitle(getString(R.string.customize_section_bar_activity_text_color_title))
                        .initialColor(textColor)
                        .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                        .density(12)
                        .setPositiveButton(R.string.button_ok, new ColorPickerClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int selectedColor, Integer[] integers) {
                                changesMade = true;
                                textColor = selectedColor;
                                PreferencesUtils.storePreference(Constants.PREF_CURRENT_SECTION_BAR_TEXT_COLOR, selectedColor);
                                textColorColorView.setBackground(CustomizationUtils.createSimpleBackground(CustomizeSectionBarActivity.this, 6, selectedColor));
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
                        .with(CustomizeSectionBarActivity.this)
                        .setTitle(getString(R.string.customize_section_bar_activity_background_color_title))
                        .initialColor(backgroundcolor)
                        .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                        .density(12)
                        .setPositiveButton(R.string.button_ok, new ColorPickerClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int selectedColor, Integer[] integers) {
                                changesMade = true;
                                backgroundcolor = selectedColor;
                                PreferencesUtils.storePreference(Constants.PREF_CURRENT_SECTION_BAR_BACKGROUND, selectedColor);
                                backgroundColorColorView.setBackground(CustomizationUtils.createSimpleBackground(CustomizeSectionBarActivity.this, 6, selectedColor));
                                refreshPreview();
                            }
                        })
                        .build()
                        .show();
            }
        });
    }

    @Override
    public void onStyleSelected(int position) {
        changesMade = true;
        style = position;
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_SECTION_BAR_STYLE, position);
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
        CustomizationUtils.styleSectionBarText(previewText, style, textColor);
        previewText.setBackground(CustomizationUtils.createSectionBarSimpleBackground(CustomizeSectionBarActivity.this, backgroundcolor));
    }

}
