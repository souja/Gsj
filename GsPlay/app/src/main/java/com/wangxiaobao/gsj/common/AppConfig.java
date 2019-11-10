package com.wangxiaobao.gsj.common;


import com.wangxiaobao.waiter.BuildConfig;

public class AppConfig {
    public static final String APP_ID = "3a16dae968";
//    public static final boolean IS_RELEASE_MODE =BuildConfig.SERVER_MODE==4;
    public static final boolean IS_RELEASE_MODE =false;
    public final static String PACKAGE_NAME = "com.wxb.waiter";

    public final static String SP_KEY_AUTO_PRINT = PACKAGE_NAME + ".autoPrint";


    public final static String SP_KEY_FONT_SIZE = PACKAGE_NAME + ".font_size";


    public final static String SP_KEY_LOADED_TABLE_INFO_LIST = PACKAGE_NAME + "_SP_KEY_LOADED_TABLE_INFO_LIST";


    public final static String RESULT_SUCCESS = "0";

    public final static String RESULT_EXCEPTION = "1";

    public final static String RESULT_SESSION_DEAD = "7";

    public final static String SP_PREFIX = PACKAGE_NAME + ".sp_";
    public final static String SHOP_ID = SP_PREFIX + "shop_id";

    public final static String MERCHANT_ID = SP_PREFIX + "merchant_id";
    public final static String MERCHANT_NAME = SP_PREFIX + "merchant_name";
    public final static String USER_NAME = SP_PREFIX + "username";
    public final static String PROXY_USER_NAME = SP_PREFIX + "proxy_username";
    public final static String OPERATION_LOGIN_USER_NAME = SP_PREFIX + "operation_login_username";
    public final static String USER_PASSWORD = SP_PREFIX + "user_password";
    public final static String PROXY_USER_PASSWORD = SP_PREFIX + "proxy_user_password";
    public final static String PROXY_MERCHANT_NAME = SP_PREFIX + "proxy_merchant_nam";
    public final static String USER_ID = SP_PREFIX + "user_id";
    public final static String USER_IDENTITY = SP_PREFIX + "user_identity";
    public final static String ACTION = PACKAGE_NAME + ".action_";
    public final static String ACTION_ADD_DISH = ACTION + "add_dish";  //编辑菜单
    public final static String ACTION_EDIT_DISH = ACTION + "edit_dish";//添加菜单
    public final static String KEY_LOGO_URL = "com.wangxiaobao.waiter.noerp.LOGO_URL";//添加菜单
    public final static String EXTRA_DISH = "com.wangxiaobao.waiter.noerp.EXTRA_DISH";
    public static final String PREFERENCE_PHONE_NUMBER = "phone_number";
    public static final String PREFERENCE_ACCOUNT = "account";

    public static final String PREFERENCE_MERCHANT_ACCOUNT = "preference_merchant_account";
    public static final String SPP = "00001101-0000-1000-8000-00805F9B34FB";
    public static final String BLUETOOTH_DEVICE_NAME = "PrinterService";


    public static final int LONG_TIME_DURATION = 60000;
    public static boolean IS_ERP_YUZHOUREN_MERCHANT = false;
    public static String YUZHOUREN_MERCHANT_LOG_URL;

    public static boolean NEED_SHOW_MAIN_SPLASH = false;

    public static boolean NEED_SHOW_TEMPLATE_SPLASH_FLAG = false;
}
