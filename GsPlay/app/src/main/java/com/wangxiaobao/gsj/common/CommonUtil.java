package com.wangxiaobao.gsj.common;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.Gravity;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.wangxiaobao.gsj.acount.DBHelper;
import com.wangxiaobao.gsj.acount.VerificationCodeActivity;
import com.wangxiaobao.gsj.base.App;
import com.wangxiaobao.waiter.R;
import com.wangxiaobao.gsj.user.VerifyCode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 */
public class CommonUtil {

    public static final String PREFERENCES_NAME = "com.wxb.waiter.sharedpre";
    private static final String TAG = CommonUtil.class.getSimpleName();

    public static void saveProperty(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void clearProperty(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public static void saveProperty(Context context, String key, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getPropertyBoolean(Context context, String key) {
        return getPropertyBoolean(context, key, false);
    }

    public static boolean getPropertyBoolean(Context context, String key, boolean defaultValue) {
        boolean re = false;
        if (context != null) {
            SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
            re = preferences.getBoolean(key, defaultValue);
        }
        return re;
    }

    public static void saveProperty(Context context, String key, int value) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }


    //给文字添加下划线
    private Spannable getDividerText(String text) {
        SpannableString builder = new SpannableString(text);
        StrikethroughSpan stSpan = new StrikethroughSpan();
        builder.setSpan(stSpan, 0, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return builder;
    }


    //给文字添加颜色
    private SpannableStringBuilder getColorText(String text, int colorId) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);


        ForegroundColorSpan redSpan = new ForegroundColorSpan(colorId);

        builder.setSpan(redSpan, 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return builder;
    }

    public static void saveProperty(Context context, String key, long value) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static String getPropertyString(Context context, String key) {
        String re = "";
        if (context != null) {
            SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
            re = preferences.getString(key, "");
        }
        return re;
    }


    public static long getCountDown(Context context) {
        return getPropertyLong(context, VerificationCodeActivity.PREFERENCE_COUNT_DOWN, 0L);
    }

    public static String getMerchantName(Context context) {
        return getPropertyString(context, AppConfig.MERCHANT_NAME);
    }

    public static String getMerchantAccount(Context context) {
        return getPropertyString(context, AppConfig.PREFERENCE_MERCHANT_ACCOUNT);
    }

    public static String getMerchantID(Context context) {
        return getPropertyString(context, AppConfig.MERCHANT_ID);
    }

    public static String getUserID(Context context) {
        return getPropertyString(context, AppConfig.USER_ID);
    }


    public static String getUserName(Context context) {
        return getPropertyString(context, AppConfig.USER_NAME);
    }


    public static boolean hasM() {
        return Build.VERSION.SDK_INT >= 23;
    }

    public static int getPropertyInt(Context context, String key) {
        return getPropertyInt(context, key, 0);
    }

    public static int getPropertyInt(Context context, String key, int defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        return preferences.getInt(key, defaultValue);
    }

    public static long getPropertyLong(Context context, String key, long defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        return preferences.getLong(key, defaultValue);
    }

    public static boolean isFirstIntoDishManager(Context context) {

        return context.getResources().getBoolean(R.bool.first_into_dish_manager);

    }

    public static String getShopId(Context context) {
        return CommonUtil.getPropertyString(context, AppConfig.SHOP_ID);
    }


    public static void showShortToast(Context context, String message) {
        LogTool.saveLog(TAG, message);
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    public static void showShortToast(String message) {
        showShortToast(App.getContext(), message);
    }

    public static String getCurrentTime() {
        return new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(new Date());
    }

    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    /**
     * version1 > version2 return 1, if version1 < version2 return -1, otherwise return 0
     *
     * @param version1
     * @param version2
     * @return
     */

    public static int compareVersion(String version1, String version2) {
        if (version1 == null && version2 == null)
            return 0;

        String[] v1 = version1.split("\\.");
        String[] v2 = version2.split("\\.");

        if (v1.length > v2.length) {
            if (compareVersion(version2, version1) == 1)
                return -1;
            else if (compareVersion(version2, version1) == -1)
                return 1;
            return 0;
        }

        for (int i = 0; i < v1.length; i++) {
            if (Integer.valueOf(v1[i]) > Integer.valueOf(v2[i]))
                return 1;
            else if (Integer.valueOf(v1[i]) < Integer.valueOf(v2[i]))
                return -1;
        }

        for (int i = v1.length; i < v2.length; i++) {
            if (Integer.valueOf(v2[i]) > 0)
                return -1;
            else if (Integer.valueOf(v2[i]) < 0)
                return 1;
        }
        return 0;
    }


    public static Date parseDate(String dateText) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            Date date = sdf.parse(dateText);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Try to return the absolute file path from the given Uri  兼容了file:///开头的 和 content://开头的情况
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


    public static void sendCountEvent(Context context, String eventName) {
        if (!AppConfig.IS_RELEASE_MODE) return;
        try {
            Map<String, String> map = new HashMap<>();
            map.put("MerchantName", CommonUtil.getMerchantName(context));
            MobclickAgent.onEvent(context, eventName, map);
        } catch (Exception e) {

        }

    }


    public static void saveVerifyCode(Context context, String phoneNumber) {
        VerifyCode verifyCode = new VerifyCode();
        verifyCode.setPhoneNumber(phoneNumber);
        verifyCode.setTime(System.currentTimeMillis() / 1000);
        LogTool.i(TAG, "saveVerifyCode verifyCode:" + verifyCode.toString());
        try {
            DBHelper.getHelper(context).getVerifyCode().createOrUpdate(verifyCode);
        } catch (Exception e) {
            LogTool.saveLog(TAG, "saveVerifyCode error", e);
        }
    }

    public static VerifyCode getVerifyCode(Context context, String phoneNumber) {
        LogTool.i(TAG, "getVerifyCode phoneNumber:" + phoneNumber);
        try {
            return DBHelper.getHelper(context).getVerifyCode().queryForId(phoneNumber);
        } catch (Exception e) {
            LogTool.saveLog(TAG, "getVerifyCode error", e);
            return null;
        }
    }


    public static void sendLoginStatus(Context context, int logStatus) {
        Map<String, String> map = new HashMap<>();
        map.put("MerchantName", getMerchantName(context));
        map.put("user", getUserName(context));
        if (logStatus == 1) {
            map.put("loginStatus", "成功");
        } else {
            map.put("loginStatus", "失败");
        }

        LogTool.saveLog(TAG, "sendLoginStatus map:" + map);
        MobclickAgent.onEvent(context, "LoginStatus", map);
    }
}
