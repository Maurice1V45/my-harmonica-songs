package com.mivas.myharmonicasongs.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * Util class that converts dp, sp, px.
 */
public class DimensionUtils {

    /**
     * Converts dp to px.
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dpToPx(Context context, int dp) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return px;
    }

    /**
     * Converts sp to px.
     *
     * @param context
     * @param sp
     * @return
     */
    public static int convertSpToPixels(Context context, float sp) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return px;
    }
}
