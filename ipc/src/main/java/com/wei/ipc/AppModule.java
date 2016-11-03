package com.wei.ipc;

import android.content.Context;
import android.view.WindowManager;

import com.wei.ipc.ui.MainActivity_;

import dagger.Module;
import dagger.Provides;

/**
 * Created by WEI on 2016/8/10.
 */
@Module(library = true, injects = {MainActivity_.class,
        MyApplication.class})
public class AppModule
{
    MyApplication mMyApplication;

    public AppModule(MyApplication myApplication)
    {
        this.mMyApplication = myApplication;
    }


    @Provides
    WindowManager provideWindowManager()
    {
        return (WindowManager) mMyApplication.getSystemService(Context.WINDOW_SERVICE);
    }
}
