package com.wangxiaobao.pushmanager;

import android.os.Environment;

import com.wangxiaobao.waiter.BuildConfig;


/**
 * className   Constants
 * author      jucongyuan
 * date        4/19/16 3:55 PM
 * description 功能描述
 * versions    版本记录
 */
public class Constants {

    // UDP桌牌绑定信息返回结果
    public static final String UDP_TLVTYPE_MESSAGE_TABLEINFO_RESPONSE = "T02";

    public static String getServerType() {
        int ServiceMode = BuildConfig.SERVER_MODE;
        String serverName = "";
        switch (ServiceMode) {
            case 1:
                serverName = "DEV";
                break;
            case 2:
                serverName = "DEV";
                break;
            case 3:
                serverName = "PREVIEW";
                break;
            case 4:
                serverName = "PRODUCTION";
                break;

            default:
                serverName = "PRODUCTION";
                break;
        }

        return serverName + "_";

    }


    public static final String getHost() {

        String host;
        switch (BuildConfig.SERVER_MODE) {
            case 1:
                host = "http://develop.source3g.com:57567/";
                break;
            //debug  测试环境
            case 2:
                host = "http://develop.source3g.com:57567/";
//                host = "http://dev.source3g.com:13016/";
                break;
            //preview
            case 3:
                host = "http://preview.source3g.com:13016/";
                break;
            case 4:
//                    https://decoder.wangxiaobao.com/  集码器app权限正式环境地址
                host = "https://decoder.wangxiaobao.com/ ";
                break;
            default:
                host = "https://decoder.wangxiaobao.com/ ";
                break;
        }
        return host;
    }


    public static final String BASE_FOLDER = Environment.getExternalStorageDirectory().getPath() + "/wangxiaobao";
    public static final String LOG_PATH = BASE_FOLDER + "/log/";

}
