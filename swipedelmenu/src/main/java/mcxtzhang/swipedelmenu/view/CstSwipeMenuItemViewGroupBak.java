package mcxtzhang.swipedelmenu.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Scroller;

/**
 * 继承自ViewGroup，实现滑动出现删除等选项的效果，
 * 思路：跟随手势将第一个item向左滑动，
 * 在onMeasure时 将第一个Item设为屏幕宽度
 * 平滑滚动使用的是Scroller,20160811，最新平滑滚动又用属性动画做了，因为这样更酷炫
 * Created by zhangxutong .
 * Date: 16/04/24
 */
@Deprecated
public class CstSwipeMenuItemViewGroupBak extends ViewGroup {
    private static final String TAG = "zxt";

    private boolean isSwipeEnable = true;//右滑删除的开关,默认开

    private int mMaxVelocity;
    private int mPointerId;
    private android.util.Log LogUtils;

    public CstSwipeMenuItemViewGroupBak(Context context) {
        this(context, null);
    }

    public CstSwipeMenuItemViewGroupBak(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CstSwipeMenuItemViewGroupBak(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public boolean isSwipeEnable() {
        return isSwipeEnable;
    }

    public void setSwipeEnable(boolean swipeEnable) {
        isSwipeEnable = swipeEnable;
    }

    private void init(Context context) {
        mScreenH = getResources().getDisplayMetrics().heightPixels;
        mScreenW = getResources().getDisplayMetrics().widthPixels;
        mMaxVelocity = ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity();
        //初始化滑动帮助类对象
        mScroller = new Scroller(context);
    }

    private int mHeight;
    private int mScreenH, mScreenW;

    /**
     * 右侧菜单宽度总和
     */
    private int mRightMenuWidths;
    /**
     * 滑动判定临界值（右侧菜单宽度的40%）
     */
    private int mLimit;

    private Scroller mScroller;

    /**
     * 子View一共的宽度
     */
    private int childTotalWidth;
    /**
     * 最大滑动距离(右侧菜单宽度总和)
     */
    private int maxScrollGap;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //Log.d(TAG, "onMeasure() called with: " + "widthMeasureSpec = [" + widthMeasureSpec + "], heightMeasureSpec = [" + heightMeasureSpec + "]");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mRightMenuWidths = 0;//由于ViewHolder的复用机制，每次这里要手动恢复初始值
        int childCount = getChildCount();

        //add by 2016 08 11 子View能matchParent
        final boolean measureMatchParentChildren = MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY;
        boolean isNeedMeasureChildHeight = false;


        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() != GONE) {
                //measureChild(childView, widthMeasureSpec, heightMeasureSpec);
                measureChildWithMargins(childView, widthMeasureSpec, 0, heightMeasureSpec, 0);
                final MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();

                mHeight = Math.max(mHeight, childView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                if (measureMatchParentChildren && lp.height == LayoutParams.MATCH_PARENT) {
                    isNeedMeasureChildHeight = true;
                }

                if (i > 0) {//第一个布局是Left item，从第二个开始才是RightMenu
                    mRightMenuWidths += childView.getMeasuredWidth();
                }
            }
        }
        setMeasuredDimension(mScreenW, mHeight);
        mLimit = mRightMenuWidths * 4 / 10;
        //Log.d(TAG, "onMeasure() called with: " + "mRightMenuWidths = [" + mRightMenuWidths);

        if (isNeedMeasureChildHeight) {
            forceUniformHeight(childCount, widthMeasureSpec);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    /**
     * 给MatchParent的子View设置高度
     *
     * @param count
     * @param widthMeasureSpec
     */
    private void forceUniformHeight(int count, int widthMeasureSpec) {
        // Pretend that the linear layout has an exact size. This is the measured height of
        // ourselves. The measured height should be the max height of the children, changed
        // to accommodate the heightMeasureSpec from the parent
        int uniformMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(),
                MeasureSpec.EXACTLY);//以父布局高度构建一个Exactly的测量参数
        for (int i = 0; i < count; ++i) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

                if (lp.height == LayoutParams.MATCH_PARENT) {
                    // Temporarily force children to reuse their old measured width
                    // FIXME: this may not be right for something like wrapping text?
                    int oldWidth = lp.width;//measureChildWithMargins 这个函数会用到宽，所以要保存一下
                    lp.width = child.getMeasuredWidth();

                    // Remeasure with new dimensions
                    measureChildWithMargins(child, widthMeasureSpec, 0, uniformMeasureSpec, 0);
                    lp.width = oldWidth;
                }
            }
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        LogUtils.d(TAG, "onLayout() called with: " + "changed = [" + changed + "], l = [" + l + "], t = [" + t + "], r = [" + r + "], b = [" + b + "]");
        int childCount = getChildCount();
        int left = l;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() != GONE) {
                if (i == 0) {
                    childView.layout(left, getPaddingTop(), left + mScreenW, getPaddingTop() + childView.getMeasuredHeight());
                    left = left + mScreenW;
                } else {
                    childView.layout(left, getPaddingTop(), left + childView.getMeasuredWidth(), getPaddingTop() + childView.getMeasuredHeight());
                    left = left + childView.getMeasuredWidth();
                }
            }
        }
        childTotalWidth = left;
        maxScrollGap = childTotalWidth - mScreenW;
        //Log.d(TAG, "onLayout() called with: " + "maxScrollGap = [" + maxScrollGap + "], l = [" + l + "], t = [" + t + "], r = [" + r + "], b = [" + b + "]");
    }

    //上一次的xy
    private PointF mLastP = new PointF();

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogUtils.d(TAG, "dispatchTouchEvent() called with: " + "ev = [" + ev + "]");
        if (isSwipeEnable) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mLastP.set(ev.getRawX(), ev.getRawY());
                    break;
                case MotionEvent.ACTION_MOVE:
                    float gap = mLastP.x - ev.getRawX();
                    //为了在水平滑动中禁止父类ListView等再竖直滑动
                    if (gap > ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    //如果scroller还没有滑动结束 停止滑动动画
                    if (!mScroller.isFinished()) {
                        mScroller.abortAnimation();
                    }
                    scrollBy((int) (gap), 0);
                    //修正
                    if (getScrollX() < 0) {
                        scrollTo(0, 0);
                    }

                    if (getScrollX() > mRightMenuWidths) {
                        scrollTo(mRightMenuWidths, 0);
                    }
                    mLastP.set(ev.getRawX(), ev.getRawY());
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                default:
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    //存储的是当前正在展开的View
    private static CstSwipeMenuItemViewGroupBak ViewCache;


    private VelocityTracker mVelocityTracker;//生命变量

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtils.d(TAG, "onTouchEvent() called with: " + "event = [" + event + "]");
        if (isSwipeEnable) {
            acquireVelocityTracker(event);
            final VelocityTracker verTracker = mVelocityTracker;


            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    LogUtils.i(TAG, "onTouch A ACTION_DOWN");

                    break;
                case MotionEvent.ACTION_MOVE:
                    LogUtils.i(TAG, "onTouch A ACTION_MOVE");
                    break;

                default:
                    LogUtils.i(TAG, "onTouch A default:" + event);
                    break;
            }
        }

        return super.onTouchEvent(event);
    }

    /**
     * 平滑展开
     */
    public void smoothExpand() {
        /*mScroller.startScroll(getScrollX(), 0, mRightMenuWidths - getScrollX(), 0);
        invalidate();*/
        ValueAnimator valueAnimator = ValueAnimator.ofInt(getScrollX(), mRightMenuWidths);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollTo((Integer) animation.getAnimatedValue(), 0);
            }
        });
        valueAnimator.setInterpolator(new OvershootInterpolator());
        valueAnimator.setDuration(300).start();
    }

    /**
     * 平滑关闭
     */
    public void smoothClose() {
/*        mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0);
        invalidate();*/

        ValueAnimator valueAnimator = ValueAnimator.ofInt(getScrollX(), 0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollTo((Integer) animation.getAnimatedValue(), 0);
            }
        });
        valueAnimator.setInterpolator(new AnticipateInterpolator());
        valueAnimator.setDuration(300).start();

        LogUtils.d(TAG, "smoothClose() called with:getScrollX() " + getScrollX());
    }


    /**
     * @param event 向VelocityTracker添加MotionEvent
     * @see VelocityTracker#obtain()
     * @see VelocityTracker#addMovement(MotionEvent)
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


    //防止内存泄露 在每个view呗attach的时候 计数+1，在detach的时候判断  count=0 说明全部移除屏幕  将static的viewcache置为null
    private static int mAttachViewCount = 0;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAttachViewCount += 1;
        LogUtils.d("TAG", "onAttachedToWindow() called with: mAttachViewCount " + mAttachViewCount);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAttachViewCount -= 1;
        LogUtils.d("TAG", "onDetachedFromWindow() called with:  mAttachViewCount" + mAttachViewCount);
    }

    //平滑滚动
    @Override
    public void computeScroll() {
        //判断Scroller是否执行完毕：
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            //通知View重绘-invalidate()->onDraw()->computeScroll()
            invalidate();
        }
    }

}
