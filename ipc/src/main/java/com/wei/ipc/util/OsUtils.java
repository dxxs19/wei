package com.wei.ipc.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by WEI on 2016/7/22.
 */
public class OsUtils
{
    public static String getProcessName(Context context, int pid)
    {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = activityManager.getRunningAppProcesses();
        if (runningAppProcessInfos == null)
            return null;

        for (ActivityManager.RunningAppProcessInfo info:
             runningAppProcessInfos) {
            if (pid == info.pid)
            {
                return info.processName;
            }
        }
        return null;
    }
}
