package com.wangxiaobao.gsj.log;

/**
 * Created by candy on 17-11-13.
 */

public class QiNiuTokenEntity {
    protected int code;
    protected String message = "";
    private String data;




    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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
}
