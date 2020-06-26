package com.amlo.shopping.fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A Dynamic fit recycler used in card view in Landing page
 */
public class DynamicSpanFitRecycler extends RecyclerView {

    private int columnWidth = -1;
    private GridLayoutManager mgmr;

    public DynamicSpanFitRecycler(Context context) {
        super(context);
        spanFitter(context, null);
    }

    public DynamicSpanFitRecycler(Context context, AttributeSet attrs) {
        super(context, attrs);
        spanFitter(context, attrs);
    }

    public DynamicSpanFitRecycler(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        spanFitter(context, attrs);
    }

    private void spanFitter(Context context, AttributeSet attrs) {
        if (attrs != null) {
            int[] attrsArray = {
                    android.R.attr.columnWidth
            };
            TypedArray arrayVal = context.obtainStyledAttributes(attrs, attrsArray);
            columnWidth = arrayVal.getDimensionPixelSize(0, -1);
            arrayVal.recycle();
        }

        mgmr = new GridLayoutManager(getContext(), 1);
        setLayoutManager(mgmr);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        if (columnWidth > 0) {
            int countOfSpan = Math.max(1, getMeasuredWidth() / columnWidth);
            mgmr.setSpanCount(countOfSpan);
        }
    }
}