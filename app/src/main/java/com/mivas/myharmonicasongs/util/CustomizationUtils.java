package com.mivas.myharmonicasongs.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.widget.TextView;

/**
 * Util class for customizations.
 */
public class CustomizationUtils {

    public static int getBlowSign() {
        return PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_NOTE_BLOW_SIGN, Constants.DEFAULT_NOTE_BLOW_SIGN);
    }

    public static int getBlowStyle() {
        return PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_NOTE_BLOW_STYLE, Constants.DEFAULT_NOTE_BLOW_STYLE);
    }

    public static int getBlowTextColor() {
        return PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_NOTE_BLOW_TEXT_COLOR, Constants.DEFAULT_NOTE_BLOW_TEXT_COLOR);
    }

    public static int getBlowBackgroundColor() {
        return PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_NOTE_BLOW_BACKGROUND_COLOR, Constants.DEFAULT_NOTE_BLOW_BACKGROUND_COLOR);
    }

    public static int getDrawSign() {
        return PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_NOTE_DRAW_SIGN, Constants.DEFAULT_NOTE_DRAW_SIGN);
    }

    public static int getDrawStyle() {
        return PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_NOTE_DRAW_STYLE, Constants.DEFAULT_NOTE_DRAW_STYLE);
    }

    public static int getDrawTextColor() {
        return PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_NOTE_DRAW_TEXT_COLOR, Constants.DEFAULT_NOTE_DRAW_TEXT_COLOR);
    }

    public static int getDrawBackgroundColor() {
        return PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_NOTE_DRAW_BACKGROUND_COLOR, Constants.DEFAULT_NOTE_DRAW_BACKGROUND_COLOR);
    }

    public static int getBackgroundColor() {
        return PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_BACKGROUND_COLOR, Constants.DEFAULT_BACKGROUND_COLOR);
    }

    public static int getSectionStyle() {
        return PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_SECTION_STYLE, Constants.DEFAULT_SECTION_STYLE);
    }

    public static int getSectionTextColor() {
        return PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_SECTION_TEXT_COLOR, Constants.DEFAULT_SECTION_TEXT_COLOR);
    }

    public static boolean getShowBends() {
        return PreferencesUtils.getPreferences().getBoolean(Constants.PREF_CURRENT_SHOW_BENDS, Constants.DEFAULT_SHOW_BENDS);
    }

    public static boolean getShowButton() {
        return PreferencesUtils.getPreferences().getBoolean(Constants.PREF_CURRENT_SHOW_BUTTON, Constants.DEFAULT_SHOW_BUTTON);
    }

    public static boolean getShowMediaPlayer() {
        return PreferencesUtils.getPreferences().getBoolean(Constants.PREF_CURRENT_SHOW_MEDIA_PLAYER, Constants.DEFAULT_SHOW_MEDIA_PLAYER);
    }

    public static boolean getShowSectionBar() {
        return PreferencesUtils.getPreferences().getBoolean(Constants.PREF_CURRENT_SHOW_SECTION_BAR, Constants.DEFAULT_SHOW_SECTION_BAR);
    }

    public static int getSectionBarBackground() {
        return PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_SECTION_BAR_BACKGROUND, Constants.DEFAULT_SECTION_BAR_BACKGROUND);
    }

    public static int getSectionBarTextColor() {
        return PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_SECTION_BAR_TEXT_COLOR, Constants.DEFAULT_SECTION_BAR_TEXT_COLOR);
    }

    public static int getSectionBarStyle() {
        return PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_SECTION_BAR_STYLE, Constants.DEFAULT_SECTION_BAR_STYLE);
    }

    public static boolean getScrollTimersEnabled() {
        return PreferencesUtils.getPreferences().getBoolean(Constants.PREF_CURRENT_SCROLL_TIMERS_ENABLED, Constants.DEFAULT_SCROLL_TIMERS_ENABLED);
    }

    public static boolean getPlayNoteSound() {
        return PreferencesUtils.getPreferences().getBoolean(Constants.PREF_CURRENT_PLAY_NOTE_SOUND, Constants.DEFAULT_PLAY_NOTE_SOUND);
    }

    public static int getHarpType() {
        return PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_HARP_TYPE, Constants.DEFAULT_HARP_TYPE);
    }

    public static int getButtonStyle() {
        return PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_BUTTON_STYLE, Constants.DEFAULT_BUTTON_STYLE);
    }

    public static boolean getPlayClosesMediaPlayer() {
        return PreferencesUtils.getPreferences().getBoolean(Constants.PREF_CURRENT_PLAY_CLOSES_MEDIA_PLAYER, Constants.DEFAULT_PLAY_CLOSES_MEDIA_PLAYER);
    }

    public static boolean get16NumbersChromaticNotation() {
        return PreferencesUtils.getPreferences().getBoolean(Constants.PREF_CURRENT_16_NUMBERS_CHROMATIC_NOTATION, Constants.DEFAULT_16_NUMBERS_CHROMATIC_NOTATION);
    }

    public static int getNoteWidth() {
        return PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_NOTE_WIDTH, Constants.DEFAULT_NOTE_WIDTH);
    }

    public static int getNoteHeight() {
        return PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_NOTE_HEIGHT, Constants.DEFAULT_NOTE_HEIGHT);
    }

    public static int getNoteTextSize() {
        return PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_NOTE_TEXT_SIZE, Constants.DEFAULT_NOTE_TEXT_SIZE);
    }

    public static int getNoteWordSize() {
        return PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_NOTE_WORD_SIZE, Constants.DEFAULT_NOTE_WORD_SIZE);
    }

    public static int getSectionTextSize() {
        return PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_SECTION_TEXT_SIZE, Constants.DEFAULT_SECTION_TEXT_SIZE);
    }

    public static int getSectionBarHeight() {
        return PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_SECTION_BAR_HEIGHT, Constants.DEFAULT_SECTION_BAR_HEIGHT);
    }

    public static int getSectionBarTextSize() {
        return PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_SECTION_BAR_TEXT_SIZE, Constants.DEFAULT_SECTION_BAR_TEXT_SIZE);
    }

    public static StateListDrawable createNoteBackground(Context context, int backgroundColor) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        GradientDrawable normalShape = new GradientDrawable();
        normalShape.setShape(GradientDrawable.RECTANGLE);
        normalShape.setCornerRadius(DimensionUtils.dpToPx(context, 6));
        normalShape.setColor(backgroundColor);
        GradientDrawable pressedShape = new GradientDrawable();
        pressedShape.setShape(GradientDrawable.RECTANGLE);
        pressedShape.setCornerRadius(DimensionUtils.dpToPx(context, 6));
        pressedShape.setColor(Constants.DEFAULT_NOTE_BACKGROUND_COLOR_PRESSED);
        GradientDrawable selectedShape = new GradientDrawable();
        selectedShape.setShape(GradientDrawable.RECTANGLE);
        selectedShape.setCornerRadius(DimensionUtils.dpToPx(context, 6));
        selectedShape.setColor(Constants.DEFAULT_COLOR_PRIMARY);
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedShape);
        stateListDrawable.addState(new int[]{android.R.attr.state_selected}, selectedShape);
        stateListDrawable.addState(new int[]{}, normalShape);
        return stateListDrawable;
    }

    public static StateListDrawable createNotePickerBackground(Context context, int backgroundColor) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        GradientDrawable normalShape = new GradientDrawable();
        normalShape.setShape(GradientDrawable.RECTANGLE);
        normalShape.setStroke(DimensionUtils.dpToPx(context, 1), Constants.DEFAULT_COLOR_BLACK);
        normalShape.setCornerRadius(DimensionUtils.dpToPx(context, 12));
        normalShape.setColor(backgroundColor);
        GradientDrawable pressedShape = new GradientDrawable();
        pressedShape.setShape(GradientDrawable.RECTANGLE);
        pressedShape.setCornerRadius(DimensionUtils.dpToPx(context, 12));
        pressedShape.setColor(Constants.DEFAULT_COLOR_PRIMARY);
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedShape);
        stateListDrawable.addState(new int[]{}, normalShape);
        return stateListDrawable;
    }

    public static StateListDrawable createSectionBarBackground(Context context, int backgroundColor) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        GradientDrawable normalShape = new GradientDrawable();
        normalShape.setShape(GradientDrawable.RECTANGLE);
        int dp6 = DimensionUtils.dpToPx(context, 6);
        normalShape.setCornerRadii(new float[]{0, 0, 0, 0, dp6, dp6, dp6, dp6});
        normalShape.setColor(backgroundColor);
        GradientDrawable pressedShape = new GradientDrawable();
        pressedShape.setShape(GradientDrawable.RECTANGLE);
        pressedShape.setCornerRadii(new float[]{0, 0, 0, 0, dp6, dp6, dp6, dp6});
        pressedShape.setColor(Constants.DEFAULT_COLOR_PRIMARY);
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedShape);
        stateListDrawable.addState(new int[]{}, normalShape);
        return stateListDrawable;
    }

    public static ColorStateList createNotePickerTextColor(Context context, int textColor) {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_pressed},
                new int[]{-android.R.attr.state_pressed}
        };

        int[] colors = new int[]{
                Constants.DEFAULT_COLOR_WHITE,
                textColor
        };

        return new ColorStateList(states, colors);
    }

    public static ColorStateList createSectionBarTextColor(Context context, int textColor) {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_pressed},
                new int[]{-android.R.attr.state_pressed}
        };

        int[] colors = new int[]{
                Constants.DEFAULT_COLOR_WHITE,
                textColor
        };

        return new ColorStateList(states, colors);
    }

    public static ColorStateList createNoteTextColor(int textColor) {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_selected},
                new int[]{-android.R.attr.state_selected}
        };

        int[] colors = new int[]{
                Constants.DEFAULT_COLOR_WHITE,
                textColor
        };

        return new ColorStateList(states, colors);
    }

    public static GradientDrawable createSimpleBackground(Context context, int dpRadius, int backgroundColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(DimensionUtils.dpToPx(context, dpRadius));
        shape.setColor(backgroundColor);
        return shape;
    }

    public static GradientDrawable createSectionBarSimpleBackground(Context context, int backgroundColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        int dp6 = DimensionUtils.dpToPx(context, 6);
        shape.setCornerRadii(new float[]{0, 0, 0, 0, dp6, dp6, dp6, dp6});
        shape.setColor(backgroundColor);
        return shape;
    }

    public static void styleDiatonic10NoteText(TextView textView, int hole, float bending, int sign, int style, int textColor) {
        textView.setText(getDiatonic10NoteText(hole, sign, bending));
        StyleUtils.setStyle(textView, style);
        textView.setTextColor(textColor);
    }

    public static void styleDiatonic10NoteText(TextView textView, int hole, float bending, int sign, int style, ColorStateList colorStateList) {
        textView.setText(getDiatonic10NoteText(hole, sign, bending));
        StyleUtils.setStyle(textView, style);
        textView.setTextColor(colorStateList);
    }

    public static void styleChromatic12NoteText(TextView textView, int hole, float bending, int sign, int style, int buttonStyle, int textColor) {
        textView.setText(getChromatic12NoteText(hole, sign, bending, buttonStyle));
        StyleUtils.setStyle(textView, style);
        textView.setTextColor(textColor);
    }

    public static void styleChromatic12NoteText(TextView textView, int hole, float bending, int sign, int style, int buttonStyle, ColorStateList colorStateList) {
        textView.setText(getChromatic12NoteText(hole, sign, bending, buttonStyle));
        StyleUtils.setStyle(textView, style);
        textView.setTextColor(colorStateList);
    }

    public static void styleChromatic16NoteText(TextView textView, int hole, float bending, int sign, int style, int buttonStyle, boolean numbers16Notation, int textColor) {
        textView.setText(getChromatic16NoteText(hole, sign, bending, buttonStyle, numbers16Notation));
        StyleUtils.setStyle(textView, style);
        textView.setTextColor(textColor);
    }

    public static void styleChromatic16NoteText(TextView textView, int hole, float bending, int sign, int style, int buttonStyle, boolean numbers16Notation, ColorStateList colorStateList) {
        textView.setText(getChromatic16NoteText(hole, sign, bending, buttonStyle, numbers16Notation));
        StyleUtils.setStyle(textView, style);
        textView.setTextColor(colorStateList);
    }

    public static void styleSectionBarText(TextView textView, int style, int textColor) {
        StyleUtils.setStyle(textView, style);
        textView.setTextColor(textColor);
    }

    public static void styleSectionBarText(TextView textView, int style, ColorStateList colorStateList) {
        StyleUtils.setStyle(textView, style);
        textView.setTextColor(colorStateList);
    }

    public static void styleSectionText(TextView textView, int style, int textColor) {
        StyleUtils.setStyle(textView, style);
        textView.setTextColor(textColor);
    }

    private static String getDiatonic10NoteText(int hole, int sign, float bending) {
        StringBuilder format = new StringBuilder();
        format.append(NoteSignUtils.getFormat(sign));
        for (int i = 0; i < Math.abs(bending * 2); i++) {
            format.append("'");
        }
        return String.format(format.toString(), hole);
    }

    private static String getChromatic12NoteText(int hole, int sign, float bending, int buttonStyle) {
        StringBuilder format = new StringBuilder();
        format.append(NoteSignUtils.getFormat(sign));
        String noteString = String.format(format.toString(), hole);
        if (bending != 0) {
            switch (buttonStyle) {
                case 0:
                    noteString = "*" + noteString;
                    break;
                case 1:
                    noteString = noteString + "*";
                    break;
                case 2:
                    noteString = "<" + noteString;
                    break;
                case 3:
                    noteString = noteString + "<";
                    break;
            }
        }
        return noteString;
    }

    private static String getChromatic16NoteText(int hole, int sign, float bending, int buttonStyle, boolean numbers16Notation) {
        StringBuilder format = new StringBuilder();
        format.append(NoteSignUtils.getFormat(sign));
        String noteString;
        if (numbers16Notation) {
            noteString = String.format(format.toString(), hole);
        } else {
            if (hole < 5) {
                noteString = "°" + String.format(format.toString(), hole);
            } else {
                hole -= 4;
                noteString = String.format(format.toString(), hole);
            }
        }
        if (bending != 0) {
            switch (buttonStyle) {
                case 0:
                    noteString = "*" + noteString;
                    break;
                case 1:
                    noteString = noteString + "*";
                    break;
                case 2:
                    noteString = "<" + noteString;
                    break;
                case 3:
                    noteString = noteString + "<";
                    break;
            }
        }
        return noteString;
    }

    public static int getNoteWidthValue(int position) {
        switch (position) {
            case 0:
                return 30;
            case 1:
                return 36;
            case 2:
                return 42;
            case 3:
                return 48;
            case 4:
                return 54;
            case 5:
                return 60;
            case 6:
                return 66;
        }
        return 0;
    }

    public static int getNoteHeightValue(int position) {
        switch (position) {
            case 0:
                return 48;
            case 1:
                return 54;
            case 2:
                return 60;
            case 3:
                return 66;
            case 4:
                return 72;
            case 5:
                return 78;
            case 6:
                return 84;
        }
        return 0;
    }

    public static int getNoteTextSizeValue(int position) {
        switch (position) {
            case 0:
                return 15;
            case 1:
                return 18;
            case 2:
                return 21;
            case 3:
                return 24;
            case 4:
                return 27;
            case 5:
                return 30;
            case 6:
                return 33;
        }
        return 0;
    }

    public static int getNoteWordSizeValue(int position) {
        switch (position) {
            case 0:
                return 7;
            case 1:
                return 8;
            case 2:
                return 9;
            case 3:
                return 10;
            case 4:
                return 11;
            case 5:
                return 12;
            case 6:
                return 13;
        }
        return 0;
    }

    public static int getSectionSizeValue(int position) {
        switch (position) {
            case 0:
                return 15;
            case 1:
                return 18;
            case 2:
                return 21;
            case 3:
                return 24;
            case 4:
                return 27;
            case 5:
                return 30;
            case 6:
                return 33;
        }
        return 0;
    }

    public static int getSectionBarHeightValue(int position) {
        switch (position) {
            case 0:
                return 21;
            case 1:
                return 24;
            case 2:
                return 27;
            case 3:
                return 30;
            case 4:
                return 33;
            case 5:
                return 36;
            case 6:
                return 39;
        }
        return 0;
    }

    public static int getSectionBarTextSizeValue(int position) {
        switch (position) {
            case 0:
                return 6;
            case 1:
                return 8;
            case 2:
                return 10;
            case 3:
                return 12;
            case 4:
                return 14;
            case 5:
                return 16;
            case 6:
                return 18;
        }
        return 0;
    }
}
