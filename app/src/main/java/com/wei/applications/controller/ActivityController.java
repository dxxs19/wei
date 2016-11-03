package com.wei.applications.controller;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WEI on 2016/5/19.
 */
public class ActivityController
{
    public static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity)
    {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity)
    {
        activities.remove(activity);
    }

    public static void finishAll()
    {
        for (Activity activity : activities)
        {
            if (!activity.isFinishing())
            {
                activity.finish();
            }
        }
    }

    public static void exitApp(Context context)
    {
        finishAll();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO)
        {
            activityManager.killBackgroundProcesses(context.getPackageName());
        }
        System.exit(0);
    }
}
