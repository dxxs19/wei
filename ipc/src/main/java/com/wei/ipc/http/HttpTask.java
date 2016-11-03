package com.wei.ipc.http;

import java.util.concurrent.Callable;

import bolts.Task;

/**
 * Created by WEI on 2016/8/11.
 */
public class HttpTask
{
    public Task<String> getWebBitmap()
    {
        return Task.callInBackground(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "hello bolts";
            }
        });
    }
}
