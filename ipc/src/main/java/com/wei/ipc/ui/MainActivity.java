package com.wei.ipc.ui;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.wei.ipc.Person;
import com.wei.ipc.R;
import com.wei.ipc.constant.MyConstants;
import com.wei.ipc.model.User;
import com.wei.ipc.service.MessengerService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import bolts.Continuation;
import bolts.Task;
import de.greenrobot.event.EventBus;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseInjectActivity {
    private final static String TAG = "MainActivity";
    private Messenger mMessenger;
    private Messenger mGetReplyMessenger = new Messenger(new MessengerHandler());
    private final String targetPkg = "com.wei.applications";
    @ViewById
    TextView showTv;
    @ViewById
    ImageView originalImgView, showImgView;
    private int screenWidth, screenHeight;
    @Inject
    WindowManager mWindowManager;

    @AfterViews
    void initView()
    {
        Log.e(TAG, "--- initView ---");
        setCustomTitle("测试");
//        WindowManager windowManager = mWindowManager.getWindowManager();
        screenWidth = mWindowManager.getDefaultDisplay().getWidth();
        screenHeight = mWindowManager.getDefaultDisplay().getHeight();
        Log.e(TAG, "screenWidth : " + screenWidth + ", screenHeight : " + screenHeight);
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            mWindowManager.getDefaultDisplay().getSize(point);
            int width = point.x;
            int height = point.y;
            Log.e(TAG, "width : " + width + ", height : " + height);
        }
        // 相同app里面service绑定
        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        displayBitmap();
    }

    @Override
    protected void onResume()
    {
        Log.e(TAG, "--- onResume ---");
        super.onResume();
        EventBus.getDefault().post("事件订阅/分发模式");
    }

    private static class MessengerHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case MyConstants.MSG_FROM_SERVICE:
                    Log.i(TAG, "receive msg from Service:"  + msg.getData().getString("reply"));
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            Log.i(TAG, "--- onServiceConnected ---");
            mMessenger = new Messenger(service);
            Message msg = Message.obtain(null, MyConstants.MSG_FROM_CLIENT);
            Bundle data = new Bundle();
            data.putString("msg", "hello, this is client.");
            msg.setData(data);

            msg.replyTo = mGetReplyMessenger;

            try {
                mMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "--- onServiceDisconnected ---");
        }
    };

    @TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
    private void displayBitmap()
    {
        try {
            InputStream inputStream = getAssets().open("chemo.jpg");
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);

            int width = options.outWidth;
            int height = options.outHeight;

            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            originalImgView.setImageBitmap(bitmap);  // 显示原图

            BitmapRegionDecoder bitmapRegionDecoder = BitmapRegionDecoder.newInstance(inputStream, false);
            // 截取图片某部分
            bitmap = bitmapRegionDecoder.decodeRegion(new Rect(width/3 - 100, height/3 - 100, width/3 + 100, height/3 + 100), options);

            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            Log.e(TAG, "bitmapWidth : " + bitmapWidth + ", bitmapHeight : " + bitmapHeight);

            int xScale = screenWidth/bitmapWidth;
            int yScale = screenHeight/bitmapHeight;

            // 利用矩阵缩放截取出来的那部分图片
            Matrix matrix = new Matrix();
            matrix.postScale(xScale, yScale);
            Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);

            showImgView.setImageBitmap(resizeBmp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(mServiceConnection);
        super.onDestroy();
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.serialize_menu:
                serialize();
                break;

            case R.id.parcelable_menu:
                parcelable();
                break;

            case R.id.task_menu:
                setImage();
                break;
        }
        return true;
    }

    private void setImage()
    {
        Log.e(TAG, "-- setImage ---");
        Task.call(new Callable<Bitmap>() {
            @Override
            public Bitmap call() throws Exception
            {
                Log.e(TAG, "-- call ---");
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                return bitmap;
            }
        }, Task.BACKGROUND_EXECUTOR) // 在后台线程执行
                .continueWith(new Continuation<Bitmap, Void>() {
            @Override
            public Void then(Task<Bitmap> task) throws Exception
            {
                Log.e(TAG, "-- continueWith ---");
                if (task.isFaulted())
                {
                    Log.e(TAG, "-- failed ---");
                    Exception exception = task.getError();
                    if (null != exception)
                    {
                        showMsg(exception.getMessage());
                    }
                }
                else if (task.isCompleted())
                {
                    Log.e(TAG, "-- success ---");
                    showImgView.setImageBitmap(task.getResult());
                }
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR); // 在UI线程中执行
    }

    private void parcelable()
    {
        Person person = new Person(1, "anmo", false);
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("person", person);
        startActivity(intent);
    }

    private void serialize()
    {
        User user = new User(1, "wei", true);
        try {
            File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "IPC" + File.separator + "ipc_cache.txt");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file.getPath()));
            objectOutputStream.writeObject(user);
            objectOutputStream.close();

            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file.getPath()));
            User user1 = (User) objectInputStream.readObject();
            Log.e(TAG, user1.userId + ", " + user1.userName + ", " + user1.isMale);
            objectInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
