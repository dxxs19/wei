package com.wei.applications.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.wei.applications.constant.MyConstants;

public class MessengerService extends Service
{
    private static final String TAG = "MessengerService";
    private final Messenger mMessenger = new Messenger(new MessengerHandler());

    private static class MessengerHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case MyConstants.MSG_FROM_CLIENT:
                    Log.i(TAG, "receive msg from Client : " + msg.getData().getString("msg"));
                    Messenger client = msg.replyTo;

                    Message replyMessage = Message.obtain(null, MyConstants.MSG_FROM_SERVICE);
                    Bundle bundle = new Bundle();
                    bundle.putString("reply", "你的消息我已经收到，稍后回复你！");
                    replyMessage.setData(bundle);

                    try {
                        client.send(replyMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        Log.i(TAG, "--- onBind ---");
        return mMessenger.getBinder();
    }
}
