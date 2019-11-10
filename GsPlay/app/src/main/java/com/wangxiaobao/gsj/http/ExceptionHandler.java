package com.wangxiaobao.gsj.http;

import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.wangxiaobao.gsj.common.LogTool;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;
import java.net.UnknownHostException;

import retrofit2.HttpException;


public class ExceptionHandler {

    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;
    private static final String TAG = ExceptionHandler.class.getSimpleName();

    public static ResponseException handleException(Throwable e) {

        LogTool.saveLog(TAG,"handleException",e);

        ResponseException ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ResponseException(e, ERROR.HTTP_ERROR);
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    ex.errorMessage = "网络错误";
                    break;
            }
            return ex;
        }else  if (e instanceof UnknownHostException){
            ResponseException responseException=new ResponseException(e,ERROR.HTTP_ERROR);
            responseException.errorMessage = "网络错误";
            return responseException;
        }

        else if (e instanceof HttpApiException) {
            HttpApiException resultException = (HttpApiException) e;
            ex = new ResponseException(resultException, resultException.code);
            ex.errorMessage = resultException.errorMessage;
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new ResponseException(e, ERROR.PARSE_ERROR);
            ex.errorMessage = "解析错误";
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ResponseException(e, ERROR.NETWORD_ERROR);
            ex.errorMessage = "连接失败";
            return ex;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new ResponseException(e, ERROR.SSL_ERROR);
            ex.errorMessage = "证书验证失败";
            return ex;
        } else if (e instanceof ConnectTimeoutException){
            ex = new ResponseException(e, ERROR.TIMEOUT_ERROR);
            ex.errorMessage = "连接超时";
            return ex;
        } else if (e instanceof java.net.SocketTimeoutException) {
            ex = new ResponseException(e, ERROR.TIMEOUT_ERROR);
            ex.errorMessage = "连接超时";
            return ex;
        }
        else {
            ex = new ResponseException(e, ERROR.UNKNOWN);
            ex.errorMessage = "未知错误";
            return ex;
        }


    }


    /**
     * 约定异常
     */
    class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        public static final int NETWORD_ERROR = 1002;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 1003;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = 1005;

        /**
         * 连接超时
         */
        public static final int TIMEOUT_ERROR = 1006;
    }

}

