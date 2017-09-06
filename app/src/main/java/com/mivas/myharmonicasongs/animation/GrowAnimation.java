package com.mivas.myharmonicasongs.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

/**
 * Animation for expand/collapse.
 */
public class GrowAnimation extends Animation {

    public final static int EXPAND = 0;
    public final static int COLLAPSE = 1;

    private View view;
    private int endWidth;
    private int endHeight;
    private int type;
    private LinearLayout.LayoutParams layoutParams;

    public GrowAnimation(View view, int duration, int type) {

        setDuration(duration);
        this.view = view;
        this.endWidth = view.getWidth();
        this.endHeight = view.getHeight();
        this.layoutParams = ((LinearLayout.LayoutParams) view.getLayoutParams());
        this.type = type;
        if(type == EXPAND) {
            layoutParams.width = 0;
            layoutParams.height = 0;
        } else {
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        }
        view.setVisibility(View.VISIBLE);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);

        if (interpolatedTime < 1.0f) {
            if (type == EXPAND) {
                layoutParams.width =  (int)(endWidth * interpolatedTime);
                layoutParams.height =  (int)(endHeight * interpolatedTime);
            } else {
                layoutParams.width = (int) (endWidth * (1 - interpolatedTime));
                layoutParams.height = (int) (endHeight * (1 - interpolatedTime));
            }
            view.requestLayout();
        } else {
            if (type == EXPAND) {
                layoutParams.width = endWidth;
                layoutParams.height = endHeight;
                view.requestLayout();
            } else {
                view.setVisibility(View.GONE);
            }
        }
    }

    public void setWidth(int endWidth) {
        this.endWidth = endWidth;
    }

    public void setHeight(int endHeight) {
        this.endHeight = endHeight;
    }
}
