package com.wangxiaobao.gsj.base;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.wangxiaobao.gsj.common.LogTool;

/**
 * Created by candy on 17-10-31.
 */

public abstract class SaveActivity extends DataActivity {
    private static final String TAG = SaveActivity.class.getSimpleName();

    protected void openSaveAlertDialog() {
        LogTool.saveLog(TAG, "openSaveAlertDialog");
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).setMessage("确定要放弃此次编辑?").setPositiveButton("放弃保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create();
        alertDialog.show();
    }





    @Override
    protected void initView() {
        super.initView();
        setActionText("保存");
        setActionVisiable(View.VISIBLE);
        disableOnSureClick();
    }

    @Override
    protected void onBackClick() {
        handleBack();
    }

    private void handleBack() {
        if (mTitleBar.mRightAction.isEnabled()) {
            openSaveAlertDialog();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {

        handleBack();

    }
}
