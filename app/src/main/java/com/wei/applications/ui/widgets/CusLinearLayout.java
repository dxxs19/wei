package com.wei.applications.ui.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.wei.applications.utils.LogUtil;

/**
 * Created by WEI on 2016/6/29.
 */
public class CusLinearLayout extends LinearLayout
{
    private final String TAG = getClass().getSimpleName();

    public CusLinearLayout(Context context) {
        super(context);
    }

    public CusLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public CusLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CusLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        Log.e(TAG, "--- dispatchTouchEvent ---");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        Log.e(TAG, "--- onInterceptTouchEvent ---");
        // 其实就是返回false，看源码！表示不拦截
        return super.onInterceptTouchEvent(ev);
//        return true;
    }

    int lastX, lastY;
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        Log.e(TAG, "--- onTouchEvent ---");
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                LogUtil.d(TAG, "--- ACTION_DOWN ---");
                lastX = (int) event.getX();
                lastY = (int) event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                LogUtil.d(TAG, "--- ACTION_MOVE ---");
                int curX = (int) event.getX();
                int curY = (int) event.getY();

                int offsetX = curX - lastX;
                int offsetY = curY - lastY;

                break;

            case MotionEvent.ACTION_UP:
                LogUtil.d(TAG, "--- ACTION_UP ---");
                break;
        }
        return super.onTouchEvent(event);
//        return true;
    }
}
