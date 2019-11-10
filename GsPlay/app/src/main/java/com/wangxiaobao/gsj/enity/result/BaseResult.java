package com.wangxiaobao.gsj.enity.result;

import java.io.Serializable;

/**
 * 网络请求返回基本数据
 */
public class BaseResult implements Serializable{
    protected String code="";
    protected String message="";

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


    @Override
    public String toString() {
        return "BaseResult{" +
                "code='" + code + '\'' +
                ", errorMessage='" + message + '\'' +
                '}';
    }
}
