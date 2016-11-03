package com.wei.applications.ui.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.wei.applications.R;
import com.wei.applications.controller.ActivityController;

public class BaseActivity extends FragmentActivity
{
    private final static String TAG = "BaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.e(TAG, getClass().getSimpleName());
        Log.e(getClass().getSimpleName(), "-- onCreate --");
        Log.e(getClass().getSimpleName(), "taskId = " + getTaskId());
        setContentView(R.layout.activity_base);
        ActivityController.addActivity(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e(getClass().getSimpleName(), "-- onConfigurationChanged --" + newConfig.orientation);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(getClass().getSimpleName(), "-- onRestart --");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(getClass().getSimpleName(), "-- onStart --");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e(getClass().getSimpleName(), "-- onRestoreInstanceState --");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(getClass().getSimpleName(), "-- onResume --");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(getClass().getSimpleName(), "-- onPause --");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(getClass().getSimpleName(), "-- onSaveInstanceState --");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(getClass().getSimpleName(), "-- onStop --");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        Log.e(getClass().getSimpleName(), "-- onCreateOptionsMenu --");
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removeActivity(this);
        Log.e(getClass().getSimpleName(), "-- onDestroy --");
    }

    protected void showMsg(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
