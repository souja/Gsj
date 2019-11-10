package com.wangxiaobao.gsj.enity.result;

import java.io.Serializable;

/**
 * 网络请求返回基本数据
 */
public class BaseStringResult implements Serializable {
    protected String code = "";
    protected String message = "";
    private String data;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }




    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "BaseStringResult{" +
                "code='" + code + '\'' +
                ", errorMessage='" + message + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
