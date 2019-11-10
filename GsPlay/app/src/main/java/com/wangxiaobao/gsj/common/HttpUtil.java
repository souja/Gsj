package com.wangxiaobao.gsj.common;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.wangxiaobao.gsj.enity.result.JsonListResult;
import com.wangxiaobao.gsj.enity.result.JsonResult;
import com.wangxiaobao.gsj.http.Constant;
import com.wangxiaobao.gsj.base.App;
import com.wangxiaobao.gsj.enity.result.BaseResult;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.OkHttpRequestBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class HttpUtil {


    static volatile HttpUtil singleton = null;

    final Context context;
    public static final String TAG = HttpUtil.class.getSimpleName();
    public static final int REQUEST_TYPE_GET = 0;
    public static final int REQUEST_TYPE_POST = 1;
    private String mDefaultErrorTip;
    private String mDefaultSuccessTip;


    public static final int DATA_TYPE_ENTITY = 1;
    public static final int DATA_TYPE_LIST = 2;

    private HttpUtil(Context context, String defaultErrorTip, String defaultSuccessTip) {
        this.context = context;
        this.mDefaultErrorTip = defaultErrorTip;
        this.mDefaultSuccessTip = defaultSuccessTip;
        configDefaultHttp();
    }

    private void configDefaultHttp() {


    }


    public static class Builder {
        final Context context;
        String defaultErrorTip;
        String defaultSuccessTip;

        public Builder(Context context) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null.");
            }
            this.context = context.getApplicationContext();
        }


        public HttpUtil build() {
            Context context = this.context;
            if (TextUtils.isEmpty(defaultErrorTip)) {
                defaultErrorTip = "真淘气，你的网络状态不好，请稍后再试";
            }
            if (TextUtils.isEmpty(defaultSuccessTip)) {
                defaultSuccessTip = "数据加载成功";
            }
            return new HttpUtil(context, defaultErrorTip, defaultSuccessTip);
        }
    }


    public RequestCreator request(int requestType) {
        return new RequestCreator(requestType);
    }

    public <T> RequestCreator post() {
        return new RequestCreator(HttpUtil.REQUEST_TYPE_POST);
    }

    public <T> RequestCreator get() {
        return new RequestCreator<T>(HttpUtil.REQUEST_TYPE_GET);
    }


    public class RequestCreator<T> {
        public RequestCreator setRequestType(int requestType) {
            this.requestType = requestType;
            return this;
        }

        int requestType = HttpUtil.REQUEST_TYPE_POST;
        Map<String, String> params;
        Class entityClass;
        Type type;
        int dataType = DATA_TYPE_LIST;
        String errorTip;

        String url;


        public RequestCreator setUrl(String url) {
            this.url = url;
            return this;
        }

        public RequestCreator setErrorTip(String errorTip) {
            this.errorTip = errorTip;
            return this;
        }


        public RequestCreator setDataType(int dataType) {
            this.dataType = dataType;
            return this;
        }


        public RequestCreator setType(Type type) {
            this.type = type;
            return this;
        }

        public RequestCreator type() {
            this.type = new TypeToken<T>() {
            }.getType();
            return this;
        }


        public RequestCreator() {

        }


        public RequestCreator(int requestType) {
            this.requestType = requestType;
        }


        public RequestCreator setParams(Map<String, String> params) {
            this.params = params;
            return this;
        }

        public RequestCreator setResponseClass(Class entityClass) {
            this.entityClass = entityClass;
            return this;
        }

        public <T> void load(String action, RequestCallback<T> requestCallback) {
            singleton.load(requestType, action, params, entityClass, requestCallback);
        }

        public <T> void load(String action, BaseRequestCallback<T> requestCallback) {
            singleton.load(requestType, action, params, type, requestCallback, errorTip);
        }

        public <T> void load(String action, NewEntityCallback<T> requestCallback, int maxTime) {
            singleton.load(requestType, action, params, type, requestCallback, errorTip, maxTime);
        }


        public void load(CallBack<T> callBack) {
            load(requestType, url, params, DATA_TYPE_LIST, callBack, errorTip);
        }


        public void load(int requestType, final String action, Map<String, String> params, final int type, final NewEntityCallback<T> requestCallback, final String  errorTip) {

            LogTool.i(TAG, "\n\n=============================   post start " + "    =========================== ");
            if (params == null) {
                params = new HashMap<>();
                params.put("", "");
            }

            RequestCall requestCall;
            if (requestType == HttpUtil.REQUEST_TYPE_GET) {
                requestCall = getGetCall(action, params);
            } else {
                requestCall = getPostCall(action, params);
            }

            requestCall.execute(new StringCallback() {

                @Override
                public void onError(Call call, Exception e, int i) {
                    try {
                        LogTool.saveLog(TAG, "onError", e);
                        if (e.getClass().getSimpleName().equals(IOException.class.getSimpleName())) {
                            if (!TextUtils.isEmpty(errorTip)) {
                                CommonUtil.showShortToast(context, errorTip);
                            } else {
                                CommonUtil.showShortToast(context, mDefaultErrorTip);
                            }
                        } else {
                            CommonUtil.showShortToast(context, mDefaultErrorTip);
                        }
                        requestCallback.onError(e);
                    } catch (Exception e1) {
                        LogTool.saveLog(TAG, "onError exception", e1);
                    }
                }

                @Override
                public void onResponse(String response, int i) {
                    LogTool.E(TAG, "HttpUtil onResponse action:" + action + "\nquest response action:" + response + "\n\n");
                    try {
                        if (!TextUtils.isEmpty(response)) {
                            Gson gson = new Gson();
                            if (type == DATA_TYPE_ENTITY) {
                                JsonResult<T> jsonResult = gson.fromJson(response, (new TypeToken<JsonResult<T>>() {
                                }.getType()));
                                if (jsonResult.getCode().equals("8")) {
                                    requestCallback.onSectionSuccess();

                                } else if (!isSuccess(jsonResult)) {
                                    handleFailResult(jsonResult, requestCallback);
                                } else {
                                    requestCallback.onSuccess(jsonResult.getData());
                                }
                            }


                        }
                    } catch (Exception e) {
                        LogTool.saveLog(TAG, "HttpUtil exception action:" + action + "\nresponse handle exception", e);
                        requestCallback.onException();
                    }
                    LogTool.I(TAG, "=============================   Post end" + "    ===========================\n" +
                            "\n");
                }

            });


        }


        public void load(int requestType, final String action, Map<String, String> params, final int type, final CallBack<T> requestCallback, final String errorTip) {

            LogTool.i(TAG, "\n\n=============================   post start " + "    =========================== ");
            if (params == null) {
                params = new HashMap<>();
                params.put("", "");
            }

            RequestCall requestCall;
            if (requestType == HttpUtil.REQUEST_TYPE_GET) {
                requestCall = getGetCall(action, params);
            } else {
                requestCall = getPostCall(action, params);
            }

            requestCall.execute(new StringCallback() {

                @Override
                public void onError(Call call, Exception e, int i) {
                    try {
                        LogTool.saveLog(TAG, "onError", e);
                        if (e.getClass().getSimpleName().equals(IOException.class.getSimpleName())) {
                            if (!TextUtils.isEmpty(errorTip)) {
                                CommonUtil.showShortToast(context, errorTip);
                            } else {
                                CommonUtil.showShortToast(context, mDefaultErrorTip);
                            }
                        } else {
                            CommonUtil.showShortToast(context, mDefaultErrorTip);
                        }
                        requestCallback.onError(e);
                    } catch (Exception e1) {
                        LogTool.saveLog(TAG, "onError exception", e1);
                    }
                }

                @Override
                public void onResponse(String response, int i) {
                    LogTool.E(TAG, "HttpUtil onResponse action:" + action + "\nquest response action:" + response + "\n\n");
                    try {
                        if (!TextUtils.isEmpty(response)) {
                            Gson gson = new Gson();
                            if (type == DATA_TYPE_ENTITY) {
                                JsonResult<T> jsonResult = gson.fromJson(response, (new TypeToken<JsonResult<T>>() {
                                }.getType()));
                                if (!isSuccess(jsonResult)) {
                                    handleFailResult(jsonResult, requestCallback);
                                } else {
                                    requestCallback.onSuccess(jsonResult.getData());
                                }
                            } else {
                                JsonListResult<T> jsonListResult = gson.fromJson(response, (new TypeToken<JsonListResult<T>>() {
                                }.getType()));
                                if (!isSuccess(jsonListResult)) {
                                    handleFailResult(jsonListResult, requestCallback);
                                } else {
                                    requestCallback.onSuccess(jsonListResult.getData());
                                }
                            }


                        }
                    } catch (Exception e) {
                        LogTool.saveLog(TAG, "HttpUtil exception action:" + action + "\nresponse handle exception", e);
                        requestCallback.onException();
                    }
                    LogTool.I(TAG, "=============================   Post end" + "    ===========================\n" +
                            "\n");
                }

            });


        }


    }


    public static HttpUtil with(Context context) {
        if (singleton == null) {
            synchronized (HttpUtil.class) {
                if (singleton == null) {
                    singleton = new Builder(context).build();
                }
            }
        }
        return singleton;
    }


    public static void get(final Context context, int requestType, String action, Map<String, String> params, final Class tClass, final RequestCallback requestCallback) {
        if (params == null) {
            params = new HashMap<>();
            params.put("", "");
        }
        RequestCall requestCall = getGetCall(action, params);

        requestCall.execute(new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int i) {
                try {
                    LogTool.saveLog(TAG, "onError", e);
                    CommonUtil.showShortToast(context, "网络连接失败,请检查网络是否打开");
                    requestCallback.onError(e);
                } catch (Exception e1) {
                    LogTool.saveLog(TAG, "onError exception", e1);
                }
            }

            @Override
            public void onResponse(String response, int i) {
                Logger.t(TAG).i("\n======= Get onResponse Json Data ======\n\n");
                Logger.t(TAG).json(response);


                try {
                    if (!TextUtils.isEmpty(response)) {
                        Gson gson = new Gson();
                        BaseResult baseResult = gson.fromJson(response, BaseResult.class);
                        if (baseResult.getCode().equals(AppConfig.RESULT_SUCCESS)) {
                            requestCallback.onResponse(response, gson.fromJson(response, tClass));
                        } else if (baseResult.getCode().equals(AppConfig.RESULT_SESSION_DEAD + "")) {
                            CommonUtil.showShortToast(context, baseResult.getMessage());
                            App.jumpToLogin(context);
                            LogTool.saveLog(TAG, "RESULT_SESSION_DEAD");
                        } else if (baseResult.getCode().equals(AppConfig.RESULT_EXCEPTION)) {
                            CommonUtil.showShortToast(context, baseResult.getMessage());
                            requestCallback.onResponse(response, gson.fromJson(response, tClass));
                        } else {
                            CommonUtil.showShortToast(context, baseResult.getMessage());
                        }

                    }
                } catch (Exception e) {
                    LogTool.saveLog(TAG, "Get response handle exception", e);
                }
            }

//            @Override
//            public void onError(Request request, Exception e) {
////                LogTool.saveLog(TAG, "onError", e);
////                CommonUtil.showShortToast(context, "网络连接失败,请检查网络是否打开");
////                requestCallback.onError(request, e);
//
//
//
//
//            }
//
//            @Override
//            public void onResponse(String response) {
//
//
//
//
//            }
        });
    }


    public void load(int requestType, final String action, Map<String, String> params, final Class tClass, final RequestCallback requestCallback) {

        LogTool.i(TAG, "\n\n=============================   post start " + "    =========================== ");
        if (params == null) {
            params = new HashMap<>();
            params.put("", "");
        }

        RequestCall requestCall = getRequestCall(requestType, action, params);
        requestCall.execute(new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int i) {
                try {
                    LogTool.saveLog(TAG, "onError", e);
                    CommonUtil.showShortToast(context, mDefaultErrorTip);
                    requestCallback.onError(e);
                } catch (Exception e1) {
                    LogTool.saveLog(TAG, "onError exception", e1);
                }

            }

            @Override
            public void onResponse(String response, int i) {

                LogTool.I(TAG, "========quest response action:" + response + "\n\n");
                LogTool.json(TAG, response);

                try {
                    if (!TextUtils.isEmpty(response)) {
                        Gson gson = new Gson();
                        BaseResult baseResult = gson.fromJson(response, BaseResult.class);
                        if (baseResult.getCode().equals(AppConfig.RESULT_SUCCESS)) {
                            requestCallback.onResponse(response, gson.fromJson(response, tClass));
                        } else if (baseResult.getCode().equals(AppConfig.RESULT_SESSION_DEAD + "")) {
                            processSessionDeadException(baseResult.getMessage());
                        } else if (baseResult.getCode().equals(AppConfig.RESULT_EXCEPTION)) {
                            CommonUtil.showShortToast(context, baseResult.getMessage());
                            requestCallback.onResponse(response, gson.fromJson(response, tClass));
                        } else {
                            CommonUtil.showShortToast(context, baseResult.getMessage());
                        }

                    }
                } catch (Exception e) {
                    LogTool.saveLog(TAG, "Post response handle exception", e);

                }
                LogTool.I(TAG, "=============================   Post end" + "    ===========================\n" +
                        "\n");
            }

        });


    }


    public <T> void load(int requestType, final String action, Map<String, String> params, final Type type, final NewEntityCallback<T> requestCallback,final String errorTip, int maxTime) {

        LogTool.i(TAG, "\n\n=============================   post start " + "    =========================== ");
        if (params == null) {
            params = new HashMap<>();
            params.put("", "");
        }

        RequestCall requestCall = getRequestCall(requestType, action, params);
        requestCall.connTimeOut(60 * 1000);
        requestCall.readTimeOut(60 * 1000);
        requestCall.writeTimeOut(60 * 1000);
        requestCall.execute(new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int i) {
                try {
                    LogTool.saveLog(TAG, "onError", e);
                    if (e.getClass().getSimpleName().equals(IOException.class.getSimpleName())) {
                        if (!TextUtils.isEmpty(errorTip)) {
                            CommonUtil.showShortToast(context, errorTip);
                        } else {
                            CommonUtil.showShortToast(context, mDefaultErrorTip);
                        }
                    } else {
                        CommonUtil.showShortToast(context, mDefaultErrorTip);
                    }
                    requestCallback.onError(e);
                } catch (Exception e1) {
                    LogTool.saveLog(TAG, "onError exception", e1);
                }
            }

            @Override
            public void onResponse(String response, int i) {
                LogTool.E(TAG, "HttpUtil onResponse action:" + action + "\nquest response action:" + response + "\n\n");
//                LogTool.json(TAG, response);
                try {
                    if (!TextUtils.isEmpty(response)) {
                        Gson gson = new Gson();

                        if (requestCallback instanceof EntityCallback) {
                            JsonResult<T> jsonResult = gson.fromJson(response, type);
                            if (jsonResult.getCode().equals(AppConfig.RESULT_SUCCESS)) {
                                T data = jsonResult.getData();
                                EntityCallback<T> entityCallback = (EntityCallback<T>) requestCallback;
                                entityCallback.onSuccess(data);
                            } else if (jsonResult.getCode().equals(8 + "")){
                                requestCallback.onSectionSuccess();
                            }else if (jsonResult.getCode().equals(AppConfig.RESULT_SESSION_DEAD + "")) {
                                processSessionDeadException(jsonResult.getMessage());
                            } else if (jsonResult.getCode().equals(AppConfig.RESULT_EXCEPTION)) {
                                processServerException(requestCallback, jsonResult.getMessage(), 1);
                            } else {
//                                CommonUtil.showShortToast(context, jsonResult.getMessage());
                            }
                        } else {
                            JsonListResult<T> jsonResult = gson.fromJson(response, type);
                            if (jsonResult.getCode().equals(AppConfig.RESULT_SUCCESS)) {
                                List<T> data = jsonResult.getData();
                                ListCallback<T> entityCallback = (ListCallback<T>) requestCallback;
                                entityCallback.onSuccess(data);
                            } else if (jsonResult.getCode().equals(AppConfig.RESULT_SESSION_DEAD + "")) {
                                processSessionDeadException(jsonResult.getMessage());
                            } else if (jsonResult.getCode().equals(AppConfig.RESULT_EXCEPTION)) {
                                processServerException(requestCallback, jsonResult.getMessage(), 1);
                            } else {
//                                CommonUtil.showShortToast(context, jsonResult.getMessage());
                            }


                        }


                    }
                } catch (Exception e) {
                    LogTool.saveLog(TAG, "HttpUtil exception action:" + action + "\nresponse handle exception", e);
                    requestCallback.onException();
                }
                LogTool.I(TAG, "=============================   Post end" + "    ===========================\n" +
                        "\n");
            }

        });


    }

    public static RequestCall getRequestCall(int requestType, String action, Map<String, String> params) {
        return requestType == HttpUtil.REQUEST_TYPE_GET ? getGetCall(action, params) : getPostCall(action, params);
    }


    public interface CallBack<T> extends BaseRequestCallback {
        void onSuccess(T entity);

        void onSuccess(List<T> list);
    }

    private void handleFailResult(BaseResult baseResult, BaseRequestCallback baseRequestCallback) {
        if (baseResult.getCode().equals(AppConfig.RESULT_SESSION_DEAD + "")) {
            processSessionDeadException(baseResult.getMessage());
        } else if (baseResult.getCode().equals(AppConfig.RESULT_EXCEPTION)) {
            processServerException(baseRequestCallback, baseResult.getMessage());
        } else {
            CommonUtil.showShortToast(context, baseResult.getMessage());
        }
    }


    private boolean isSuccess(BaseResult baseResult) {
        return baseResult.getCode().equals(AppConfig.RESULT_SUCCESS + "");
    }

    public <T> void load(int requestType, final String action, Map<String, String> params, final int type, final CallBack<T> requestCallback, final String errorTip) {

        LogTool.i(TAG, "\n\n=============================   post start " + "    =========================== ");
        if (params == null) {
            params = new HashMap<>();
            params.put("", "");
        }

        RequestCall requestCall = getRequestCall(requestType, action, params);
        requestCall.execute(new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int i) {
                try {
                    LogTool.saveLog(TAG, "onError", e);
                    if (e.getClass().getSimpleName().equals(IOException.class.getSimpleName())) {
                        if (!TextUtils.isEmpty(errorTip)) {
                            CommonUtil.showShortToast(context, errorTip);
                        } else {
                            CommonUtil.showShortToast(context, mDefaultErrorTip);
                        }
                    } else {
                        CommonUtil.showShortToast(context, mDefaultErrorTip);
                    }
                    requestCallback.onError(e);
                } catch (Exception e1) {
                    LogTool.saveLog(TAG, "onError exception", e1);
                }
            }

            @Override
            public void onResponse(String response, int i) {
                LogTool.E(TAG, "HttpUtil onResponse action:" + action + "\nquest response action:" + response + "\n\n");
                try {
                    if (!TextUtils.isEmpty(response)) {
                        Gson gson = new Gson();
                        if (type == DATA_TYPE_ENTITY) {
                            JsonResult<T> jsonResult = gson.fromJson(response, (new TypeToken<JsonResult<T>>() {
                            }.getType()));
                            if (!isSuccess(jsonResult)) {
                                handleFailResult(jsonResult, requestCallback);
                            } else {
                                requestCallback.onSuccess(jsonResult.getData());
                            }
                        } else {
                            JsonListResult<T> jsonListResult = gson.fromJson(response, (new TypeToken<JsonListResult<T>>() {
                            }.getType()));
                            if (!isSuccess(jsonListResult)) {
                                handleFailResult(jsonListResult, requestCallback);
                            } else {
                                requestCallback.onSuccess(jsonListResult.getData());
                            }
                        }


                    }
                } catch (Exception e) {
                    LogTool.saveLog(TAG, "HttpUtil exception action:" + action + "\nresponse handle exception", e);
                    requestCallback.onException();
                }
                LogTool.I(TAG, "=============================   Post end" + "    ===========================\n" +
                        "\n");
            }
        });


    }


    public <T> void load(int requestType, final String action, Map<String, String> params, final Type type, final BaseRequestCallback<T> requestCallback,final String errorTip) {

        LogTool.i(TAG, "\n\n=============================   post start " + "    =========================== ");
        if (params == null) {
            params = new HashMap<>();
            params.put("", "");
        }

        RequestCall requestCall = getRequestCall(requestType, action, params);
        requestCall.execute(new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int i) {
                try {
                    LogTool.saveLog(TAG, "onError", e);
                    if (e.getClass().getSimpleName().equals(IOException.class.getSimpleName())) {
                        if (!TextUtils.isEmpty(errorTip)) {
                            CommonUtil.showShortToast(context, errorTip);
                        } else {
                            CommonUtil.showShortToast(context, mDefaultErrorTip);
                        }
                    } else {
                        CommonUtil.showShortToast(context, mDefaultErrorTip);
                    }
                    requestCallback.onError(e);
                } catch (Exception e1) {
                    LogTool.saveLog(TAG, "onError exception", e1);
                }
            }

            @Override
            public void onResponse(String response, int i) {
                LogTool.E(TAG, "HttpUtil onResponse action:" + action + "\nquest response action:" + response + "\n\n");
//                LogTool.json(TAG, response);
                try {
                    if (!TextUtils.isEmpty(response)) {
                        Gson gson = new Gson();

                        if (requestCallback instanceof EntityCallback) {
                            JsonResult<T> jsonResult = gson.fromJson(response, type);
                            if (jsonResult.getCode().equals(AppConfig.RESULT_SUCCESS)) {
                                T data = jsonResult.getData();
                                EntityCallback<T> entityCallback = (EntityCallback<T>) requestCallback;
                                entityCallback.onSuccess(data);
                            } else if (jsonResult.getCode().equals(AppConfig.RESULT_SESSION_DEAD + "")) {
                                processSessionDeadException(jsonResult.getMessage());
                            } else if (jsonResult.getCode().equals(AppConfig.RESULT_EXCEPTION)) {
                                processServerException(requestCallback, jsonResult.getMessage());
                            } else {
                                CommonUtil.showShortToast(context, jsonResult.getMessage());
                            }
                        } else {
                            JsonListResult<T> jsonResult = gson.fromJson(response, type);
                            if (jsonResult.getCode().equals(AppConfig.RESULT_SUCCESS)) {
                                List<T> data = jsonResult.getData();
                                ListCallback<T> entityCallback = (ListCallback<T>) requestCallback;
                                entityCallback.onSuccess(data);
                            } else if (jsonResult.getCode().equals(AppConfig.RESULT_SESSION_DEAD + "")) {
                                processSessionDeadException(jsonResult.getMessage());
                            } else if (jsonResult.getCode().equals(AppConfig.RESULT_EXCEPTION)) {
                                processServerException(requestCallback, jsonResult.getMessage());
                            } else {
                                CommonUtil.showShortToast(context, jsonResult.getMessage());
                            }


                        }


                    }
                } catch (Exception e) {
                    LogTool.saveLog(TAG, "HttpUtil exception action:" + action + "\nresponse handle exception", e);
                    requestCallback.onException();
                }
                LogTool.I(TAG, "=============================   Post end" + "    ===========================\n" +
                        "\n");
            }

        });


    }

    private void processServerException(BaseRequestCallback requestCallback, String exceptionMessage) {
        CommonUtil.showShortToast(context, exceptionMessage);
        requestCallback.onException();
    }

    private void processServerException(BaseRequestCallback requestCallback, String exceptionMessage, int a) {
        requestCallback.onException();
    }

    private void processSessionDeadException(String exceptionMessage) {
        CommonUtil.showShortToast(context, exceptionMessage);
        App.jumpToLogin(context);
        LogTool.saveLog(TAG, "RESULT_SESSION_DEAD");
    }



    public static void post(final Context context, int requestType, final String action, Map<String, String> params, final Class tClass, final RequestCallback requestCallback) {

        LogTool.i(TAG, "\n\n=============================   post start " + "    =========================== ");
        if (params == null) {
            params = new HashMap<>();
            params.put("", "");
        }

        RequestCall requestCall = getRequestCall(requestType, action, params);
        requestCall.execute(new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int i) {
                try {
                    LogTool.saveLog(TAG, "onError", e);
                    CommonUtil.showShortToast(context, "真淘气，你的网络状态不好，请稍后再试");
                    requestCallback.onError(e);
                } catch (Exception e1) {
                    LogTool.saveLog(TAG, "onError exception", e1);
                }
            }

            @Override
            public void onResponse(String response, int i) {
                LogTool.i(TAG, "\n======= Post onResponse Json Data ======\n\n");
                LogTool.json(TAG, response);
                try {
                    if (!TextUtils.isEmpty(response)) {
                        Gson gson = new Gson();
                        BaseResult baseResult = gson.fromJson(response, BaseResult.class);
                        if (baseResult.getCode().equals(AppConfig.RESULT_SUCCESS)) {
                            requestCallback.onResponse(response, gson.fromJson(response, tClass));
                        } else if (baseResult.getCode().equals(AppConfig.RESULT_SESSION_DEAD + "")) {
                            CommonUtil.showShortToast(context, baseResult.getMessage());
                            App.jumpToLogin(context);
                            LogTool.saveLog(TAG, "RESULT_SESSION_DEAD action:" + action);

                        } else if (baseResult.getCode().equals(AppConfig.RESULT_EXCEPTION)) {
                            CommonUtil.showShortToast(context, baseResult.getMessage());
                            requestCallback.onResponse(response, gson.fromJson(response, tClass));
                        }

                    }
                } catch (Exception e) {
                    LogTool.saveLog(TAG, "Post response handle exception", e);

                }
                LogTool.i(TAG, "=============================   Post end" + "    ===========================\n" +
                        "\n");
            }


        });


    }


    public static String buildUrl(String action) {
        String url;
        if (Constant.ACTION_SET_ALL_QRCODE.equals(action) || Constant.ACTION_DELETE_QR_URL.equals(action) || Constant.ACTION_CONFIG_TABLE.equals(action) || Constant.ACTION_FIND_TABLE_LIST.equals(action) || Constant.ACTION_GET_QINIU_TOKEN.equals(action) || Constant.ACTION_RESET_PASSWORD.equals(action) || Constant.ACTION_LOGIN.equals(action) || Constant.ACTION_GET_VERIFY_CODE.equals(action) || Constant.ACTION_CHECK_VERIFY_CODE.equals(action)) {
            url = Constant.LOGIN_URL + action;
        } else {
//            if (BuildConfig.IS_ERP) {
//                if (Constant.ACTION_DELETE_DISH.equals(action)) {
//                    url = Constant.HOST_ERP + "api/wzp/dish/erp/deleteDish.htm";
//                } else if (Constant.ACTION_DISH_UPDATE.equals(action)) {
//                    url = Constant.HOST_ERP + "api/wzp/dish/erp/updateDish.htm";
//                } else if (Constant.ACTION_DISH_CREATE.equals(action)) {
//                    url = Constant.HOST_ERP + "api/wzp/dish/erp/createDish.htm";
////                    url = "http://172.16.1.42:8080/tablecard-wap-diancaibao/" + "api/wzp/dish/erp/createDish.htm";
//                } else if (Constant.ACTION_GET_ERP_MERCHANT_NAME.equals(action)) {
//                    url = Constant.HOST_ERP + action;
//                } else if (Constant.ACTION_DISH_SALE_UPDATE.equals(action)) {
//                    url = Constant.HOST_ERP + "api/wzp/dish/erp/updateDishSale.htm";
//                } else if (Constant.ACTION_UPDATE_DISH_ACTIVE.equals(action)) {
//                    url = Constant.HOST_ERP + "api/wzp/dish/erp/updateDishActive.htm";
//                } else {
//                    url = Constant.DATA_URL + action;
//                }
//            } else {
                url = Constant.DATA_URL + action;
//            }

        }
        return url;
    }


    public static RequestCall getGetCall(String action, Map<String, String> params) {
        GetBuilder builder = OkHttpUtils.get();
        builder.addHeader(Constant.HEADER_KEY, Constant.HEADER_VALUE);
        builder.tag(action);
        String url = buildUrl(action);
        LogTool.E(TAG, "请求地址：" + url);
        LogTool.E(TAG, "请求参数：" + params.toString());
        builder.url(url);
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addParams(entry.getKey(), entry.getValue());
            }
        }

        return builder.build();
    }


    public static RequestCall getPostCall(String action, Map<String, String> params) {
        PostFormBuilder builder = OkHttpUtils.post();
        builder.tag(action);
        String url = buildUrl(action);
        LogTool.E(TAG, "请求地址：" + url);
        LogTool.E(TAG, "请求参数：" + params.toString());
        builder.url(url);
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addParams(entry.getKey(), entry.getValue());
            }
        }

        return builder.build();
    }

    private static PostFormBuilder getPostBuilder() {
        PostFormBuilder builder = OkHttpUtils.post();
        builder.addHeader(Constant.HEADER_KEY, Constant.HEADER_VALUE);
        return builder;
    }

    /**
     * 产生通用的builder;
     *
     * @return
     */
    private static OkHttpRequestBuilder getGetBuilder() {
        OkHttpRequestBuilder builder = OkHttpUtils.get();
        builder.addHeader(Constant.HEADER_KEY, Constant.HEADER_VALUE);
        return builder;
    }


//    /**
//     * 产生通用的RequestCall;
//     *
//     * @param action
//     * @param params
//     * @return
//     */
//    private static RequestCall getRequestCall(int requestType, String action, Map<String, String> params) {
//        OkHttpRequestBuilder builder;
//        if (requestType == REQUEST_TYPE_GET) {
//            builder = getGetBuilder();
//        } else {
//            builder = getPostBuilder();
//        }
//        builder.tag(action);
//        String url;
//        if (action.equals(Constant.ACTION_CHECK_VERSION)) {
//            url = action;
//        } else {
//            if (action.equals(Constant.ACTION_GET_QINIU_TOKEN)) {
//                url = Constant.QINIU_URL + action;
//            } else {
//                url = Constant.DATA_URL + action;
//            }
//        }
//
//
//        builder.url(url);
//
//
//        if (params == null) {
//            params = new HashMap<>();
//            params.put("", "");
//        }
//        if (params != null) {
//            for (Map.Entry<String, String> entry : params.entrySet()) {
//                builder.addParams(entry.getKey(), entry.getValue());
//            }
//        }
//
//        LogTool.E(TAG, "请求接口：" + url);
//        LogTool.E(TAG, "请求参数：" + params.toString());
//        return builder.build();
//    }

    public interface RequestCallback<T> {
        void onError(Exception e);

        void onResponse(String response, T t);
    }


    public interface BaseRequestCallback<T> {
        void onError(Exception e);

        void onException();
    }

    public interface EntityCallback<T> extends BaseRequestCallback<T> {
        void onSuccess(T entity);
    }


    public interface NewEntityCallback<T> extends EntityCallback<T> {
        void onSectionSuccess();
    }

    public interface ListCallback<T> extends BaseRequestCallback {
        void onSuccess(List<T> list);
    }


}
