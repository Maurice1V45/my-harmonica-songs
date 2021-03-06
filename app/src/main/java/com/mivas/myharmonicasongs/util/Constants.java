package com.mivas.myharmonicasongs.util;

/**
 * Constants class
 */
public class Constants {

    /* Preferences */
    public static final String PREFERENCES = "prefs";
    public static final String PREF_FIRST_RUN = "pref_first_run";

    /* Dialog tags */
    public static final String TAG_SONG_DIALOG = "tag_song_dialog";
    public static final String TAG_TIME_PICKER_DIALOG = "tag_time_picker_dialog";
    public static final String TAG_SECTION_PICKER_DIALOG = "tag_section_picker_dialog";
    public static final String TAG_SECTION_LINE_PICKER_DIALOG = "tag_section_line_picker_dialog";
    public static final String TAG_NOTES_SHIFT_DIALOG = "tag_notes_shift_dialog";
    public static final String TAG_INSERT_NOTE_DIALOG = "tag_insert_note_dialog";

    /* Extras */
    public static final String EXTRA_SONG_ID = "extra_song_id";
    public static final String EXTRA_SONGS_UPDATED_COUNT = "extra_songs_updated_count";
    public static final String EXTRA_BLOW = "extra_blow";
    public static final String EXTRA_HARP_TYPE = "extra_harp_type";

    /* Broadcast receivers */
    public static final String INTENT_SONGS_UPDATED = "intent_songs_updated";
    public static final String INTENT_AUDIO_FILE_UPDATED = "intent_audio_file_updated";
    public static final String INTENT_CUSTOMIZATIONS_UPDATED = "intent_customizations_updated";
    public static final String INTENT_SCROLL_TIMERS_UPDATED = "intent_scroll_timers_updated";

    /* Default customizations */
    public static final int DEFAULT_NOTE_BLOW_SIGN = 2;
    public static final int DEFAULT_NOTE_BLOW_STYLE = 0;
    public static final int DEFAULT_NOTE_BLOW_TEXT_COLOR = 0xff000000;
    public static final int DEFAULT_NOTE_BLOW_BACKGROUND_COLOR = 0xffffffff;
    public static final int DEFAULT_NOTE_DRAW_SIGN = 0;
    public static final int DEFAULT_NOTE_DRAW_STYLE = 0;
    public static final int DEFAULT_NOTE_DRAW_TEXT_COLOR = 0xff000000;
    public static final int DEFAULT_NOTE_DRAW_BACKGROUND_COLOR = 0xffffffff;
    public static final int DEFAULT_NOTE_BACKGROUND_COLOR_PRESSED = 0xffcccccc;
    public static final int DEFAULT_NOTE_WIDTH = 3;
    public static final int DEFAULT_NOTE_HEIGHT = 3;
    public static final int DEFAULT_NOTE_TEXT_SIZE = 3;
    public static final int DEFAULT_NOTE_WORD_SIZE = 3;
    public static final int DEFAULT_SECTION_TEXT_SIZE = 3;
    public static final int DEFAULT_SECTION_BAR_HEIGHT = 3;
    public static final int DEFAULT_SECTION_BAR_TEXT_SIZE = 3;
    public static final int DEFAULT_COLOR_PRIMARY = 0xffad0556;
    public static final int DEFAULT_COLOR_BLACK = 0xff000000;
    public static final int DEFAULT_COLOR_WHITE = 0xffffffff;
    public static final int DEFAULT_BACKGROUND_COLOR = 0xffdddddd;
    public static final int DEFAULT_SECTION_STYLE = 0;
    public static final int DEFAULT_SECTION_TEXT_COLOR = 0xff000000;
    public static final boolean DEFAULT_SHOW_BENDS = false;
    public static final boolean DEFAULT_SHOW_BUTTON = false;
    public static final boolean DEFAULT_SHOW_MEDIA_PLAYER = false;
    public static final boolean DEFAULT_SHOW_SECTION_BAR = true;
    public static final int DEFAULT_SECTION_BAR_STYLE = 0;
    public static final int DEFAULT_SECTION_BAR_TEXT_COLOR = 0xff000000;
    public static final int DEFAULT_SECTION_BAR_BACKGROUND = 0xffffffff;
    public static final boolean DEFAULT_SCROLL_TIMERS_ENABLED = false;
    public static final boolean DEFAULT_PLAY_NOTE_SOUND = false;
    public static final int DEFAULT_HARP_TYPE = 0;
    public static final int DEFAULT_BUTTON_STYLE = 1;
    public static final boolean DEFAULT_PLAY_CLOSES_MEDIA_PLAYER = false;
    public static final boolean DEFAULT_16_NUMBERS_CHROMATIC_NOTATION = false;

    /* Current customizations */
    public static final String PREF_CURRENT_NOTE_BLOW_SIGN = "pref_current_note_blow_sign";
    public static final String PREF_CURRENT_NOTE_BLOW_STYLE = "pref_current_note_blow_style";
    public static final String PREF_CURRENT_NOTE_BLOW_TEXT_COLOR = "pref_current_note_blow_text_color";
    public static final String PREF_CURRENT_NOTE_BLOW_BACKGROUND_COLOR = "pref_current_note_blow_background_color";
    public static final String PREF_CURRENT_NOTE_DRAW_SIGN = "pref_current_note_draw_sign";
    public static final String PREF_CURRENT_NOTE_DRAW_STYLE = "pref_current_note_draw_style";
    public static final String PREF_CURRENT_NOTE_DRAW_TEXT_COLOR = "pref_current_note_draw_text_color";
    public static final String PREF_CURRENT_NOTE_DRAW_BACKGROUND_COLOR = "pref_current_note_draw_background_color";
    public static final String PREF_CURRENT_NOTE_WIDTH = "pref_current_note_width";
    public static final String PREF_CURRENT_NOTE_HEIGHT = "pref_current_note_height";
    public static final String PREF_CURRENT_NOTE_TEXT_SIZE = "pref_current_note_text_size";
    public static final String PREF_CURRENT_NOTE_WORD_SIZE = "pref_current_note_word_size";
    public static final String PREF_CURRENT_SECTION_TEXT_SIZE = "pref_current_section_text_size";
    public static final String PREF_CURRENT_SECTION_BAR_HEIGHT = "pref_current_section_bar_height";
    public static final String PREF_CURRENT_SECTION_BAR_TEXT_SIZE = "pref_current_section_bar_text_size";
    public static final String PREF_CURRENT_BACKGROUND_COLOR = "pref_current_background_color";
    public static final String PREF_CURRENT_SECTION_STYLE = "pref_current_section_style";
    public static final String PREF_CURRENT_SECTION_TEXT_COLOR = "pref_current_section_text_color";
    public static final String PREF_CURRENT_SHOW_BENDS = "pref_current_show_bends";
    public static final String PREF_CURRENT_SHOW_BUTTON = "pref_current_show_button";
    public static final String PREF_CURRENT_SHOW_MEDIA_PLAYER = "pref_current_show_media_player";
    public static final String PREF_CURRENT_SHOW_SECTION_BAR = "pref_current_show_section_bar";
    public static final String PREF_CURRENT_SECTION_BAR_STYLE = "pref_current_section_bar_style";
    public static final String PREF_CURRENT_SECTION_BAR_TEXT_COLOR = "pref_current_section_bar_text_color";
    public static final String PREF_CURRENT_SECTION_BAR_BACKGROUND = "pref_current_section_bar_background";
    public static final String PREF_CURRENT_SCROLL_TIMERS_ENABLED = "pref_current_scroll_timers_enabled";
    public static final String PREF_CURRENT_PLAY_NOTE_SOUND = "pref_current_play_note_sound";
    public static final String PREF_CURRENT_HARP_TYPE = "pref_current_harp_type";
    public static final String PREF_CURRENT_BUTTON_STYLE = "pref_current_button_style";
    public static final String PREF_CURRENT_PLAY_CLOSES_MEDIA_PLAYER = "pref_current_play_closes_media_player";
    public static final String PREF_CURRENT_16_NUMBERS_CHROMATIC_NOTATION = "pref_current_16_numbers_chromatic_notation";

    /* Misc */
    public static final String SEPARATOR_AUDIO_FILE = "%&#";

    /* URL */
    public static final String URL_PRIVACY_POLICY = "https://www.freeprivacypolicy.com/privacy/view/b48dfdc570071efa8ae1b06173454a80";
}
