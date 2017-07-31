package com.mivas.myharmonicasongs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.mivas.myharmonicasongs.dialog.ResetCustomizationsDialog;
import com.mivas.myharmonicasongs.listener.CustomizeActivityListener;
import com.mivas.myharmonicasongs.util.Constants;
import com.mivas.myharmonicasongs.util.CustomToast;
import com.mivas.myharmonicasongs.util.CustomizationUtils;
import com.mivas.myharmonicasongs.util.PreferencesUtils;

/**
 * Credits Activity.
 */
public class CustomizeActivity extends AppCompatActivity implements CustomizeActivityListener {

    private View blowNoteView;
    private View blowNoteBackground;
    private TextView blowNoteText;
    private View drawNoteView;
    private View drawNoteBackground;
    private TextView drawNoteText;
    private View sectionView;
    private TextView sectionText;
    private View backgroundColorView;
    private View backgroundColorColorView;
    private BroadcastReceiver customizationReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            refreshPreviews();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initViews();
        initListeners();
        refreshPreviews();
        registerReceiver(customizationReceiver, new IntentFilter(Constants.INTENT_CUSTOMIZATIONS_UPDATED));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_customize_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_reset:
                ResetCustomizationsDialog dialog = new ResetCustomizationsDialog();
                dialog.setListener(CustomizeActivity.this);
                dialog.show(getFragmentManager(), Constants.TAG_RESET_CUSTOMIZATIONS_DIALOG);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Views initializer.
     */
    private void initViews() {
        blowNoteView = findViewById(R.id.view_blow_note);
        blowNoteBackground = findViewById(R.id.view_blow_background);
        blowNoteText = (TextView) findViewById(R.id.text_blow_note);
        drawNoteView = findViewById(R.id.view_draw_note);
        drawNoteBackground = findViewById(R.id.view_draw_background);
        drawNoteText = (TextView) findViewById(R.id.text_draw_note);
        sectionView = findViewById(R.id.view_section);
        sectionText = (TextView) findViewById(R.id.text_section);
        backgroundColorView = findViewById(R.id.view_background_color);
        backgroundColorColorView = findViewById(R.id.view_background_color_color);
    }

    /**
     * Listeners initializer.
     */
    private void initListeners() {
        blowNoteView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startCustomizeNoteActivity(true);
            }
        });

        drawNoteView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startCustomizeNoteActivity(false);
            }
        });

        sectionView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomizeActivity.this, CustomizeSectionActivity.class);
                startActivity(intent);
            }
        });

        backgroundColorView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ColorPickerDialogBuilder
                        .with(CustomizeActivity.this)
                        .setTitle(getString(R.string.customize_activity_background_color_title))
                        .initialColor(CustomizationUtils.getBackgroundColor())
                        .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                        .density(12)
                        .setPositiveButton(R.string.button_ok, new ColorPickerClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int selectedColor, Integer[] integers) {
                                PreferencesUtils.storePreference(Constants.PREF_CURRENT_BACKGROUND_COLOR, selectedColor);

                                // send broadcast receiver
                                Intent intent = new Intent();
                                intent.setAction(Constants.INTENT_CUSTOMIZATIONS_UPDATED);
                                sendBroadcast(intent);
                            }
                        })
                        .build()
                        .show();
            }
        });
    }

    private void startCustomizeNoteActivity(boolean blow) {
        Intent intent = new Intent(CustomizeActivity.this, CustomizeNoteActivity.class);
        intent.putExtra(Constants.EXTRA_BLOW, blow);
        startActivity(intent);
    }

    private void refreshPreviews() {
        CustomizationUtils.styleNoteText(blowNoteText, 4, 0, CustomizationUtils.getBlowSign(), CustomizationUtils.getBlowStyle(), CustomizationUtils.getBlowTextColor());
        blowNoteBackground.setBackground(CustomizationUtils.createSimpleBackground(CustomizeActivity.this, 6, CustomizationUtils.getBlowBackgroundColor()));
        CustomizationUtils.styleNoteText(drawNoteText, 4, 0, CustomizationUtils.getDrawSign(), CustomizationUtils.getDrawStyle(), CustomizationUtils.getDrawTextColor());
        drawNoteBackground.setBackground(CustomizationUtils.createSimpleBackground(CustomizeActivity.this, 6, CustomizationUtils.getDrawBackgroundColor()));
        CustomizationUtils.styleSectionText(sectionText, CustomizationUtils.getSectionStyle(), CustomizationUtils.getSectionTextColor());
        backgroundColorColorView.setBackground(CustomizationUtils.createSimpleBackground(CustomizeActivity.this, 6, CustomizationUtils.getBackgroundColor()));
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(customizationReceiver);
        super.onDestroy();
    }

    @Override
    public void onResetCustomizations() {

        // reset prefs
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_NOTE_BLOW_SIGN, Constants.DEFAULT_NOTE_BLOW_SIGN);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_NOTE_BLOW_STYLE, Constants.DEFAULT_NOTE_BLOW_STYLE);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_NOTE_BLOW_TEXT_COLOR, Constants.DEFAULT_NOTE_BLOW_TEXT_COLOR);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_NOTE_BLOW_BACKGROUND_COLOR, Constants.DEFAULT_NOTE_BLOW_BACKGROUND_COLOR);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_NOTE_DRAW_SIGN, Constants.DEFAULT_NOTE_DRAW_SIGN);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_NOTE_DRAW_STYLE, Constants.DEFAULT_NOTE_DRAW_STYLE);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_NOTE_DRAW_TEXT_COLOR, Constants.DEFAULT_NOTE_DRAW_TEXT_COLOR);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_NOTE_DRAW_BACKGROUND_COLOR, Constants.DEFAULT_NOTE_DRAW_BACKGROUND_COLOR);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_SECTION_STYLE, Constants.DEFAULT_SECTION_STYLE);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_SECTION_TEXT_COLOR, Constants.DEFAULT_SECTION_TEXT_COLOR);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_BACKGROUND_COLOR, Constants.DEFAULT_BACKGROUND_COLOR);

        // show toast
        CustomToast.makeText(CustomizeActivity.this, R.string.customize_activity_toast_customizations_reset, Toast.LENGTH_SHORT).show();

        // send broadcast
        Intent intent = new Intent(Constants.INTENT_CUSTOMIZATIONS_UPDATED);
        sendBroadcast(intent);
    }
}
