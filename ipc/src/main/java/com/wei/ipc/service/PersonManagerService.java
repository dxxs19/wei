package com.wei.ipc.service;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.wei.ipc.IMyAidlInterface;
import com.wei.ipc.IOnNewPersonArrivedListener;
import com.wei.ipc.Person;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class PersonManagerService extends Service
{
    private final String TAG = getClass().getSimpleName();
    private CopyOnWriteArrayList<Person> mPersons = new CopyOnWriteArrayList<>();

    private AtomicBoolean mIsServiceDestroyed = new AtomicBoolean(false);
//    private CopyOnWriteArrayList<IOnNewPersonArrivedListener> mIOnNewPersonArrivedListeners = new CopyOnWriteArrayList<>();
    private RemoteCallbackList<IOnNewPersonArrivedListener> mIOnNewPersonArrivedListeners = new RemoteCallbackList<>();

    private Binder mBinder = new IMyAidlInterface.Stub()
    {
        @Override
        public List<Person> getPersonList() throws RemoteException {
            return mPersons;
        }

        @Override
        public void addPerson(Person person) throws RemoteException {
            mPersons.add(person);
        }

        @Override
        public void registerListener(IOnNewPersonArrivedListener listener) throws RemoteException {
//            if (!mIOnNewPersonArrivedListeners.contains(listener))
//            {
//                mIOnNewPersonArrivedListeners.add(listener);
//            }
//            else
//            {
//                Log.d(TAG, "already exists.");
//            }
            mIOnNewPersonArrivedListeners.register(listener);
            Log.d(TAG, "registerListener size : " + mIOnNewPersonArrivedListeners.beginBroadcast());
        }

        @Override
        public void unregisterListener(IOnNewPersonArrivedListener listener) throws RemoteException {
//            if (mIOnNewPersonArrivedListeners.contains(listener))
//            {
//                mIOnNewPersonArrivedListeners.remove(listener);
//                Log.d(TAG, "unregister listener succeed.");
//            }
//            else
//            {
//                Log.d(TAG, "not found, can not unregister.");
//            }
            mIOnNewPersonArrivedListeners.unregister(listener);
            Log.d(TAG, "unregisterListener, current size : " + mIOnNewPersonArrivedListeners.beginBroadcast());
        }
    };

    @Override
    public void onCreate()
    {
        Log.e(TAG, "--- onCreate ---");
        super.onCreate();
        mPersons.add(new Person(1, "xiangwei", true));
        mPersons.add(new Person(2, "ggenmn", false));
        new Thread(new ServiceWorker()).start();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        int check = 0;
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            check = checkCallingOrSelfPermission("android.permission.MOUNT_UNMOUNT_FILESYSTEMS");
            Log.e(TAG, "--- onBind ---" + ", check = " + check );
            if ( check == PackageManager.PERMISSION_DENIED)
            {
                Log.e(TAG, "无权访问");
                return null;
            }
        }
        return mBinder;
    }

    @Override
    public void onDestroy() {
        mIsServiceDestroyed.set(true);
        super.onDestroy();
    }

    private class ServiceWorker implements Runnable
    {
        @Override
        public void run() {
            while (!mIsServiceDestroyed.get())
            {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int personId = mPersons.size() + 1;
                Person newPerson = new Person(personId, "new person#" + personId, personId % 2 == 0 ? true : false);
                onNewPersonArrived(newPerson);
            }
        }
    }

    private void onNewPersonArrived(Person newPerson)
    {
        mPersons.add(newPerson);
        try {
            int size = mIOnNewPersonArrivedListeners.beginBroadcast();
            for (int i = 0; i < size; i ++)
            {
                IOnNewPersonArrivedListener iOnNewPersonArrivedListener = mIOnNewPersonArrivedListeners.getBroadcastItem(i);
                Log.d(TAG, "onNewPersonArrived, notify listener : " + iOnNewPersonArrivedListener);
                if (iOnNewPersonArrivedListener != null) {
                    try {
                        iOnNewPersonArrivedListener.onNewPersonArrived(newPerson);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        catch (Exception e)
        {
//            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        finally {
            mIOnNewPersonArrivedListeners.finishBroadcast();
        }
//        Log.d(TAG, "onNewPersonArrived, notify listeners : " + mIOnNewPersonArrivedListeners.size());
//        for (int i = 0; i < mIOnNewPersonArrivedListeners.size(); i ++)
//        {
//            IOnNewPersonArrivedListener iOnNewPersonArrivedListener = mIOnNewPersonArrivedListeners.get(i);
//            Log.d(TAG, "onNewPersonArrived, notify listener : " + iOnNewPersonArrivedListener);
//            try {
//                iOnNewPersonArrivedListener.onNewPersonArrived(newPerson);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
