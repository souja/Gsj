package com.wangxiaobao.pushmanager;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.List;

/**
 * Created by wave on 2018/1/14.
 */

public class GsjService extends Service {

    private static final String TAG = GsjService.class.getSimpleName();

    private Runnable checkMonitorService = new Runnable() {
        @Override
        public void run() {
            launchGsjMonitor();
            handler.postDelayed(this, DELAY_CHECK_MONITOR);

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        LogTool.saveLog("GsjService onCreate");
        handler.post(checkMonitorService);


    }

    private Handler handler = new Handler();

    private static final int DELAY_CHECK_MONITOR = 1000 * 60;


    /**
     * 启动工商局
     */
    private void launchGsjMonitor() {

        try {
            if (!isServiceRunning(getApplicationContext(), "com.wangxiaobao.gsjmonitor.MonitorService")) {
//                launchApk("com.wangxiaobao.gsj.player");

                Intent intent = new Intent("android.wangxiaobao.gsj.ACTION_MONITOR_SERVICE");
                intent.setPackage("com.wangxiaobao.gsjmonitorII");
                //ComponentName的参数1:目标app的包名,参数2:目标app的Service完整类名
                intent.setComponent(new ComponentName("com.wangxiaobao.gsjmonitorII", "com.wangxiaobao.gsjmonitor.MonitorService"));
                startService(intent);
                Log.i(TAG, "启动工商局守护进程完成");
            } else {
                Log.i(TAG, "工商局守护进程 已经运行");
            }

        } catch (Exception e) {
            Log.i(TAG, "启动工商局守护进程错误", e);
        }


    }

    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList
                = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (serviceList.size() < 1) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            String runningClassName = serviceList.get(i).service.getClassName();
            Log.i(TAG, runningClassName);
            if (runningClassName.equals(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
