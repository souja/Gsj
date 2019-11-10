package com.wangxiaobao.gsj.log;


import com.google.gson.Gson;

/**
 * gson的工具类
 * Created by wenchaoy on 2017/3/21.
 */
public class GsonInstance {
    private static Gson instance;
    public static Gson getInstance(){
        if (instance == null){
            instance  = new Gson();
        }
        return instance;
    }
}
