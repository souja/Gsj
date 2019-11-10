package com.wangxiaobao.gsj.util;

/**
 * Created by ijays on 2018/8/30.
 */
public class ClickUtil {
    private static final int DOUBLE_CLICK_TIME = 800;
    private static long lastClickTime;


    public static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < DOUBLE_CLICK_TIME) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
