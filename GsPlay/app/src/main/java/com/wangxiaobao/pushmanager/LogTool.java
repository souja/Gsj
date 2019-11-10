package com.wangxiaobao.pushmanager;



import com.wangxiaobao.gsj.common.LogUtils;

public class LogTool {


    public static void saveLog(String content) {

        LogUtils.saveLog(content);



    }

    public static void saveLog(String content, Exception e) {
        LogUtils.saveLog(content,e);


    }
}
