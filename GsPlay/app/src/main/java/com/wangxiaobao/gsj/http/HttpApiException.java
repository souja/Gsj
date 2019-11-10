package com.wangxiaobao.gsj.http;

/**
 * Created by candy on 17-10-27.
 */

public class HttpApiException extends RuntimeException{

    public String errorMessage;
    public int code;
    public HttpApiException(int code, String errorMessage){
        super();
        this.code = code;
        this.errorMessage = errorMessage;

    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


}
