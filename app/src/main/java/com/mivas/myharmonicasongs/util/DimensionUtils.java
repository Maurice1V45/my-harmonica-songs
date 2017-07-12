package com.mivas.myharmonicasongs.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by Marius on 12-Jul-17.
 */

public class DimensionUtils {

    public static int dpToPx(Context context, int dp) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return px;
    }

    public static int convertSpToPixels(Context context, float sp) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return px;
    }
}
