import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.wei.applications.utils.LogUtil;
import com.wei.applications.utils.OsUtils;

/**
 * Created by WEI on 2016/7/22.
 */
public class TestApplication extends Application
{
    private static final String TAG = "TestApplication";

    @Override
    public void onCreate()
    {
        super.onCreate();
        ActivityManager am = ((ActivityManager)getSystemService(Context.ACTIVITY_SERVICE));
        int myPid = android.os.Process.myPid();
        int myUid = android.os.Process.myUid();
        LogUtil.e(TAG, "app start, myPid : " + myPid + ", myUid : " + myUid + ", processName : " + OsUtils.getProcessName(this, myPid));
    }
}
