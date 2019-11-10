package com.wangxiaobao.gsj.common;


import com.wangxiaobao.gsj.base.App;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = CrashHandler.class.getSimpleName();

    private Thread.UncaughtExceptionHandler mDefaultUEH;

    public CrashHandler() {
        LogTool.saveLog(TAG, "------------ CrashHandler ------------");
        mDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        LogTool.saveLog(TAG, "app exception", ex);
        LogTool.saveLog(TAG, "App Crash", ex);
        CommonUtil.saveProperty(App.getContext(),"Crash",true);

        mDefaultUEH.uncaughtException(thread, ex); // 不加本语句会导致ANR
    }

}