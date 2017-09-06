package com.mivas.myharmonicasongs.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

/**
 * Animation for expand/collapse.
 */
public class GrowAnimation extends Animation {

    public final static int EXPAND = 0;
    public final static int COLLAPSE = 1;

    private View view;
    private int endHeight;
    private int endWidth;
    private int type;
    private RelativeLayout.LayoutParams layoutParams;

    public GrowAnimation(View view, int duration, int type) {

        setDuration(duration);
        this.view = view;
        endHeight = this.view.getHeight();
        endWidth = this.view.getWidth();
        layoutParams = ((RelativeLayout.LayoutParams) view.getLayoutParams());
        this.type = type;
        if(this.type == EXPAND) {
            layoutParams.height = 0;
            layoutParams.width = 0;
        } else {
            layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        }
        view.setVisibility(View.VISIBLE);
    }

    public int getHeight(){
        return view.getHeight();
    }

    public void setHeight(int height){
        endHeight = height;
    }

    public int getWidth() {
        return endWidth;
    }

    public void setWidth(int endWidth) {
        this.endWidth = endWidth;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {

        super.applyTransformation(interpolatedTime, t);
        if (interpolatedTime < 1.0f) {
            if (type == EXPAND) {
                layoutParams.height =  (int)(endHeight * interpolatedTime);
                layoutParams.width =  (int)(endWidth * interpolatedTime);
            } else {
                layoutParams.height = (int) (endHeight * (1 - interpolatedTime));
                layoutParams.width = (int) (endWidth * (1 - interpolatedTime));
            }
            view.requestLayout();
        } else {
            if (type == EXPAND) {
                layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                layoutParams.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                view.requestLayout();
            } else {
                view.setVisibility(View.GONE);
            }
        }
    }
}
