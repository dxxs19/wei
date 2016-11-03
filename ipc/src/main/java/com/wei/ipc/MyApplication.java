package com.wei.ipc;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.wei.ipc.util.OsUtils;

import dagger.ObjectGraph;

/**
 * Created by WEI on 2016/7/26.
 */
public class MyApplication extends Application
{
    private final String TAG = "MyApplication";
    private ObjectGraph mObjectGraph;
    private static MyApplication mInstance;

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.e(TAG, "--- onCreate ---");
        ActivityManager am = ((ActivityManager)getSystemService(Context.ACTIVITY_SERVICE));
        int myPid = android.os.Process.myPid();
        int myUid = android.os.Process.myUid();
        Log.e(TAG, "app start, myPid : " + myPid + ", myUid : " + myUid + ", processName : " + OsUtils.getProcessName(this, myPid));

        mInstance = this;
        // 初始化对象注入关系表
        mObjectGraph = ObjectGraph.create(new AppModule(this));
        mObjectGraph.inject(this);

    }

    public void inject(Object o)
    {
        mObjectGraph.inject(o);
    }
}
