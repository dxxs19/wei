package com.wei.applications.ui.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.wei.applications.utils.LogUtil;

/**
 * Created by WEI on 2016/7/4.
 */
public class SurfaceViewTemplate extends SurfaceView implements SurfaceHolder.Callback, Runnable
{
    private final String TAG = getClass().getSimpleName();
    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    private boolean mIsDrawing;
    private Path mPath;
    private Paint mPaint;
    private int x, y;

    public SurfaceViewTemplate(Context context) {
        this(context, null);
    }

    public SurfaceViewTemplate(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SurfaceViewTemplate(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SurfaceViewTemplate(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        LogUtil.d(TAG, "--- surface 创建完毕 ---");
        mIsDrawing = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        LogUtil.d(TAG, "--- surfaceChanged --- " + "format, width, height : " + format + ", " + width + ", " + height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        LogUtil.d(TAG, "--- surfaceDestroyed ---");
        mIsDrawing = false;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        while (mIsDrawing)
        {
            // 画正弦函数
            x += 1;
            y = (int) (100 * Math.sin(x * 2 * Math.PI / 180) + 400);
            mPath.lineTo(x,y);
            drawView();
        }
        long end = System.currentTimeMillis();
        if (end - start < 100)
        {
            try {
                Thread.sleep(100-(end - start));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 画板
//    @Override
//    public boolean onTouchEvent(MotionEvent event)
//    {
//        float x = event.getX();
//        float y = event.getY();
//        switch (event.getAction())
//        {
//            case MotionEvent.ACTION_DOWN:
//                mPath.moveTo(x,y);
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//                mPath.lineTo(x, y);
//                break;
//        }
//        return true;
//    }

    private void initView()
    {
        mHolder = getHolder();
        mHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);

        // 初始化path
        mPath = new Path();

        // 初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
    }

    private void drawView()
    {
        try {
            mCanvas = mHolder.lockCanvas();
            // 业务实现
            mCanvas.drawColor(Color.WHITE);
            mCanvas.drawPath(mPath, mPaint);
        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            if (mCanvas != null)
            {
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

}
