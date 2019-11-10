package com.wangxiaobao.gsj.http;


import com.wangxiaobao.gsj.base.App;
import com.wangxiaobao.gsj.common.LogTool;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.https.HttpsUtils;


import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.*;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * BaseRetrofit
 */
public class BaseRetrofit {


    private static final String TAG = BaseRetrofit.class.getSimpleName();
    private static Api service;
    protected static Retrofit mRetrofitClient;


    private static int CONNECT_TIME_OUT = 30;
    private static int READ_TIME_OUT = 30;


    public Api getService() {
        if (service == null) {
            service = getRetrofitClient().create(Api.class);
        }
        return service;
    }

    public final static String createWebUrl(String url) {
        return Constant.HOST_WEB_BASE_URL + url;
    }

    public static ErrorTransformer errorTransformer = new ErrorTransformer();

    //处理错误的变换
    private static class ErrorTransformer<T> implements ObservableTransformer {

        @Override
        public ObservableSource apply(Observable upstream) {
            //onErrorResumeNext当发生错误的时候，由另外一个Observable来代替当前的Observable并继续发射数据
            return (Observable<T>) upstream.map(new HandleFuc<T>()).onErrorResumeNext(new HttpResponseFunc<T>());
        }
    }


    public static class HttpResponseFunc<T> implements Function<Throwable, Observable<T>> {
        @Override
        public Observable<T> apply(Throwable throwable) throws Exception {
            return Observable.error(ExceptionHandler.handleException(throwable));
        }
    }


    public static <T> Observable<T> startRequest(Observable<Response<T>> observable) {
        return observable.compose(RxSchedulers.compose()).compose(RetrofitClient.errorTransformer);
    }

    public static Observable getResponse(Observable<Response> observable) {
        return observable.compose(RxSchedulers.compose()).compose(RetrofitClient.errorTransformer);
    }


    /**
     * 转换数据
     *
     * @param <T>
     */
    public static class HandleFuc<T> implements Function<Response<T>, T> {
        @Override
        public T apply(Response<T> response) throws Exception {
            if (!response.isSuccess()) {
                throw new HttpApiException(response.getCode(), response.getMessage() != null ? response.getMessage() : "");
            }
            if (response.getData() == null) {

                response.setData((T) new Response<>());
            }
            return response.getData();
        }
    }


    /**
     * create you Api
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     */
    public <T> T create(final Class<T> service) {

        LogTool.saveLog(TAG, "create service " + service.getSimpleName());
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return mRetrofitClient.create(service);
    }


    private static Retrofit getRetrofitClient() {


        if (mRetrofitClient == null) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    LogTool.saveLog(TAG, message);
                }
            });

            HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(App.getContext()));
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .cookieJar(cookieJar) //自动保存session
                    .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                    .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)//允许访问所有的https网站
                    .addInterceptor(httpLoggingInterceptor);

            OkHttpClient client = builder.build();
            mRetrofitClient = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(Constant.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();


        }

        return mRetrofitClient;
    }

}
