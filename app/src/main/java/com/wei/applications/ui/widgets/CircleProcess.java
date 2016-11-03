package com.wei.applications.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.wei.applications.R;

/**
 * TODO: document your custom view class.
 */
public class CircleProcess extends View
{
    private final String TAG = getClass().getSimpleName();
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private Paint mPaint, mFillPaint;
    private int mWidth;
    private int mHeight;
    private int mSpacing = 0, radius = 0;
    private float sweepAngle;

    public CircleProcess(Context context) {
        super(context);
        init(null, 0);
    }

    public CircleProcess(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CircleProcess(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CircleProcess, defStyle, 0);

        mExampleString = a.getString(
                R.styleable.CircleProcess_circle_string);
        mExampleColor = a.getColor(
                R.styleable.CircleProcess_circle_process_color,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.CircleProcess_circle_dimension,
                mExampleDimension);

        mSpacing = (int) a.getDimension(R.styleable.CircleProcess_circle_space, 1);

        if (a.hasValue(R.styleable.CircleProcess_circle_drawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.CircleProcess_circle_drawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(2);

        mFillPaint = new Paint();
        mFillPaint.setAntiAlias(true);
        mFillPaint.setColor(Color.GRAY);
        mFillPaint.setStyle(Paint.Style.FILL);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (sweepAngle <= 360)
                {
                    sweepAngle += 6;
                    postInvalidate();
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        Log.e(TAG, "--- onMeasure ---");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getWidth();
        mHeight = getHeight();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int contentWidth = mWidth - paddingLeft - paddingRight;
        int contentHeight = mHeight - paddingTop - paddingBottom;
        radius = Math.min(contentWidth, contentHeight)/2;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        Log.e(TAG, "--- onDraw ---");
        super.onDraw(canvas);
        canvas.drawCircle(mWidth/2, mHeight/2, radius, mPaint);

        Shader shader = new LinearGradient(0, 0, 100, 100, new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.LTGRAY},
                null, Shader.TileMode.REPEAT);
        mFillPaint.setShader(shader);

        canvas.save();
        RectF rectF = new RectF(mWidth/2-radius + mSpacing , mHeight/2-radius + mSpacing, mWidth/2 + radius - mSpacing, mHeight/2 + radius - mSpacing);
        canvas.drawArc(rectF, 270, sweepAngle, true, mFillPaint);
        canvas.restore();
//        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher), 0, 0, mPaint);
    }

}
