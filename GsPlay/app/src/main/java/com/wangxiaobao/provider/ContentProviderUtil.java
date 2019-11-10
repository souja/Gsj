package com.wangxiaobao.provider;

import android.content.Context;
import android.os.Bundle;

import com.wangxiaobao.pushmanager.LogTool;


/**
 * Created by jack on 2018/1/26.
 */

public class ContentProviderUtil {


    /**
     * 从adplayer获取店铺账号要信息
     * @param context
     * @return
     */

    public  static String getMerchantAccount(Context context){
        try {
            Bundle bundle= context.getContentResolver().call(ContentProviderConstant.DownloaderContentProvider_URI,"getMerchantAccount",null,null);

            return bundle.getString(ContentProviderConstant.EXTRA_MESSAGE);
        }catch (Exception e){
            LogTool.saveLog("获取店铺信息发生异常:",e);
            return "";
        }




    }
}
