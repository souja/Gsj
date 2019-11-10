package com.wangxiaobao.pushmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * className   MessageReceiver
 * author      jucongyuan
 * date        3/16/16 2:58 PM
 * description 功能描述
 * versions    版本记录
 */
public class MessageReceiver extends BroadcastReceiver {

    public final static String ACTION_RECEIVED_MESSAGE = "com.wangxiaobao.RECEIVED_ALL_MESSAGE";
    private final static String EXTRA_MESSAGE = "message";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_RECEIVED_MESSAGE)) {
            LogicalClassification logicalClassification = (LogicalClassification) intent.getSerializableExtra(EXTRA_MESSAGE);
            if (logicalClassification != null && logicalClassification.getTag().equals(Constants.UDP_TLVTYPE_MESSAGE_TABLEINFO_RESPONSE)) {


                String strMerchantInfo = (String) logicalClassification.getMap().get(logicalClassification.getTag());


                LogTool.saveLog("==============================收到店铺信息==============================：\n" + strMerchantInfo);





//             MerchantInfo merchantInfo=   new Gson().fromJson(strMerchantInfo,MerchantInfo.class);
//             if (!merchantInfo.getMerchantId().equals(MerchantDao.getInstance().getMerchantId())){
//                 MerchantDao.getInstance().updateMerchantInfo(merchantInfo);
//                 PushManager.getInstance().checkMerchantAccount();
//
//                 LogTool.saveLog("店铺信息发生变化");
//             }










            }
        }
    }

}
