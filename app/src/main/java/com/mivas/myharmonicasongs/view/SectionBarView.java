package com.mivas.myharmonicasongs.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mivas.myharmonicasongs.database.model.DbSection;
import com.mivas.myharmonicasongs.listener.SectionBarListener;
import com.mivas.myharmonicasongs.model.CellLine;
import com.mivas.myharmonicasongs.util.CustomizationUtils;
import com.mivas.myharmonicasongs.util.DimensionUtils;

import java.util.List;

public class SectionBarView extends LinearLayout {

    private Context context;
    private List<CellLine> cellLines;
    private SectionBarListener listener;

    private int textStyle;
    private int textColor;
    private int textBackground;
    private int height;
    private int textSize;
    private int dp1;
    private int dp2;

    public SectionBarView(Context context) {
        super(context);
        this.context = context;
        initCustomizations();
        initViews();
        initListeners();
    }

    public SectionBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initCustomizations();
        initViews();
        initListeners();
    }

    public SectionBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initCustomizations();
        initViews();
        initListeners();
    }

    private void initViews() {
        setOrientation(HORIZONTAL);
        dp1 = DimensionUtils.dpToPx(context, 1);
        dp2 = DimensionUtils.dpToPx(context, 2);
    }

    private void initListeners() {

    }

    public void initialize() {
        removeAllViews();
        for (final CellLine cellLine : cellLines) {
            if (cellLine.getSectionCell() != null && cellLine.getSectionCell().getDbSection() != null) {
                DbSection dbSection = cellLine.getSectionCell().getDbSection();
                TextView textView = new TextView(context);
                LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(0, DimensionUtils.dpToPx(context, CustomizationUtils.getSectionBarHeightValue(height)));
                textLayoutParams.setMargins(dp1, 0, dp1, dp2);
                textLayoutParams.weight = 1;
                textView.setLayoutParams(textLayoutParams);
                textView.setPadding(dp2, 0, dp2, 0);
                CustomizationUtils.styleSectionBarText(textView, textStyle, CustomizationUtils.createSectionBarTextColor(context, textColor));
                textView.setBackground(CustomizationUtils.createSectionBarBackground(context, textBackground));
                textView.setText(dbSection.getName());
                textView.setTextSize(CustomizationUtils.getSectionBarTextSizeValue(textSize));
                textView.setMaxLines(1);
                textView.setGravity(Gravity.CENTER);
                textView.setClickable(true);
                textView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        listener.onSectionSelected(cellLine);
                    }
                });

                addView(textView);
            }
        }
    }

    public void setCellLines(List<CellLine> cellLines) {
        this.cellLines = cellLines;
    }

    public void setListener(SectionBarListener listener) {
        this.listener = listener;
    }

    public void initCustomizations() {
        textStyle = CustomizationUtils.getSectionBarStyle();
        textColor = CustomizationUtils.getSectionBarTextColor();
        textBackground = CustomizationUtils.getSectionBarBackground();
        height = CustomizationUtils.getSectionBarHeight();
        textSize = CustomizationUtils.getSectionBarTextSize();
    }
}
