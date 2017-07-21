package com.mivas.myharmonicasongs.util;

import android.graphics.Typeface;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Util class that handles all style representations.
 */
public class StyleUtils {

    public static void setStyle(TextView textView, int position) {
        switch (position) {
            case 0:
                textView.setTypeface(null, Typeface.NORMAL);
                break;
            case 1:
                textView.setTypeface(null, Typeface.BOLD);
                break;
            case 2:
                textView.setTypeface(null, Typeface.ITALIC);
                break;
            default:
                break;
        }
    }
}
