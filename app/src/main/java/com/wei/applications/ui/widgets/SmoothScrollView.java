package com.wei.applications.ui.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.wei.applications.utils.LogUtil;

/**
 * Created by WEI on 2016/6/14.
 */
public class SmoothScrollView extends View {
    public final String TAG = this.getClass().getSimpleName();
    Scroller mScroller = null;
    Context mContext = null;

    public SmoothScrollView(Context context) {
//        super(context);
        this(context, null);
    }

    public SmoothScrollView(Context context, AttributeSet attrs) {
//        super(context, attrs);
        this(context, attrs, 0);
    }

    public SmoothScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SmoothScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        initView();
    }

    private void initView() {
        mScroller = new Scroller(mContext);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e(TAG, "--- OnTouchListener ---");
                // 如返回true，则onTouchEvent(MotionEvent event)将不被执行，因为给View设置的OnTouchListener优先级比onTouchEvent高
                return false;
            }
        });
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "--- OnClickListener ---");
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.e(TAG, "--- dispatchTouchEvent ---");
        return super.dispatchTouchEvent(event);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        Log.e(TAG, "--- onInterceptTouchEvent ---");
//        return super.onInterceptTouchEvent(ev);
//    }
//
//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b)
//    {
//        Log.e(TAG, changed + ", " + l + ", " + t + ", " + r + ", " + b);
//        Log.e(TAG, "--- onLayout ---");
//    }


    @Override
    protected void onAttachedToWindow()
    {
        LogUtil.e(TAG, "--- onAttachedToWindow ---");
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow()
    {
        LogUtil.e(TAG, "--- onDetachedFromWindow ---");
        super.onDetachedFromWindow();
    }

    int lastX, lastY;
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        Log.e(TAG, "--- onTouchEvent ---");
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getX();
                lastY = (int) event.getY();
                Log.e(TAG, "lastX = " + lastX + ", lastY = " + lastY);
                break;

            case MotionEvent.ACTION_MOVE:
                int curX = (int) event.getX();
                int curY = (int) event.getY();

                int offsetX = curX - lastX;
                int offsetY = curY - lastY;

                // 方法一
//                layout( getLeft() + offsetX, getTop() + offsetY, getRight() + offsetX, getBottom() + offsetY);

                // 方法二：等价于layout();
                // 同时对left和right进行偏移
//                offsetLeftAndRight(offsetX);
//                // 同时对top和bottom进行偏移
//                offsetTopAndBottom(offsetY);

//                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
//                layoutParams.leftMargin = getLeft() + offsetX;
//                layoutParams.topMargin = getTop() + offsetY;
//                setLayoutParams(layoutParams);

                // 移动的是控件的内容而不是控件本身
                ((View)getParent()).scrollBy(-offsetX, -offsetY);

//                smoothScrollTo(curX, curY);
                Log.e(TAG, "lastX = " + lastX + ", lastY = " + lastY + "; curX = " + curX + ", curY = " + curY);
                LogUtil.e(TAG, "offsetX = " + offsetX + ", offsetY = " + offsetY);
                break;

            case MotionEvent.ACTION_UP:
                View viewGroup = (View) getParent();
                mScroller.startScroll(viewGroup.getScrollX(), viewGroup.getScrollY(), - viewGroup.getScrollX(), -viewGroup.getScrollY());
                invalidate();
                break;
        }
        return true;
    }

    private void smoothScrollTo(int destX, int destY) {
        LogUtil.e(TAG, "--- smoothScrollTo ---");
        int scrollX = getScrollX();
        int delta = destX - scrollX;
        // 1000ms内滑向destX,效果就是慢慢滑动
        mScroller.startScroll(scrollX, 0, delta, 0, 1000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        LogUtil.e(TAG, "--- computeScroll ---");
        super.computeScroll();
        if (mScroller.computeScrollOffset())
        {
            ((View)getParent()).scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }
}
