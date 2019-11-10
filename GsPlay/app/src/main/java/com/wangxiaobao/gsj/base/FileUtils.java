package com.wangxiaobao.gsj.base;


import com.wangxiaobao.gsj.common.CommonUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by candy on 17-11-13.
 */

public class FileUtils {
    public static String getZipFileName(String fileName){
       String zipFileName = fileName +"_" + CommonUtil.getMerchantAccount(App.getContext()) + "_" + getCurrentTime()+".zip";
        return  zipFileName;
    }
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}
