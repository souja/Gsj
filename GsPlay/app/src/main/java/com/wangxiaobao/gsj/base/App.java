package com.wangxiaobao.gsj.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.wangxiaobao.gsj.common.AppConfig;
import com.wangxiaobao.gsj.common.CommonUtil;
import com.wangxiaobao.gsj.common.CrashHandler;
import com.wangxiaobao.gsj.common.LogTool;
import com.wangxiaobao.pushmanager.GsjService;
import com.wangxiaobao.waiter.BuildConfig;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


public class App extends MultiDexApplication {

    private static final String JSON_TAG = "Json";
    public static final String ACTION_OPEN_DEBUG = "com.wangxiaobao.waiter.ACTION_OPEN_DEBUG";

    /**
     * 是否加载商家相关的投诉列表
     */
    public boolean isLoadMerchantList;

    /**
     * 上一次操作的时间戳
     */
    public long lastOperateTime;

    /**
     * 大屏id
     */
    public static App mApp;


    public static void jumpToLogin(Context context) {
        CommonUtil.clearProperty(context, AppConfig.USER_PASSWORD);
        CommonUtil.clearProperty(context, AppConfig.PROXY_USER_PASSWORD);
        context.sendBroadcast(new Intent(BaseActivity.INIENT_FINISH));
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
//        if (BuildConfig.DEBUG) {
//        Timber.plant(new Timber.DebugTree());
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                .detectAll()
//                .penaltyLog()
//                .build());
//        StrictMode.ThreadPolicy.Builder threadPolicyBuilder = new StrictMode.ThreadPolicy.Builder()
//                .detectAll()
//                .penaltyLog();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            threadPolicyBuilder.penaltyDeathOnNetwork();
//        }
//        StrictMode.setThreadPolicy(threadPolicyBuilder.build());


    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
//        configUmeng();

//        configHttp();
        Logger.init(JSON_TAG);

        CrashReport.initCrashReport(getApplicationContext(), AppConfig.APP_ID, false);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_OPEN_DEBUG);
        registerReceiver(mBroadcastReceiver, intentFilter);
        if (BuildConfig.SERVER_MODE != 4) {
            Stetho.initializeWithDefaults(this);
        }

        startService(new Intent(this, GsjService.class));

        new CrashHandler();

        LogTool.saveLog("工商局项目创建成功!");
    }

    private void configHttp() {
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG")) 日志//
                .cookieJar(cookieJar) //自动保存session
                .connectTimeout(30000L, TimeUnit.MILLISECONDS)
                .readTimeout(30000L, TimeUnit.MILLISECONDS)
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)//允许访问所有的https网站
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) return;
            if (intent.getAction().equals(ACTION_OPEN_DEBUG)) {
                if (intent.getAction().equals(ACTION_OPEN_DEBUG)) {
                    LogTool.DEBUG = true;
                }
            }
        }

    };


    private void configUmeng() {
        try {
            MobclickAgent.setDebugMode(false);
            MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
            MobclickAgent.openActivityDurationTrack(false);
            MobclickAgent.setCatchUncaughtExceptions(false);
        } catch (Exception e) {
        }
    }


    public static Context getContext() {
        return mApp.getApplicationContext();
    }
}
