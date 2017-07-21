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
        int retrievedBlowSign = PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_NOTE_BLOW_SIGN, -1);
        return retrievedBlowSign == -1 ? Constants.DEFAULT_NOTE_BLOW_SIGN : retrievedBlowSign;
    }

    public static int getBlowStyle() {
        int retrievedBlowStyle = PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_NOTE_BLOW_STYLE, -1);
        return retrievedBlowStyle == -1 ? Constants.DEFAULT_NOTE_BLOW_STYLE : retrievedBlowStyle;
    }

    public static int getBlowTextColor() {
        int retrievedBlowTextColor = PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_NOTE_BLOW_TEXT_COLOR, -1);
        return retrievedBlowTextColor == -1 ? Constants.DEFAULT_NOTE_BLOW_TEXT_COLOR : retrievedBlowTextColor;
    }

    public static int getBlowBackgroundColor() {
        int retrievedBlowBackgroundColor = PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_NOTE_BLOW_BACKGROUND_COLOR, -1);
        return retrievedBlowBackgroundColor == -1 ? Constants.DEFAULT_NOTE_BLOW_BACKGROUND_COLOR : retrievedBlowBackgroundColor;
    }

    public static int getDrawSign() {
        int retrievedDrawSign = PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_NOTE_DRAW_SIGN, -1);
        return retrievedDrawSign == -1 ? Constants.DEFAULT_NOTE_DRAW_SIGN : retrievedDrawSign;
    }

    public static int getDrawStyle() {
        int retrievedDrawStyle = PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_NOTE_DRAW_STYLE, -1);
        return retrievedDrawStyle == -1 ? Constants.DEFAULT_NOTE_DRAW_STYLE : retrievedDrawStyle;
    }

    public static int getDrawTextColor() {
        int retrievedDrawTextColor = PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_NOTE_DRAW_TEXT_COLOR, -1);
        return retrievedDrawTextColor == -1 ? Constants.DEFAULT_NOTE_DRAW_TEXT_COLOR : retrievedDrawTextColor;
    }

    public static int getDrawBackgroundColor() {
        int retrievedDrawBackgroundColor = PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_NOTE_DRAW_BACKGROUND_COLOR, -1);
        return retrievedDrawBackgroundColor == -1 ? Constants.DEFAULT_NOTE_DRAW_BACKGROUND_COLOR : retrievedDrawBackgroundColor;
    }

    public static int getBackgroundColor() {
        int retrievedBackgroundColor = PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_BACKGROUND_COLOR, -1);
        return retrievedBackgroundColor == -1 ? Constants.DEFAULT_BACKGROUND_COLOR : retrievedBackgroundColor;
    }

    public static int getSectionStyle() {
        int retrievedSectionStyle = PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_SECTION_STYLE, -1);
        return retrievedSectionStyle == -1 ? Constants.DEFAULT_SECTION_STYLE : retrievedSectionStyle;
    }

    public static int getSectionTextColor() {
        int retrievedSectionTextColor = PreferencesUtils.getPreferences().getInt(Constants.PREF_CURRENT_SECTION_TEXT_COLOR, -1);
        return retrievedSectionTextColor == -1 ? Constants.DEFAULT_SECTION_TEXT_COLOR : retrievedSectionTextColor;
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
        stateListDrawable.addState(new int[] {android.R.attr.state_pressed}, pressedShape);
        stateListDrawable.addState(new int[] {}, normalShape);
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
        stateListDrawable.addState(new int[] {android.R.attr.state_pressed}, pressedShape);
        stateListDrawable.addState(new int[] {}, normalShape);
        return stateListDrawable;
    }

    public static ColorStateList createNotePickerTextColor(Context context, int textColor) {
        int[][] states = new int[][] {
                new int[] {android.R.attr.state_pressed},
                new int[] {-android.R.attr.state_pressed}
        };

        int[] colors = new int[] {
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

    public static void styleNoteText(TextView textView, int hole, float bending, int sign, int style, int textColor) {
        textView.setText(getNoteText(hole, sign, bending));
        StyleUtils.setStyle(textView, style);
        textView.setTextColor(textColor);
    }

    public static void styleNoteText(TextView textView, int hole, float bending, int sign, int style, ColorStateList colorStateList) {
        textView.setText(getNoteText(hole, sign, bending));
        StyleUtils.setStyle(textView, style);
        textView.setTextColor(colorStateList);
    }

    public static void styleSectionText(TextView textView, int style, int textColor) {
        StyleUtils.setStyle(textView, style);
        textView.setTextColor(textColor);
    }

    private static String getNoteText(int hole, int sign, float bending) {
        StringBuilder format = new StringBuilder();
        format.append(NoteSignUtils.getFormat(sign));
        for (int i = 0; i < Math.abs(bending * 2); i++) {
            format.append("'");
        }
        return String.format(format.toString(), hole);
    }
}
