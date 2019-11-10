package com.wangxiaobao.gsj.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.wangxiaobao.gsj.common.LogTool;
import com.wangxiaobao.gsj.common.LogUtils;
import com.wangxiaobao.gsj.log.QiNiuTool;
import com.wangxiaobao.pushmanager.CommonUtils;
import com.wangxiaobao.waiter.R;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by candy on 18-3-16.
 */

public class DebugActivity extends BaseActivity {
    @BindView(R.id.tv_device_id)
    TextView tvDeviceId;


    @Override
    protected int generateLayoutId() {
        return R.layout.activity_debug;
    }


    public static final int DISABLE_EXPAND = 0x00010000;//4.2以上的整形标识
    public static final int DISABLE_EXPAND_LOW = 0x00000001;//4.2以下的整形标识
    public static final int DISABLE_NONE = 0x00000000;
    //    0x00000000


    @Override
    protected void initView() {
        super.initView();
        tvDeviceId.setText(CommonUtils.getSn());
    }

    /**
     * 禁止状态栏
     */
    @SuppressLint("WrongConstant")
    public void disableStatusBar() {
        Object service = getSystemService("statusbar");
        try {
            Class<?> statusBarManager = Class
                    .forName("android.app.StatusBarManager");
            Method expand = statusBarManager.getMethod("disable", int.class);
            //判断版本大小
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                expand.invoke(service, DISABLE_EXPAND);
            } else {
                expand.invoke(service, DISABLE_EXPAND_LOW);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnClick(R.id.crash)
    public void crash() {
//        CrashReport.testJavaCrash();


        if (true) {
            throw new RuntimeException("产生一个崩溃");
        }
    }


    @OnClick(R.id.exit)
    public void exit() {
//        CrashReport.testJavaCrash();

        //关闭所有界面
        Integer code = 111;
        EventBus.getDefault().post(code);
        System.exit(-1);
        if (true) {
            throw new RuntimeException("产生一个崩溃,退出程序");
        }
    }

    /**
     * 上传日志文件
     */
    private void uploadFile() {
        String zipFileName = FileUtils.getZipFileName("Gsj");
        LogTool.saveLog("需要上传的路径:" + LogUtils.LOG_PATH + "  zipFileName:" + zipFileName);
        if (!TextUtils.isEmpty(zipFileName)) {

            QiNiuTool.uploadFile(LogUtils.LOG_PATH, "", zipFileName);

        }
    }


    @OnClick(R.id.upload_log_file)
    public void uploadLogFile() {
        uploadFile();
    }

    @OnClick(R.id.finish)
    public void back() {
        finish();
    }


    /**
     * 隐藏状态栏
     */
    private void hideNavigationbar() {
        setNavigationBar(this, View.GONE);
    }

    /**
     * 隐藏状态栏
     */
    private void displayNavigationbar() {
        setNavigationBar(this, View.VISIBLE);
    }


    /**
     * @param activity
     * @param
     */
    public static void setNavigationBar(Activity activity, int visible) {
        View decorView = activity.getWindow().getDecorView();
        //显示NavigationBar
        if (View.GONE == visible) {
            int option = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            decorView.setSystemUiVisibility(option);
        }
    }


    @OnClick(R.id.jump_to_login)
    public void jumpToLogin() {
        startActivity(new Intent(mContext, LoginActivity.class));
//        Constant.Event event =new Constant.Event();
//        event.code=0x2222;
        Integer code = 111;
        EventBus.getDefault().post(code);
    }


    @OnClick(R.id.enable_navigation)
    public void enableNavigation() {
        View decorView = getWindow().getDecorView();
        //显示NavigationBar

        int option = View.SYSTEM_UI_FLAG_VISIBLE;
        decorView.setSystemUiVisibility(option);
    }

    @OnClick(R.id.disable_navigation)
    public void disableNavigation() {


        hideNavigationbar();


    }

    @OnClick(R.id.enable_statusbar)
    public void enableStatusbar() {
        Object service = getSystemService("statusbar");
        try {
            Class<?> statusBarManager = Class
                    .forName("android.app.StatusBarManager");
            Method expand = statusBarManager.getMethod("disable", int.class);
            //判断版本大小
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                expand.invoke(service, DISABLE_NONE);
            } else {
                expand.invoke(service, DISABLE_NONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.disable_statusbar)
    public void disableStatusbar() {
        disableStatusBar();

    }

    @OnClick(R.id.bt_go_setting)
    void onClickGoSetting() {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        startActivity(intent);
    }


}
