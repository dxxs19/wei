package com.wei.applications.ui.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.wei.applications.R;
import com.wei.applications.utils.LogUtil;

/**
 * Created by WEI on 2016/6/16.
 */
 public class CircleView extends View
{
    private final String TAG = getClass().getSimpleName();
    int mColor = Color.RED;
    Paint mPaint = null;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        mColor = typedArray.getColor(R.styleable.CircleView_circle_color, Color.GRAY);
        typedArray.recycle();
        initView();
    }

    private void initView()
    {
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST  && heightSpecMode == MeasureSpec.AT_MOST)
        {
            setMeasuredDimension(200, 200);
        }
        else if (widthSpecMode == MeasureSpec.AT_MOST)
        {
            setMeasuredDimension(200, heightSpecSize);
        }
        else if (heightSpecMode == MeasureSpec.AT_MOST)
        {
            setMeasuredDimension(widthSpecSize, 200 );
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        float paddingLeft = getPaddingLeft();
        float paddingRight = getPaddingRight();
        float paddingTop = getPaddingTop();
        float paddingBottom = getPaddingBottom();

        float width = getWidth() - paddingLeft - paddingRight;
        float height = getHeight() - paddingTop - paddingBottom;

        float radius = Math.min(width, height)/2;
        canvas.drawCircle(width/2 + paddingLeft, height/2 + paddingTop, radius, mPaint);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        Log.e(TAG, "--- dispatchTouchEvent ---");
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        Log.e(TAG, "--- onTouchEvent ---");
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                LogUtil.d(TAG, "--- ACTION_DOWN ---");
                break;

            case MotionEvent.ACTION_MOVE:
                LogUtil.d(TAG, "--- ACTION_MOVE ---");
                break;

            case MotionEvent.ACTION_UP:
                LogUtil.d(TAG, "--- ACTION_UP ---");
                break;
        }
        return super.onTouchEvent(event);
//        return true;
    }
}
