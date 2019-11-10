package com.wangxiaobao.pushmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by wave on 2018/1/14.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            LogTool.saveLog("收到开机启动广播");

            context.startService(new Intent(context, GsjService.class));

        }else if (intent.getAction().equals("com.wangxiaobao.action.UPDATE_MERCHANT_ACCOUNT")){


                LogTool.saveLog("收到店铺账号发生变更广播");

//                MerchantDao.getInstance().setMerchantAccount(intent.getStringExtra("extra_message"));
        }

    }
}
