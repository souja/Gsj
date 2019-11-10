package com.wangxiaobao.gsj.common;

import android.util.Log;

import com.orhanobut.logger.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016-04-06.
 */
public class LogTool {
    public static Boolean DEBUG = false;
    private static String TAG = LogTool.class.getSimpleName();
    static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);


    public static void destory() {
        try {
            fixedThreadPool.shutdownNow();
            fixedThreadPool = null;
        } catch (Exception e) {
            Log.i(TAG, "", e);
        }


    }

    public static void E(String str) {
        E(null, str);
    }


    public static void E(String TAG, String str) {
        E(TAG, str, null, false);
    }

    public static void E(String TAG, String message, Throwable tr, boolean needSave) {

        LogUtils.saveLog(LogUtils.TAG_LOG,message,tr);



    }

    public static void E(String TAG, String message, Throwable tr) {
        E(TAG, message, tr, false);
    }


    public static void saveLog(String TAG, String message) {
        E(TAG, message, null, true);
    }


    public static void saveLog(String TAG, String message, Throwable throwable) {
        E(TAG, message, throwable, true);
    }



    public static void saveLog(String message) {
        E(null, message, null, true);
    }

    public static void I(String tag, String str) {
        E(tag, str);
    }

    public static void i(String tag, String str) {
        E(tag, str);
    }


    public static void i(String tag, String str, Throwable throwable) {
        E(tag, str, throwable);
    }


    public static void json(String tag, String json) {



    }

}
