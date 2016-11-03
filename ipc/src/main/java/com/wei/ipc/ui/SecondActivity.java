package com.wei.ipc.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.wei.ipc.IMyAidlInterface;
import com.wei.ipc.IOnNewPersonArrivedListener;
import com.wei.ipc.Person;
import com.wei.ipc.R;
import com.wei.ipc.service.PersonManagerService;

import java.util.List;

/**
 * 新版本2.3.02商城显示网页和实现跳转
 *
 * @author Jetch
 */
public class SecondActivity extends BaseActivity
{
    private final String TAG = getClass().getSimpleName();
    private final int MESSAGE_NEW_PERSON_ARRIVED = 1;
    private IMyAidlInterface mRemotePersonManager;

    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case MESSAGE_NEW_PERSON_ARRIVED:
                    Log.d(TAG, "receive new book : " + msg.obj);
                    break;

                default:
                    super.handleMessage(msg);
            }
        }
    };

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IMyAidlInterface anInterface = IMyAidlInterface.Stub.asInterface(service);

            try {
                mRemotePersonManager = anInterface;

                List<Person> persons = anInterface.getPersonList();
                Log.e(TAG, "query persons, list type: " + persons.getClass().getCanonicalName());
                Log.e(TAG, "persons : " + persons.toString());

                Person person = new Person(3, "美女", false);
                anInterface.addPerson(person);
                Log.e(TAG, "new Person : " + person);

                List<Person> personsList = anInterface.getPersonList();
                Log.e(TAG, "personsList : " + personsList.toString());

                anInterface.registerListener(mIOnNewPersonArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRemotePersonManager = null;
            Log.e(TAG, "binder died.");
        }
    };

    private IOnNewPersonArrivedListener mIOnNewPersonArrivedListener = new IOnNewPersonArrivedListener.Stub()
    {
        @Override
        public void onNewPersonArrived(Person newPerson) throws RemoteException {
            mHandler.obtainMessage(MESSAGE_NEW_PERSON_ARRIVED, newPerson).sendToTarget();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        Person person = intent.getParcelableExtra("person");
        Log.e(TAG, person.personId + ", " + person.personName + ", " + person.isMale);

        Intent intent1 = new Intent(this, PersonManagerService.class);
        bindService(intent1, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy()
    {
        if (mRemotePersonManager != null && mRemotePersonManager.asBinder().isBinderAlive())
        {
            Log.i(TAG, "unregister listener : " + mIOnNewPersonArrivedListener);
            try {
                mRemotePersonManager.unregisterListener(mIOnNewPersonArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        unbindService(mServiceConnection);
        super.onDestroy();
    }
}
