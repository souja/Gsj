package com.wangxiaobao.gsj.base;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.wangxiaobao.gsj.common.LogTool;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
//import com.wangxiaobao.SmartTable.rxbus2.RxBus;

/**
 * Created by candy on 17-10-30.
 */

public class ProgressDialogManager {
    private boolean stoped;
    protected ProgressDialog progressDialog;
    private static final String TAG = ProgressDialogManager.class.getSimpleName();
    private Context mContext;

    private String name;

    public ProgressDialogManager(Activity context) {
        mContext = context;
        name = mContext.getClass().getSimpleName();
        progressDialog = new ProgressDialog(context);
    }


    public void stop() {
        stoped = true;
        LogTool.saveLog(TAG, name + "停止对话框");
        EventBus.getDefault().unregister(this);
    }

    public void resume() {
        stoped = false;

        EventBus.getDefault().register(this);
        LogTool.saveLog(TAG, name + "唤醒对话框");
    }


    public void hideWaitDialog() {
        LogTool.saveLog(TAG, name + "隐藏进度对话框");
        try {
            if (progressDialog != null) {
                progressDialog.hide();
            }
        } catch (Exception e) {
            LogTool.saveLog(TAG, "隐藏进度对话框异常", e);
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(DialogEvent dialogEvent) {
        if (dialogEvent.eventCode == DialogEvent.EVENT_DISMISS_DIALOG) {
            dismissDialog();
        } else if (dialogEvent.eventCode == DialogEvent.EVENT_HIDE_DIALOG) {
            hideWaitDialog();
        } else if (dialogEvent.eventCode == DialogEvent.EVENT_SHOW_DIALOG) {
            showWaitDialog(dialogEvent.getMessage(), dialogEvent.isCancelable());
        }
    }


    public void dismissDialog() {
        LogTool.saveLog(TAG, name + "关闭进度对话框");
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            LogTool.saveLog(TAG, "关闭进度对话框异常", e);
        }
    }


    protected void showWaitDialog(String message, boolean cancelable) {
        LogTool.saveLog(TAG, name + "显示对话框，对话框内容：" + message);
        if (stoped) {
            LogTool.saveLog(TAG, name + "对话框已经被停止");
            return;
        }
        if (TextUtils.isEmpty(message)) {
            message = "正在加载数据...";
        }
        try {
            if (progressDialog == null) {
                LogTool.saveLog(TAG, "加载对话框还没有初始化");
                progressDialog = new ProgressDialog(mContext);
            }
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(cancelable);
            progressDialog.show();
        } catch (Exception e) {
            LogTool.saveLog(TAG, "showWaitDialog exception", e);
        }


    }


}
