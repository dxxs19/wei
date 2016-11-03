package com.wei.applications.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.wei.applications.R;
import com.wei.applications.db.MyDatabaseHelper;
import com.wei.applications.ui.widgets.CircleView;
import com.wei.applications.utils.LogUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private final static String TAG = MainActivity.class.getSimpleName();
    private final static int CUR_VERSION = 3;
    private EditText content;
    private MyDatabaseHelper dbHelper;
    private CircleView mCircleView;
    private Button animBtn;
    private LinearLayout hideLl;
    private int mHideLayoutHeight = 0;
    private float mDensity;
    private float startX, startY, endX, endY;
    private ThreadLocal<Boolean> mThreadLocal = new ThreadLocal<>();
    private List<View> views = new ArrayList<>();
    private int[] resIds = {R.mipmap.ic_launcher, R.mipmap.mode1, R.mipmap.mode2, R.mipmap.mode3};
    private ViewStub mViewStub;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        content = (EditText) findViewById(R.id.edtTxt_input);
        mCircleView = (CircleView) findViewById(R.id.circleView);
        animBtn = (Button) findViewById(R.id.btn_anim);
        animBtn.setOnClickListener(this);
        dbHelper = new MyDatabaseHelper(this, "BookStore", null, CUR_VERSION);
        mThreadLocal.set(true);
        LogUtil.e(TAG, "[Thread#main] mThreadLocal = " + mThreadLocal.get());
        createAnims();

        mViewStub = (ViewStub) findViewById(R.id.viewStub);

        mDensity = getResources().getDisplayMetrics().density;
        mHideLayoutHeight = (int) (mDensity * 50 + 0.5);
        LogUtil.d(TAG, "屏幕密度 : " + mDensity + ", dpi : " + getResources().getDisplayMetrics().densityDpi);
    }

    private void createAnims()
    {
        views.clear();
        views.add(animBtn);
        views.add(findViewById(R.id.img1));
        views.add(findViewById(R.id.img2));
        views.add(findViewById(R.id.img3));
        views.add(findViewById(R.id.img4));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        content.setText(savedInstanceState.getString("save_data"));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("save_data", content.getText().toString());
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item)
    {
        Log.e(getClass().getSimpleName(), "-- onMenuItemSelected --");
        switch (item.getItemId())
        {
            case R.id.jump:
                // 不管有多少个action和category,只需要一个action及0到多个category和清单文件中的一致便可成功跳转!如果有data，则要匹配所有data
//                Intent intent = new Intent("com.wei.ACTIONSECOND");
//                intent.setDataAndType(Uri.parse("http://abc"), "video/abc");
//                startActivity(intent);
                SecondActivity.actionStart(this, content.getText().toString());
                break;

            case R.id.create_db:
                // 构建出 SQLiteOpenHelper 的实例之后，再调用它的 getReadableDatabase()或 getWritableDatabase()方法就能够创建数据库了，数据库文件会存放在/data/data/<package name>/databases/目录下。
                dbHelper.getWritableDatabase();
                break;

            case R.id.add_item:
                addData();
                break;

            case R.id.remove_item:
                deleteData();
                break;

            case R.id.query_item:
                queryData();
                break;

            case R.id.update_item:
                updateData();
                break;
        }
        return true;
    }

    private void deleteData()
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("book", "name like ?", new String[]{"meimei"} );
    }

    private void updateData()
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("name", "meimei");
//        db.update("book", values, "name = ?" , new String[]{"shashen"});
        db.execSQL("update book set pages = ? where name = ?", new String[]{"500", "nvshen"} );
    }

    private void queryData()
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        Cursor cursor = db.query("book", null, null, null, null, null, "price desc");
        Cursor cursor = db.rawQuery("select * from book where pages > ?", new String[]{"30"});

        StringBuilder sb = new StringBuilder();
        if (cursor.moveToFirst())
        {
            do{
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String author = cursor.getString(cursor.getColumnIndex("author"));
                int page = cursor.getInt(cursor.getColumnIndex("pages"));
                double price = cursor.getDouble(cursor.getColumnIndex("price"));
                sb.append(name).append(",").append(author).append(",").append(page).append(",").append(price).append(",");
                Log.e(TAG, sb.toString());
            }while (cursor.moveToNext());
        }


    }

    private void addData()
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", "shashen");
        values.put("author", "xwei");
        values.put("pages", 1000);
        values.put("price", 89.00);
        db.insert("book", null, values);
        values.clear();
        values.put("name", "nvshen");
        values.put("author", "xwei");
        values.put("pages", 300);
        values.put("price", 169.00);
        db.insert("book", null, values);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_anim:
//                showTips();
//                getRunningProcesses();
//                threadLocalTest();
//                performAnimate();
//                performAnimate(animBtn, animBtn.getWidth(), 1500);

                // 计时器
//                tvTimer();

                // 灵动菜单
//                if (mFlag)
//                {
//                    startAnim();
//                }
//                else
//                {
//                    closeAnim();
//                }
//
//                if (null == hideLl)
//                {
//                    View view = mViewStub.inflate();
//                    hideLl = (LinearLayout) view.findViewById(R.id.ll_hide);
//                }
//                if (hideLl.getVisibility() == View.GONE)
//                {
//                    animateOpen(hideLl);
//                }
//                else
//                {
//                    animateClose(hideLl);
//                }
                showTips(this, "提示", "当前无网络连接，请连接网络再重试！", "知道了");
                break;
        }
    }

    AlertDialog alertDialog;
    private void showTips(Context context, String title, String msg, String btnTxt)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog = builder.create();
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.show();
    }

    public void getRunningProcesses()
    {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        Log.e(TAG, "processInfos.size() : " + processInfos.size());
        for (ActivityManager.RunningAppProcessInfo info : processInfos)
        {
            LogUtil.e(TAG, "当前运行的进程：" + info.processName);
        }
        List<ActivityManager.RunningServiceInfo> serviceInfos = am.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo info:serviceInfos)
        {
            if (info.process.contains("com.xiaomi"))
            {
                LogUtil.e(TAG, "当前运行的进程及服务：" + info.process);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void animateClose(final LinearLayout hideLl)
    {
        int origHeight = hideLl.getHeight();
        ValueAnimator animator = createDropAnimator(hideLl, origHeight, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                hideLl.setVisibility(View.GONE);
            }
        });
        animator.start();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void animateOpen(LinearLayout hideLl)
    {
        hideLl.setVisibility(View.VISIBLE);
        ValueAnimator animator = createDropAnimator(hideLl, 0, mHideLayoutHeight);
        animator.start();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private ValueAnimator createDropAnimator(final View view, int start, int end)
    {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void tvTimer()
    {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 30);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animBtn.setText(animation.getAnimatedValue() + "");
            }
        });
        valueAnimator.setDuration(30000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void closeAnim()
    {
        LogUtil.d(TAG, "-- closeAnim --");
        ObjectAnimator animator0 = ObjectAnimator.ofFloat(views.get(0), "alpha", 0.5f, 1f);
        animator1 = ObjectAnimator.ofFloat(views.get(1), "translationY" , 0f);
        animator2 = ObjectAnimator.ofFloat(views.get(2), "translationX" , 0f);
        animator3 = ObjectAnimator.ofFloat(views.get(3), "translationY" , 0f);
        animator4 = ObjectAnimator.ofFloat(views.get(4), "translationX" , 0f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(500);
        set.setInterpolator(new BounceInterpolator());
        set.playTogether(animator0, animator1, animator2, animator3, animator4);
        set.start();
        mFlag = true;
    }

    ObjectAnimator animator1, animator2, animator3, animator4;
    boolean mFlag = true;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void startAnim()
    {
        LogUtil.d(TAG, "-- startAnim --");
        ObjectAnimator animator0 = ObjectAnimator.ofFloat(views.get(0), "alpha", 1f, 0.5f);
        animator1 = ObjectAnimator.ofFloat(views.get(1), "translationY" , 200f);
        animator2 = ObjectAnimator.ofFloat(views.get(2), "translationX" , 200f);
        animator3 = ObjectAnimator.ofFloat(views.get(3), "translationY" , -200f);
        animator4 = ObjectAnimator.ofFloat(views.get(4), "translationX" , -200f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(500);
        set.setInterpolator(new BounceInterpolator());
        set.playTogether(animator0, animator1, animator2, animator3, animator4);
        set.start();
        mFlag = false;
    }

    private void threadLocalTest()
    {
        new Thread("Thread#1")
        {
            @Override
            public void run() {
                mThreadLocal.set(false);
                LogUtil.e(TAG, "[Thread#1] mThreadLocal = " + mThreadLocal.get());
            }
        }.start();

        new Thread("Thread#2")
        {
            @Override
            public void run() {
                LogUtil.e(TAG, "[Thread#2] mThreadLocal = " + mThreadLocal.get());
            }
        }.start();

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void performAnimate(final View target, final int start, final int end)
    {
        final ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 100);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            IntEvaluator intEvaluator = new IntEvaluator();

            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = (int) valueAnimator.getAnimatedValue();
                LogUtil.e(TAG, "current value : " + currentValue);

                float fraction = animation.getAnimatedFraction();
                target.getLayoutParams().width = intEvaluator.evaluate(fraction, start, end);
                target.requestLayout();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        valueAnimator.setDuration(5000).start();
    }

    private void performAnimate()
    {
        ViewWrapper viewWrapper = new ViewWrapper(animBtn);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ObjectAnimator.ofInt(viewWrapper, "width", 500).setDuration(2500).start();
        }
    }

    class ViewWrapper
    {
        private View mTarget;

        public ViewWrapper(View view)
        {
            mTarget = view;
        }

        public void setWidth(int width)
        {
            mTarget.getLayoutParams().width = width;
            mTarget.requestLayout();
            LogUtil.e(TAG, "setWidth : " + width);
        }

        public int getWidth()
        {
//            int width = mTarget.getLayoutParams().width;
            int width = mTarget.getWidth();
            LogUtil.e(TAG, "getWidth : " + width);
            return width;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        LogUtil.e(TAG, "--- dispatchTouchEvent ---");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        LogUtil.e(TAG, "--- onTouchEvent ---");
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

    @Override
    protected void onResume()
    {
        super.onResume();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (!isIgnore()) { // 如果没有忽略电池优化，给出提示
//                showTips();
//            }
//        }
    }

    private void showTips()
    {
//        Log.e(TAG, "--- showTips() ---");
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        Intent intent = new Intent(this, SecondActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Notification call = getNotification(this, pendingIntent, R.drawable.ic_launcher,getString(R.string.app_name),
//                "云对讲服务中断", true, false);
//        notificationManager.notify(101, call);
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("为确保app能在手机休眠及待机状态下仍保持网络连接，请把app加入电池优化白名单。谢谢！！")
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                })
                .show();
    }



    @TargetApi(Build.VERSION_CODES.M)
    private boolean isIgnore()
    {
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
//        boolean isIgnore = powerManager.isIgnoringBatteryOptimizations(getPackageName());
//        LogUtil.e(TAG, "忽略电池优化：" + isIgnore);
        Object result = null;

        try {
            Class power = Class.forName("android.os.PowerManager");
            try {
                Method method = power.getDeclaredMethod("isIgnoringBatteryOptimizations", Class.forName("java.lang.String"));
                try {
                    result = method.invoke(powerManager,getPackageName());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                LogUtil.e(TAG, "result : " + result);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return Boolean.parseBoolean(result + "");
    }

    public Notification getNotification(Context mContext, PendingIntent pendingIntent, int icon, String title, String content, boolean ongoing, boolean autoCancel)
    {
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            Notification.Builder builder = new Notification.Builder(mContext)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setOngoing(ongoing)
                    .setAutoCancel(autoCancel)
                    .setSmallIcon(icon)
                    .setWhen(System.currentTimeMillis());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            {
                notification = builder.build();
            }
            else
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                {
                    notification = builder.getNotification();
                }
            }
        }
        return notification;
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        LogUtil.e(TAG, "--- onLowMemory ---");
    }

    @Override
    public void onTrimMemory(int level)
    {
        super.onTrimMemory(level);
        LogUtil.e(TAG, "--- onTrimMemory ---" + " , level : " + level);
    }
}
