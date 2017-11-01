package com.mivas.myharmonicasongs.animation;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

import com.mivas.myharmonicasongs.listener.CellAnimationListener;
import com.mivas.myharmonicasongs.model.Cell;
import com.mivas.myharmonicasongs.model.CellLine;

/**
 * Animation for expand/collapse.
 */
public class CellAnimation extends Animation {

    public final static int EXPAND = 0;
    public final static int COLLAPSE = 1;

    private View view;
    private int endWidth;
    private int endHeight;
    private int type;
    private CellAnimationListener listener;
    private LinearLayout.LayoutParams layoutParams;

    public CellAnimation(View view, int type) {

        setDuration(300);
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
                if (listener != null) {
                    listener.onAnimationEnded();
                }
            }
        }
    }

    public void setWidth(int endWidth) {
        this.endWidth = endWidth;
    }

    public void setHeight(int endHeight) {
        this.endHeight = endHeight;
    }

    public void setListener(CellAnimationListener listener) {
        this.listener = listener;
    }
}
