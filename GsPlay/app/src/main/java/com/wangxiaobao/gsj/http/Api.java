package com.wangxiaobao.gsj.http;


import com.wangxiaobao.gsj.enity.LoginData;
import com.wangxiaobao.gsj.enity.VersionEntity;
import com.wangxiaobao.gsj.home.AdviseListResponse;
import com.wangxiaobao.gsj.home.CommentListResponse;
import com.wangxiaobao.gsj.home.ComplainListResponse;
import com.wangxiaobao.gsj.home.StoreInfo;


import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by candy on 17-10-24.
 */

public interface Api {
    /**
     * 检查新版本
     */
    @FormUrlEncoded
    @POST(Constant.ACTION_QUERY_UPDATE_INFO)
    Observable<Response<VersionEntity>> checkNewVersion(@Field("appPackage") String appPackage);


    /**
     * 登录
     *
     * @param maps
     */
    @FormUrlEncoded
    @POST(Constant.WEB_HOST + "tc-web/login/app/noErp/doLogin.htm")
    Observable<Response<LoginData>> login(
            @FieldMap Map<String, String> maps
    );


    /**
     * 添加投诉
     *
     */

    @POST("aic/wap/complain/add")
    Observable<Response<Object>> addComplain(
            @Body RequestBody requestBody
    );


    /**
     * 添加投诉
     */
    @POST("aic/wap/comment/add")
    Observable<Response<Object>> addCommend(
            @Body RequestBody requestBody
    );


    /**
     * 投诉列表
     */
    @GET("/complaint/domesticComplaintList")
    Observable<Response<ComplainListResponse>> getComplainList(
            @QueryMap Map<String, String> map
    );


    /**
     * 意见列表
     */
    @GET("aic/wap/comment/findAdvise")
    Observable<Response<AdviseListResponse>> getAdvisementList(
            @QueryMap Map<String, String> map
    );

    /**
     * @return
     */
    @GET("aic/wap/comment/find")
    Observable<Response<CommentListResponse>> getCommentList(
            @Query("pageNum") int pageNum, @Query("pageSize") int pageSize, @Query("merchantId") String merchantId
    );


    @POST(Constant.ACTION_GET_DISH_UTILS)
    Observable<Response<List<Object>>> loadUnitList(
    );

    @GET(Constant.ACTION_FIND_TABLE_LIST)
    Observable<Response<Object>> loadTableList(@QueryMap Map<String, String> map);

    @POST("aic/wap/merchant/info")
    Observable<Response<StoreInfo>> getStoreInfo(@Body RequestBody requestBody);


    /**
     * 上传二维码
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST()
    Observable<Response> uploadQrCode(@Url String url, @FieldMap Map<String, String> map);


    /**
     * 获取店铺信息
     */
    @GET(Constant.ACTION_GET_MERCHANT)
//    @GET("http://192.168.100.246:80/web/merchant/merchantInfo.htm")
    Observable<Response<Object>> getMerchantInfo();


    /**
     * 代运营登录
     */
    @FormUrlEncoded
    @POST(Constant.ACTION_OPERATION_LOGIN)
    Observable<Response<LoginData>> operationLogin(@FieldMap Map<String, String> map);
}
