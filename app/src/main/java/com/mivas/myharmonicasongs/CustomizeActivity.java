package com.mivas.myharmonicasongs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.mivas.myharmonicasongs.util.Constants;
import com.mivas.myharmonicasongs.util.CustomToast;
import com.mivas.myharmonicasongs.util.CustomizationUtils;
import com.mivas.myharmonicasongs.util.PreferencesUtils;

/**
 * Credits Activity.
 */
public class CustomizeActivity extends AppCompatActivity {

    private View blowNoteView;
    private View drawNoteView;
    private View sectionView;
    private View sectionBarView;
    private View backgroundColorView;
    private View backgroundColorColorView;
    private Switch showBendsSwitch;
    private Switch showButtonSwitch;
    private Switch showMediaPlayerSwitch;
    private Switch playNoteSoundSwitch;
    private Switch playClosesMediaPlayerSwitch;
    private Switch numbers16ChromaticNotationSwitch;
    private View showBendsView;
    private View showButtonView;
    private View numbers16ChromaticNotationView;
    private int harpType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize);

        harpType = getIntent().getIntExtra(Constants.EXTRA_HARP_TYPE, 0);
        initViews();
        initListeners();
        refreshPreviews();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(CustomizeActivity.this);
                builder.setMessage(R.string.customize_activity_reset_customizations_dialog_description);
                builder.setPositiveButton(R.string.button_reset, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // reset prefs
                        resetPreferences();
                        refreshPreviews();

                        // show toast
                        CustomToast.makeText(CustomizeActivity.this, R.string.customize_activity_toast_customizations_reset, Toast.LENGTH_SHORT).show();

                        // send broadcast
                        sendCustomizationsUpdatedBroadcast();

                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void resetPreferences() {
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_NOTE_BLOW_SIGN, Constants.DEFAULT_NOTE_BLOW_SIGN);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_NOTE_BLOW_STYLE, Constants.DEFAULT_NOTE_BLOW_STYLE);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_NOTE_BLOW_TEXT_COLOR, Constants.DEFAULT_NOTE_BLOW_TEXT_COLOR);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_NOTE_BLOW_BACKGROUND_COLOR, Constants.DEFAULT_NOTE_BLOW_BACKGROUND_COLOR);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_NOTE_DRAW_SIGN, Constants.DEFAULT_NOTE_DRAW_SIGN);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_NOTE_DRAW_STYLE, Constants.DEFAULT_NOTE_DRAW_STYLE);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_NOTE_DRAW_TEXT_COLOR, Constants.DEFAULT_NOTE_DRAW_TEXT_COLOR);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_NOTE_DRAW_BACKGROUND_COLOR, Constants.DEFAULT_NOTE_DRAW_BACKGROUND_COLOR);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_BUTTON_STYLE, Constants.DEFAULT_BUTTON_STYLE);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_SECTION_STYLE, Constants.DEFAULT_SECTION_STYLE);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_SECTION_TEXT_COLOR, Constants.DEFAULT_SECTION_TEXT_COLOR);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_BACKGROUND_COLOR, Constants.DEFAULT_BACKGROUND_COLOR);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_SHOW_BENDS, Constants.DEFAULT_SHOW_BENDS);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_SHOW_BUTTON, Constants.DEFAULT_SHOW_BUTTON);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_SHOW_MEDIA_PLAYER, Constants.DEFAULT_SHOW_MEDIA_PLAYER);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_PLAY_CLOSES_MEDIA_PLAYER, Constants.DEFAULT_PLAY_CLOSES_MEDIA_PLAYER);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_16_NUMBERS_CHROMATIC_NOTATION, Constants.DEFAULT_16_NUMBERS_CHROMATIC_NOTATION);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_SHOW_SECTION_BAR, Constants.DEFAULT_SHOW_SECTION_BAR);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_SECTION_BAR_STYLE, Constants.DEFAULT_SECTION_BAR_STYLE);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_SECTION_BAR_TEXT_COLOR, Constants.DEFAULT_SECTION_BAR_TEXT_COLOR);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_SECTION_BAR_BACKGROUND, Constants.DEFAULT_SECTION_BAR_BACKGROUND);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_PLAY_NOTE_SOUND, Constants.DEFAULT_PLAY_NOTE_SOUND);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_NOTE_WIDTH, Constants.DEFAULT_NOTE_WIDTH);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_NOTE_HEIGHT, Constants.DEFAULT_NOTE_HEIGHT);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_NOTE_TEXT_SIZE, Constants.DEFAULT_NOTE_TEXT_SIZE);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_NOTE_WORD_SIZE, Constants.DEFAULT_NOTE_WORD_SIZE);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_SECTION_TEXT_SIZE, Constants.DEFAULT_SECTION_TEXT_SIZE);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_SECTION_BAR_HEIGHT, Constants.DEFAULT_SECTION_BAR_HEIGHT);
        PreferencesUtils.storePreference(Constants.PREF_CURRENT_SECTION_BAR_TEXT_SIZE, Constants.DEFAULT_SECTION_BAR_TEXT_SIZE);
    }

    /**
     * Views initializer.
     */
    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        blowNoteView = findViewById(R.id.view_blow_note);
        drawNoteView = findViewById(R.id.view_draw_note);
        sectionView = findViewById(R.id.view_section);
        sectionBarView = findViewById(R.id.view_section_bar);
        backgroundColorView = findViewById(R.id.view_background_color);
        backgroundColorColorView = findViewById(R.id.view_background_color_color);
        showBendsSwitch = findViewById(R.id.switch_show_bends);
        showButtonSwitch = findViewById(R.id.switch_show_button);
        showMediaPlayerSwitch = findViewById(R.id.switch_show_media_player);
        playNoteSoundSwitch = findViewById(R.id.switch_play_note_sound);
        playClosesMediaPlayerSwitch = findViewById(R.id.switch_play_closes_media_player);
        numbers16ChromaticNotationSwitch = findViewById(R.id.switch_16_numbers_chromatic_notation);
        showBendsView = findViewById(R.id.layout_show_bends);
        showButtonView = findViewById(R.id.layout_show_button);
        numbers16ChromaticNotationView = findViewById(R.id.layout_16_numbers_chromatic_notation);
        showBendsView.setVisibility(harpType == 0 ? View.VISIBLE : View.GONE);
        showButtonView.setVisibility(harpType == 0 ? View.GONE : View.VISIBLE);
        numbers16ChromaticNotationView.setVisibility(harpType == 2 ? View.VISIBLE : View.GONE);
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

        sectionBarView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomizeActivity.this, CustomizeSectionBarActivity.class);
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
                                sendCustomizationsUpdatedBroadcast();
                            }
                        })
                        .build()
                        .show();
            }
        });
        showBendsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferencesUtils.storePreference(Constants.PREF_CURRENT_SHOW_BENDS, isChecked);
                sendCustomizationsUpdatedBroadcast();
            }
        });
        showButtonSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferencesUtils.storePreference(Constants.PREF_CURRENT_SHOW_BUTTON, isChecked);
                sendCustomizationsUpdatedBroadcast();
            }
        });
        showMediaPlayerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferencesUtils.storePreference(Constants.PREF_CURRENT_SHOW_MEDIA_PLAYER, isChecked);
                sendCustomizationsUpdatedBroadcast();
            }
        });
        playNoteSoundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferencesUtils.storePreference(Constants.PREF_CURRENT_PLAY_NOTE_SOUND, isChecked);
                sendCustomizationsUpdatedBroadcast();
            }
        });
        playClosesMediaPlayerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferencesUtils.storePreference(Constants.PREF_CURRENT_PLAY_CLOSES_MEDIA_PLAYER, isChecked);
                sendCustomizationsUpdatedBroadcast();
            }
        });
        numbers16ChromaticNotationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferencesUtils.storePreference(Constants.PREF_CURRENT_16_NUMBERS_CHROMATIC_NOTATION, isChecked);
                sendCustomizationsUpdatedBroadcast();
            }
        });
    }

    private void startCustomizeNoteActivity(boolean blow) {
        Intent intent = new Intent(CustomizeActivity.this, CustomizeNoteActivity.class);
        intent.putExtra(Constants.EXTRA_BLOW, blow);
        intent.putExtra(Constants.EXTRA_HARP_TYPE, harpType);
        startActivity(intent);
    }

    private void refreshPreviews() {
        backgroundColorColorView.setBackground(CustomizationUtils.createSimpleBackground(CustomizeActivity.this, 6, CustomizationUtils.getBackgroundColor()));
        showBendsSwitch.setChecked(CustomizationUtils.getShowBends());
        showButtonSwitch.setChecked(CustomizationUtils.getShowButton());
        showMediaPlayerSwitch.setChecked(CustomizationUtils.getShowMediaPlayer());
        playNoteSoundSwitch.setChecked(CustomizationUtils.getPlayNoteSound());
        playClosesMediaPlayerSwitch.setChecked(CustomizationUtils.getPlayClosesMediaPlayer());
        numbers16ChromaticNotationSwitch.setChecked(CustomizationUtils.get16NumbersChromaticNotation());
    }

    private void sendCustomizationsUpdatedBroadcast() {
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT_CUSTOMIZATIONS_UPDATED);
        sendBroadcast(intent);
    }

}
