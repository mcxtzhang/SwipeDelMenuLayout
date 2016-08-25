package mcxtzhang.swipedelmenu.view;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

/**
 * 继承自ViewGroup，实现滑动出现删除等选项的效果，
 * 思路：跟随手势将第一个item向左滑动，
 * 在onMeasure时 将第一个Item设为屏幕宽度
 * Created by zhangxutong .
 * Date: 16/04/24
 */
@Deprecated
public class SwipeMenuItemViewGroup extends ViewGroup {
    private static final String TAG = "zxt";

    private int mMaxVelocity;
    private int mPointerId;

    public SwipeMenuItemViewGroup(Context context) {
        this(context, null);
    }

    public SwipeMenuItemViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeMenuItemViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mScreenH = getResources().getDisplayMetrics().heightPixels;
        mScreenW = getResources().getDisplayMetrics().widthPixels;
        mMaxVelocity = ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity();
        Log.d(TAG, "init() called with: " + "mScreenW:" + mScreenW);
    }

    private int mHeight;
    private int mScreenH, mScreenW;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure() called with: " + "widthMeasureSpec = [" + widthMeasureSpec + "], heightMeasureSpec = [" + heightMeasureSpec + "]");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            mHeight = Math.max(mHeight, childView.getMeasuredHeight());
        }
        LayoutParams params = getLayoutParams();
        params.height = mHeight;
        params.width = mScreenW;
        setLayoutParams(params);
    }

    int childTotalWidth = 0;


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout() called with: " + "changed = [" + changed + "], l = [" + l + "], t = [" + t + "], r = [" + r + "], b = [" + b + "]");
        int childCount = getChildCount();
        int left = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (i == 0) {
                childView.layout(left, getPaddingTop(), left + mScreenW, getPaddingTop() + childView.getMeasuredHeight());
                left = left + mScreenW;
            } else {
                childView.layout(left, getPaddingTop(), left + childView.getMeasuredWidth(), getPaddingTop() + childView.getMeasuredHeight());
                left = left + childView.getMeasuredWidth();
            }
        }
        childTotalWidth = left;
        maxScrollGap = childTotalWidth - mScreenW;
    }

    //上一次的xy
    private PointF mLastP = new PointF();
    //最大滑动距离
    private int maxScrollGap;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(TAG, "dispatchTouchEvent() called with: " + "ev = [" + ev + "]");
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastP.set(ev.getRawX(), ev.getRawY());
                break;
            case MotionEvent.ACTION_MOVE:
                float gap = mLastP.x - ev.getRawX();
                if (gap > ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                scrollBy((int) (gap), 0);
                //修正
                if (getScrollX() < 0) {
                    scrollTo(0, 0);
                }

                if (getScrollX() > maxScrollGap) {
                    scrollTo(maxScrollGap, 0);
                }
                mLastP.set(ev.getRawX(), ev.getRawY());
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    //存储的是当前正在展开的View
    private static View ViewCache;


    private VelocityTracker mVelocityTracker;//生命变量

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent() called with: " + "event = [" + event + "]");
        acquireVelocityTracker(event);
        final VelocityTracker verTracker = mVelocityTracker;


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "onTouch A ACTION_DOWN");
                //如果down，view和cacheview不一样，则立马让他还原。且把它置为null
                if (ViewCache != null && ViewCache != this) {
                    ViewCache.scrollTo(0, 0);
                    ViewCache = null;
                }

                //求第一个触点的id， 此时可能有多个触点，但至少一个
                mPointerId = event.getPointerId(0);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "onTouch A ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //求伪瞬时速度
                verTracker.computeCurrentVelocity(1000, mMaxVelocity);
                final float velocityX = verTracker.getXVelocity(mPointerId);
                final float velocityY = verTracker.getYVelocity(mPointerId);
                if (Math.abs(velocityX) > 1000) {
                    if (velocityX < -1000) {
                        scrollTo(maxScrollGap, 0);
                        //展开就加入ViewCache：
                        ViewCache = this;
                    } else/* if (velocityX > 1000) {
                        scrollTo(0, 0);
                    }else */ {
                        scrollTo(0, 0);
                    }
                } else {
                    if (getScrollX() > maxScrollGap / 2) {
                        scrollTo(maxScrollGap, 0);
                        //展开就加入ViewCache：
                        ViewCache = this;
                    } else {
                        scrollTo(0, 0);
                    }
                }


                //释放
                releaseVelocityTracker();
                Log.i(TAG, "onTouch A ACTION_UP ACTION_CANCEL:velocityY:" + velocityX);
                break;
            default:
                Log.i(TAG, "onTouch A default:" + event);
                break;
        }
        return true;
    }


    /**
     * @param event 向VelocityTracker添加MotionEvent
     * @see android.view.VelocityTracker#obtain()
     * @see android.view.VelocityTracker#addMovement(MotionEvent)
     */
    private void acquireVelocityTracker(final MotionEvent event) {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * &nbsp; &nbsp; &nbsp;* 释放VelocityTracker
     * &nbsp; &nbsp; &nbsp;*
     * &nbsp; &nbsp; &nbsp;* @see android.view.VelocityTracker#clear()
     * &nbsp; &nbsp; &nbsp;* @see android.view.VelocityTracker#recycle()
     * &nbsp; &nbsp; &nbsp;
     */
    private void releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private static final String sFormatStr = "velocityX=%f\nvelocityY=%f";

}
