package com.wei.applications.algorithms;

/**
 * Created by WEI on 2016/8/16.
 */
public class MultiThreads
{
    public static void main(String[] args)
    {
        MultiThreads multiThreads = new MultiThreads();
        multiThreads.startThread("wei");
        multiThreads.startThread("xxx");
        multiThreads.startThread("cai");
    }

    private void startThread(final String name)
    {
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                out(name);
            }
        }).start();
    }

    private void out(String name)
    {
        for (int i = 0; i < 5; i ++)
        {
            System.out.print(name + i + " ");
        }
    }
}
