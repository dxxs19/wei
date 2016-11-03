package com.wei.applications.ui.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.wei.applications.R;
import com.wei.applications.utils.LogUtil;

import java.util.Calendar;

/**
 * 自定义时钟
 * Created by WEI on 2016/7/4.
 */
public class ClockView extends View
{
    private final String TAG = getClass().getSimpleName();
    private int BIG_SCALE, SMALL_SCALE;
    private int bigTxt, smallTxt;
    private int hour, minute, second;
    private int width , height;
    private int hourHandLen, minuteHandLen, secondHandLen;
    private Calendar mCalendar;

    public ClockView(Context context) {
        this(context, null);
    }

    public ClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getSize(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ClockView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        getSize(context, attrs, defStyleAttr);
    }

    private void getSize(Context context, AttributeSet attrs, int defStyleAttr)
    {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClockView);
        width = (int) typedArray.getDimension(R.styleable.ClockView_clock_width, 0);
        height = (int) typedArray.getDimension(R.styleable.ClockView_clock_height, 0);
        BIG_SCALE = width/8;
        SMALL_SCALE = width/16;
        bigTxt = width/10;
        smallTxt = width/16;
        LogUtil.d(TAG, "width : " + width + ", height : " + height);
    }

    private final int NEED_UPDATE = 0x011;
    Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case NEED_UPDATE:
                    mCalendar = Calendar.getInstance();
                    hour = mCalendar.get(Calendar.HOUR);
                    minute = mCalendar.get(Calendar.MINUTE);
                    second = mCalendar.get(Calendar.SECOND);
                    LogUtil.d(TAG, "当前时间： " + hour + ":" + minute + ":" + second);
                    invalidate();
                    mHandler.sendEmptyMessageDelayed(NEED_UPDATE, 1000);
                    break;
            }
        }
    };

    // 如果不是精确值模式(EXACTLY)，即指定宽高或者match_parent，则该方法需重写。因为onMeasure默认只实现EXACTLY模式
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
            // 设置wrap_content对应的宽、高
            setMeasuredDimension(width, height);
        }
        else if (widthSpecMode == MeasureSpec.AT_MOST)
        {
            setMeasuredDimension(width, heightSpecSize);
        }
        else if (heightSpecMode == MeasureSpec.AT_MOST)
        {
            setMeasuredDimension(widthSpecSize, height );
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        LogUtil.d(TAG, "--- onDraw ---");
        super.onDraw(canvas);

        // 1. 画外表盘(大圆圈)
        Paint paintCircle = new Paint();
        paintCircle.setAntiAlias(true);
        paintCircle.setColor(Color.BLUE);
        paintCircle.setStyle(Paint.Style.STROKE);
        paintCircle.setStrokeWidth(5);
        int radius = Math.min(width, height)/2;
        canvas.drawCircle(width/2, height/2, radius, paintCircle);

        // 2. 画刻度线
        Paint paintDegree = new Paint();
        paintDegree.setAntiAlias(true);
        paintDegree.setColor(Color.RED);
        paintDegree.setStyle(Paint.Style.FILL);
        for ( int i = 0 ; i < 12 ; i ++ )
        {
            if (i % 3 == 0)
            { // 对应3，6，9，12点钟位置
                paintDegree.setStrokeWidth(8);
                paintDegree.setTextSize(bigTxt);
                canvas.drawLine(width/2, height/2 - radius, width/2, height/2 - radius + SMALL_SCALE, paintDegree);
                String degree = String.valueOf(i);
                if (i == 0)
                {
                    degree = "12";
                }
                canvas.drawText(degree, width/2 - paintDegree.measureText(degree)/2, height/2 - radius + 2 * SMALL_SCALE + 10, paintDegree);
            }
            else
            {
                paintDegree.setStrokeWidth(3);
                paintDegree.setTextSize(smallTxt);
                canvas.drawLine(width/2, height/2 - radius, width/2, height/2 - radius + SMALL_SCALE, paintDegree);
                String degree = String.valueOf(i);
                canvas.drawText(degree, width/2 - paintDegree.measureText(degree)/2, height/2 - radius + 2 * SMALL_SCALE, paintDegree);
            }
            canvas.rotate(360/12, width/2, height/2);
        }

        Paint paintHour = new Paint();
        paintHour.setStrokeWidth(20);
        paintHour.setAntiAlias(true);

        Paint paintMinute = new Paint();
        paintMinute.setStrokeWidth(10);
        paintMinute.setAntiAlias(true);

        Paint paintSecond = new Paint();
        paintSecond.setStrokeWidth(5);
        paintSecond.setColor(Color.GREEN);
        paintSecond.setAntiAlias(true);

        // 根据View大小给出时针、分针、秒针的长度
        hourHandLen = radius/2;
        minuteHandLen = radius*5/8;
        secondHandLen = radius*7/8;

        float hourDegree = ((hour * 60 + minute) / 12f / 60) * 360;
        canvas.save();
        canvas.rotate(hourDegree, width/2, height/2);
        canvas.drawLine(width/2, height/2, width/2, height/2 - hourHandLen, paintHour);
        canvas.restore();

        float minuteDegree = (minute / 60f) * 360;
        canvas.save();
        canvas.rotate(minuteDegree, width/2, height/2);
        canvas.drawLine(width/2, height/2, width/2, height/2 - minuteHandLen, paintMinute);
        canvas.restore();

        // 得到秒针旋转的角度
        float secDegree = (second / 60f) * 360;
        canvas.save();
        canvas.rotate(secDegree, width/2, height/2);
        canvas.drawLine(width/2, height/2, width/2, height/2 - secondHandLen, paintSecond);
        canvas.restore();

        canvas.save();
        paintSecond.setColor(Color.RED);
        canvas.drawCircle(width/2, height/2, radius/20, paintSecond);
        canvas.restore();
    }

    @Override
    protected void onAttachedToWindow()
    {
        LogUtil.d(TAG, "--- onAttachedToWindow ---");
        super.onAttachedToWindow();
        mHandler.sendEmptyMessage(NEED_UPDATE);
    }

    @Override
    protected void onDetachedFromWindow() {
        LogUtil.d(TAG, "--- onDetachedFromWindow ---");
        super.onDetachedFromWindow();
        mHandler.removeMessages(NEED_UPDATE);
    }
}
