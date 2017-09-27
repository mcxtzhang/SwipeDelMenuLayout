package com.mcxtzhang.swipemenulib;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Intro: 解决 滑动冲突的 ViewPager
 * <p>
 * 因为ViewPager 和 SwipMenuLayout都是水平方向滑动的控件。
 * 所以在一起使用时会有冲突，
 * 使用本控件(CstViewPager)，可以在ViewPager的第一页使用左滑。在ViewPager的最后一页使用右滑菜单。
 * <p>
 * Author: zhangxutong
 * E-mail: mcxtzhang@163.com
 * Home Page: http://blog.csdn.net/zxt0601
 * Created:   2017/9/27.
 * History:
 */

public class CstViewPager extends ViewPager {
    private static final String TAG = "zxt/CstViewPager";

    private int mLastX, mLastY;


    public CstViewPager(Context context) {
        super(context);
    }

    public CstViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //Log.i(TAG, "onInterceptTouchEvent() called with: ev = [" + ev + "]");
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        boolean intercept = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (isHorizontalScroll(x, y)) {
                    //除了在 第一页的手指向右滑 ， 最后一页的左滑，其他时刻都是父控件需要拦截事件
                    if (isReactFirstPage() && isScrollRight(x)) {
                        //Log.e(TAG, "第一页的手指向右滑]");
                        intercept = false;
                    } else if (isReachLastPage() && isScrollLeft(x)) {
                        //Log.e(TAG, "最后一页的左滑");
                        intercept = false;
                    } else {
                        //Log.e(TAG, "其他情况");
                        intercept = true;
                    }

                } else {

                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }

        mLastX = x;
        mLastY = y;

        boolean onInterceptTouchEvent = super.onInterceptTouchEvent(ev);
        return intercept || onInterceptTouchEvent;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //Log.i(TAG, "onTouchEvent() called with: ev = [" + ev + "]");
        return super.onTouchEvent(ev);
    }

    //是否在水平滑动
    private boolean isHorizontalScroll(int x, int y) {
        return Math.abs(y - mLastY) < Math.abs(x - mLastX);
    }

    //是否未到达最后一页
    private boolean isReachLastPage() {
        PagerAdapter adapter = getAdapter();
        if (null != adapter && adapter.getCount() - 1 == getCurrentItem()) {
            return true;
        } else {
            return false;
        }
    }

    //是否在第一页
    private boolean isReactFirstPage() {
        if (getCurrentItem() == 0) {
            return true;
        } else {
            return false;
        }
    }

    //是否左滑
    private boolean isScrollLeft(int x) {
        return x - mLastX < 0;
    }

    private boolean isScrollRight(int x) {
        return x - mLastX > 0;
    }
}
