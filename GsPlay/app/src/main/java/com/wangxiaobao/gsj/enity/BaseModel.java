package com.wangxiaobao.gsj.enity;

import com.facebook.stetho.common.LogUtil;
import com.google.gson.Gson;


import java.io.Serializable;

/**
 * BaseModel
 * Created by Ydz on 2019/3/11 0011 16:49
 */
public class BaseModel implements Serializable {

    public String toString() {
        String jsonStr = new Gson().toJson(this);
        LogUtil.e(jsonStr);
        return jsonStr;
    }
}
