package com.wangxiaobao.gsj.common;

import android.text.TextUtils;

/**
 * Created by Administrator on 2016-04-13.
 */
public class DigitTools {
    public static float getFloat(String s) {
        float re = 0.0f;
        try {
            re = Float.valueOf(s.toString().trim());
        } catch (NumberFormatException e) {
            return re;
        }
        return re;
    }

    public static String getSimpleFloat(float f) {
        int i = (int) f;
        if (f - i == 0) {
            return i + "";
        } else
            return f + "";
    }

    /**
     * 强制转化成int,return -1代表转换异常
     * @param s
     * @return
     */
    public  static  int String2Int(String s){
        if (TextUtils.isEmpty(s)){
            return  -1;
        }
        int i =-1;
        try {
            i = Integer.parseInt(s.trim());
        }catch (NumberFormatException e){
            return  i;
        }
        return  i;
    }
}
