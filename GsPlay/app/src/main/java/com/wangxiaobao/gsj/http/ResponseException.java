package com.wangxiaobao.gsj.http;

/**
 * Created by candy on 17-10-27.
 */
public class ResponseException extends Exception {
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int code;

    public String getErrorMessage() {
        return errorMessage;
    }

    public String errorMessage;



    public ResponseException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;

    }
}
