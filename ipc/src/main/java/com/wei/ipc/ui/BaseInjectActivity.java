package com.wei.ipc.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.wei.ipc.MyApplication;

import de.greenrobot.event.EventBus;

/**
 * 已经进行了inject以及evenbus注册和ALLEvents.RequestException捕获
 * 
 * @author Jetch
 *
 */
public abstract class BaseInjectActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		((MyApplication) getApplication()).inject(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		EventBus.getDefault().register(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		EventBus.getDefault().unregister(this);
	}

	/**
	 * 请求失败事件处理
	 *
	 * @param msg
	 */
	public void onEventMainThread(String msg)
	{
		Log.e("onEventMainThread", msg);
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

}
