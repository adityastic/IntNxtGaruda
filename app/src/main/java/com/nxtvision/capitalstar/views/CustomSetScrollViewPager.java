package com.nxtvision.capitalstar.views;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomSetScrollViewPager extends ViewPager {
    private boolean canScroll = false;

    public CustomSetScrollViewPager(Context context) {
        super(context);
    }

    public CustomSetScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return canScroll && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return canScroll && super.onInterceptTouchEvent(ev);
    }
}
