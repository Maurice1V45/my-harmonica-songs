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
        normalShape.setCornerRadii(new float[] {0, 0, 0, 0, dp6, dp6, dp6, dp6});
        normalShape.setColor(backgroundColor);
        GradientDrawable pressedShape = new GradientDrawable();
        pressedShape.setShape(GradientDrawable.RECTANGLE);
        pressedShape.setCornerRadii(new float[] {0, 0, 0, 0, dp6, dp6, dp6, dp6});
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
        shape.setCornerRadii(new float[] {0, 0, 0, 0, dp6, dp6, dp6, dp6});
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

    private static String getNoteText(int hole, int sign, float bending) {
        StringBuilder format = new StringBuilder();
        format.append(NoteSignUtils.getFormat(sign));
        for (int i = 0; i < Math.abs(bending * 2); i++) {
            format.append("'");
        }
        return String.format(format.toString(), hole);
    }
}
