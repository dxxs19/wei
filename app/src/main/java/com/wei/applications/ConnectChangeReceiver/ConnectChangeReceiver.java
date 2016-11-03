package com.wei.applications.ConnectChangeReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;


/**
 * 网络连接监控
 * 如果网络发生变化，则做出相应的处理
 * @author WEI
 *
 */
public class ConnectChangeReceiver extends BroadcastReceiver
{
	private final String TAG = getClass().getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent)
	{
		Log.d(TAG, "action : " + intent.getAction());
		ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo hasNetWork = conManager.getActiveNetworkInfo();
		if (hasNetWork != null) 
		{
			if (hasNetWork.isAvailable()) 
			{ // 有网络连接并且可用
//				Toast.makeText(context, "当前网络可用！", Toast.LENGTH_LONG).show();
				Log.d(TAG, "当前网络可用！");
			}
			else
			{
				Toast.makeText(context, "当前网络不可用，请切换网络再重试！", Toast.LENGTH_LONG).show();
			}
		}
		else
		{
			Toast.makeText(context, "当前无网络连接，请连接网络再重试！", Toast.LENGTH_LONG).show();
		}
		
	}
}
