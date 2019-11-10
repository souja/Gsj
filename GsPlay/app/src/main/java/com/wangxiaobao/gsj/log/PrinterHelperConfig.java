package com.wangxiaobao.gsj.log;

import android.os.Build;
import android.os.Environment;

import java.io.File;

/**
 * Created by candy on 16-10-21.
 */
public class PrinterHelperConfig {
    public final static String RESULT_SUCCESS = "0";
    public static final String PRINTER_PACKAGENAME = "com.wangxiaobao.printer.usb";
    public static final String PRINTER_HELPER_PACKAGENAME = "com.wangxiaobao.printer.helper";
    public static final String PRINTER_MARKET_PACKAGENAME = "com.wangxiaobao.printer.usb.update";
    public static boolean IS_DEBUG_SN = false;
    public final static String PRINTER_LOG_PATH
            = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "Printer";
    public final static String UPDATE_MANAGER_LOG_PATH
            = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "Market";

    public final static String UPDATE_MANAGER_APK_PATH
            = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "Market" + File.separator + "Apk";
    public final static String PRINTER_HELPER_LOG_PATH
            = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "PrinterHelper";


    public static final String QINIUYUN_URL = "https://www.wangxiaobao.co/tc-web/common/qiniu/getQiniuToken.htm";


    public final static String PRINTER_LOG_FILE_NAME = "Printer.txt";
    public final static String PRINTER_UPDATE_MANAGER_LOG_FILE_NAME = "PrinterMarket.txt";
    public final static String PRINTER_HELPER_LOG_FILE_NAME = "PrinterHelper.txt";

    public static String SN = "DB03316102200008";

    public static boolean DEBUG = false;


    public static String getSN() {
        if (IS_DEBUG_SN) {
            return SN;
        } else {
            return Build.SERIAL;
        }
    }
}
