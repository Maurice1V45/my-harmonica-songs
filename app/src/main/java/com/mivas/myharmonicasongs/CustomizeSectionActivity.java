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
import com.mivas.myharmonicasongs.adapter.StylePickerAdapter;
import com.mivas.myharmonicasongs.listener.StylePickerListener;
import com.mivas.myharmonicasongs.util.Constants;
import com.mivas.myharmonicasongs.util.CustomizationUtils;
import com.mivas.myharmonicasongs.util.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Customize Note Activity.
 */
public class CustomizeSectionActivity extends AppCompatActivity implements StylePickerListener {

    private int sectionStyle;
    private int sectionTextColor;
    private RecyclerView styleList;
    private StylePickerAdapter styleAdapter;
    private View textColorView;
    private View textColorColorView;
    private TextView previewText;
    private boolean changesMade = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_section);

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
        sectionStyle = CustomizationUtils.getSectionStyle();
        sectionTextColor = CustomizationUtils.getSectionTextColor();
    }

    /**
     * Views initializer.
     */
    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        styleList = (RecyclerView) findViewById(R.id.list_styles);
        styleList.setLayoutManager(new LinearLayoutManager(CustomizeSectionActivity.this, LinearLayout.HORIZONTAL, false));
        List<String> texts = new ArrayList<String>();
        texts.add("Normal");
        texts.add("Bold");
        texts.add("Italic");
        styleAdapter = new StylePickerAdapter(CustomizeSectionActivity.this, R.layout.list_item_section_style_picker, CustomizeSectionActivity.this, sectionStyle, texts);
        styleList.setAdapter(styleAdapter);
        textColorView = findViewById(R.id.view_text_color);
        textColorColorView = findViewById(R.id.view_text_color_color);
        textColorColorView.setBackground(CustomizationUtils.createSimpleBackground(CustomizeSectionActivity.this, 6, sectionTextColor));
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
                        .with(CustomizeSectionActivity.this)
                        .setTitle(getString(R.string.customize_section_activity_text_color_title))
                        .initialColor(sectionTextColor)
                        .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                        .density(12)
                        .setPositiveButton(R.string.button_ok, new ColorPickerClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int selectedColor, Integer[] integers) {
                                changesMade = true;
                                sectionTextColor = selectedColor;
                                PreferencesUtils.storePreference(Constants.PREF_CURRENT_SECTION_TEXT_COLOR, selectedColor);
                                textColorColorView.setBackground(CustomizationUtils.createSimpleBackground(CustomizeSectionActivity.this, 6, selectedColor));
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
        sectionStyle = position;
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_SECTION_STYLE, position);
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
        CustomizationUtils.styleSectionText(previewText, sectionStyle, sectionTextColor);
    }
}
