package com.wangxiaobao.gsj.http;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * 服务器通用返回数据格式
 * Created by jaycee on 2017/6/23.
 */
public class Response<T> {

    private int code;
    private String message;
    private String msg;

    public void setData(T data) {
        this.data = data;
    }

    @SerializedName("data")
    private T data;

    public boolean isSuccess() {
        return code == HttpConstant.RESULT_SUCCESS;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        if (TextUtils.isEmpty(message)) {
            return msg;
        }
        return message;
    }

    public T getData() {
        return data;
    }


}
