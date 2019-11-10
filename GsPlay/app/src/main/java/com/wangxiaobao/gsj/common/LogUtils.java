package com.wangxiaobao.gsj.common;

import com.thf.logger.Logger;
import com.wangxiaobao.pushmanager.Constants;
import com.wangxiaobao.waiter.BuildConfig;

public class LogUtils {

    public static final String TAG_LOG = "WXB_GSJ";
    private static final String TAG_EXCEPTION = TAG_LOG + " Exception";

    public static final String LOG_PATH = Constants.LOG_PATH + "GSJ/";
    private static final String TABLECARD_LOG_NAME = "Gsj";
    private static Logger wxbLogger;

    public static void init() {
        wxbLogger = new Logger.Builder().
                setMaxFileNumber(2)//设置缓存文件个数
                .setLogDir(LOG_PATH)//设置根路径
                .setFilePreFix(TABLECARD_LOG_NAME)//设置文件名
                .setMaxSize(BuildConfig.LOGFILE_MAX_SIZE + "MB")
                .create();//创建日志系统
    }

    public static void saveLog(String content) {
        if (wxbLogger != null) {
            wxbLogger.saveLog(TAG_LOG, content);
        } else {
            init();
            wxbLogger.saveLog(TAG_LOG, content);
        }

    }

    public static void saveLog(String tag, String content) {
        if (wxbLogger != null) {
            wxbLogger.saveLog(tag, content);
        } else {
            init();
            wxbLogger.saveLog(tag, content);
        }

    }

    public static void saveLog(String content, Throwable e) {
        if (wxbLogger != null) {
            wxbLogger.saveLog(TAG_LOG, content, e);
        } else {
            init();
            wxbLogger.saveLog(TAG_LOG, content, e);
        }

    }

    public static void saveLog(String tag, String content, Throwable e) {
        if (wxbLogger != null) {
            wxbLogger.saveLog(tag, content, e);
        } else {
            init();
            wxbLogger.saveLog(tag, content, e);
        }

    }

    public static void saveLog(String tag, String content, Exception e) {
        if (wxbLogger != null) {
            wxbLogger.saveLog(tag, content, e);
        } else {
            init();
            wxbLogger.saveLog(tag, content, e);
        }

    }

    public static void saveLog(String content, Exception e) {
        if (wxbLogger != null) {
            wxbLogger.saveLog(TAG_LOG, content, e);
        } else {
            init();
            wxbLogger.saveLog(TAG_LOG, content, e);
        }

    }
}
