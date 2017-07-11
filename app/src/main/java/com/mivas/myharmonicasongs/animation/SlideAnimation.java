package com.mivas.myharmonicasongs.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

public class SlideAnimation extends Animation {

    public final static int COLLAPSE = 1;
    public final static int EXPAND = 0;

    private View view;
    private int endHeight;
    private int type;
    private LinearLayout.LayoutParams layoutParams;

    public SlideAnimation(View view, int duration, int type) {

        setDuration(duration);
        this.view = view;
        endHeight = this.view.getHeight();
        layoutParams = ((LinearLayout.LayoutParams) view.getLayoutParams());
        this.type = type;
        if(this.type == EXPAND) {
            layoutParams.height = 0;
        } else {
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        }
        view.setVisibility(View.VISIBLE);
    }

    public int getHeight(){
        return view.getHeight();
    }

    public void setHeight(int height){
        endHeight = height;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {

        super.applyTransformation(interpolatedTime, t);
        if (interpolatedTime < 1.0f) {
            if (type == EXPAND) {
                layoutParams.height =  (int)(endHeight * interpolatedTime);
            } else {
                layoutParams.height = (int) (endHeight * (1 - interpolatedTime));
            }
            view.requestLayout();
        } else {
            if (type == EXPAND) {
                layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                view.requestLayout();
            } else {
                view.setVisibility(View.GONE);
            }
        }
    }
}
