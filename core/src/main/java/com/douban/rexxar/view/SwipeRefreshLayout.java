package com.douban.rexxar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.douban.rexxar.R;
import com.douban.rexxar.utils.AppContext;

public class SwipeRefreshLayout extends android.support.v4.widget.SwipeRefreshLayout {

    private static final int DEFAULT_CIRCLE_TARGET = 64;

    private int mTouchSlop;
    private float mPrevX;

    public SwipeRefreshLayout(Context context) {
        super(context);
        init(context);
    }

    public SwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mTouchSlop = ViewConfiguration.get(context)
                .getScaledTouchSlop();
        setMainColor(getResources().getColor(R.color.green));
    }

    public void setMainColor(int color) {
        setColorSchemeColors(color, getContext().getResources().getColor(android.R.color.transparent), color, getResources().getColor(android.R.color.transparent));
    }

    public void setMainColorRes(int colorRes) {
        setMainColor(AppContext.getInstance().getResources().getColor(colorRes));
    }

    // adapted from http://stackoverflow.com/questions/23989910/horizontalscrollview-inside-swiperefreshlayout
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = MotionEvent.obtain(event)
                        .getX();
                break;

            case MotionEvent.ACTION_MOVE:
                final float eventX = event.getX();
                float xDiff = Math.abs(eventX - mPrevX);

                if (xDiff > mTouchSlop) {
                    return false;
                }
        }
        return super.onInterceptTouchEvent(event);
    }

    /**
     * 是否启用下拉刷新
     *
     * Note: 如果使用setEnable(),嵌套滚动事件将无法向父级传递
     * @param enable
     */
    public void enableRefresh(boolean enable) {
        if (enable) {
            setDistanceToTriggerSync((int) (DEFAULT_CIRCLE_TARGET * getResources().getDisplayMetrics().density));
        } else {
            setDistanceToTriggerSync(Integer.MAX_VALUE);
        }
    }

}
