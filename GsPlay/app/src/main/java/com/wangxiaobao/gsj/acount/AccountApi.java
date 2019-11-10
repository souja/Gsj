package com.wangxiaobao.gsj.acount;

import com.wangxiaobao.gsj.enity.AddComplainModel;
import com.wangxiaobao.gsj.enity.CommitCommentModel;
import com.wangxiaobao.gsj.enity.ComplainTypeModel;
import com.wangxiaobao.gsj.enity.HomeCommentModel;
import com.wangxiaobao.gsj.enity.ComplainListModel;
import com.wangxiaobao.gsj.enity.HomeComplainInfoModel;
import com.wangxiaobao.gsj.enity.HomeComplainListModel;
import com.wangxiaobao.gsj.enity.MapMerchantModel;
import com.wangxiaobao.gsj.enity.MerchantGSInfo;
import com.wangxiaobao.gsj.enity.MerchantInfoModel;
import com.wangxiaobao.gsj.enity.NewCommentModel;
import com.wangxiaobao.gsj.enity.TradeAreaModel;
import com.wangxiaobao.gsj.enity.VoiceRecognizeModel;
import com.wangxiaobao.gsj.enity.WrapModel;
import com.wangxiaobao.gsj.enity.result.JsonListResult;
import com.wangxiaobao.gsj.http.Constant;
import com.wangxiaobao.gsj.http.Response;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by candy on 17-10-31.
 */

public interface AccountApi {


    @GET(Constant.HOST_WEB_BASE_URL + Constant.ACTION_GET_VERIFY_CODE)
    Observable<Response> sendVerifyCode(@QueryMap Map<String, String> map);

    @GET(Constant.ACTION_GET_BIND_PHONE_NUMBER)
    Observable<Response<Phone>> getBindPhoneNumber(@QueryMap Map<String, String> map);


    /**
     * 密码重置
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST(Constant.HOST_WEB_BASE_URL + Constant.ACTION_RESET_PASSWORD)
    Observable<Response> resetPassword(@FieldMap Map<String, String> map);

    @GET(Constant.HOST_WEB_BASE_URL + Constant.ACTION_CHECK_VERIFY_CODE)
    Observable<Response> checkVerifyCode(@QueryMap Map<String, String> map);


    @GET(Constant.ACTION_BIND_PHONE)
    Observable<Response> updatePhoneNumber(@QueryMap Map<String, String> map);


    /**
     * 密码更新
     *
     * @param maps
     * @return
     */
    @FormUrlEncoded
    @POST(Constant.ACTION_UPDATE_PASSWORD)
    Observable<Response> updatePassword(
            @FieldMap Map<String, String> maps
    );


    @FormUrlEncoded
    @POST("theSecondPhase-Aic/aic/manager-login")
    Observable<Response> login(@Header("bigScreenInfo") String bigScreenID, @FieldMap Map<String, String> req);

    @GET("theSecondPhase-Aic/tradingarea/findTradingarea")
    Observable<JsonListResult<TradeAreaModel>> getTradingArea(@Header("bigScreenInfo") String bigScreenId);


    @GET("theSecondPhase-Aic/complaint/domesticHomepage")
    Observable<Response<HomeComplainInfoModel>> getHomeComplainInfo(@Header("bigScreenInfo") String bigScreenId,
                                                                    @Query("businessCircleId") int businessCircleID);

    @GET("theSecondPhase-Aic/aic/comment/commentCount")
    Observable<Response<HomeCommentModel>> getHomeCommentInfo(@Header("bigScreenInfo") String bigScreenId,
                                                              @Query("tradingAreaId") int businessCircleId);

    @GET("theSecondPhase-Aic/complaint/domesticComplaintList")
    Observable<JsonListResult<HomeComplainListModel>> getHomeComplainList(@Header("bigScreenInfo") String bigScreenId,
                                                                          @Query("businessCircleId") int businessCircleId,
                                                                          @Query("pageNo") int pageNo,
                                                                          @Query("pageSize") int pageSize);

    @GET("theSecondPhase-Aic/aic/merchant/findAllByArea")
    Observable<JsonListResult<MapMerchantModel>> getMerchantByArea(@Header("bigScreenInfo") String bigScreenId,
                                                                   @Query("tradingareaId") int businessCircleId);

    @GET("theSecondPhase-Aic/aic/comment/findPageByBigScreenSn")
    Observable<Response<WrapModel<List<NewCommentModel>>>> getNewCommentList(@Header("bigScreenInfo") String bigScreenId,
                                                                             @Query("bigScreenSn") String bigScreenSn,
                                                                             @Query("pageNo") int pageNo,
                                                                             @Query("pageSize") int pageSize);

    @GET("theSecondPhase-Aic/aic/comment/findPageByTradingAreaId")
    Observable<Response<WrapModel<List<NewCommentModel>>>> getCommentList(@Header("bigScreenInfo") String bigScreenId,
                                                                          @Query("tradingAreaId") int businessCircleId,
                                                                          @Query("pageNo") int pageNo,
                                                                          @Query("pageSize") int pageSize);

    @GET("theSecondPhase-Aic/aic/comment/findPageBymerchantId")
    Observable<Response<WrapModel<List<NewCommentModel>>>> getMerchantCommentList(@Header("bigScreenInfo") String bigScreenId,
                                                                                  @Query("merchantId") String merchantId,
                                                                                  @Query("pageNo") int pageNo,
                                                                                  @Query("pageSize") int pageSize);

    @POST("theSecondPhase-Aic/aic/comment/add")
    Observable<Response> addComment(@Header("bigScreenInfo") String bigScreenId,
                                    @Body CommitCommentModel model);

    @Multipart
    @POST("theSecondPhase-Aic/mp3Upload")
    Observable<Response<VoiceRecognizeModel>> resolveAudio(@Header("bigScreenInfo") String bigScreenId,
                                                           @Part MultipartBody.Part file);


    /**
     * 获取投诉类型列表
     * */
    @GET("/theSecondPhase-Aic/complaintType/findList")
    Observable<JsonListResult<ComplainTypeModel>> getComplainTypeList(@Header("bigScreenInfo") String bigScreenId,
                                                                      @Query("merchantId") String merchantId);
//,
//                                                                      @Query("pageNo") int pageNo,
//                                                                      @Query("pageSize") int pageSize


    @POST("theSecondPhase-Aic/complaint/add")
    Observable<Response> addComplain(@Header("bigScreenInfo") String bigScreenId,
                                     @Body AddComplainModel req);

    @GET("theSecondPhase-Aic/complaint/complaintDetailList")
    Observable<JsonListResult<ComplainListModel>> getComplainList(@Header("bigScreenInfo") String bigScreenId,
                                                                  @Query("pageNo") int pageNo,
                                                                  @Query("pageSize") int pageSize);

    @GET("theSecondPhase-Aic/complaint/complaintDetailListBymerchantId")
    Observable<JsonListResult<ComplainListModel>> getMerchantComplainList(@Header("bigScreenInfo") String bigScreenId,
                                                                          @Query("merchantId") String merchantId,
                                                                          @Query("pageNo") int pageNo,
                                                                          @Query("pageSize") int pageSize);

    @GET("theSecondPhase-Aic/aic/merchant/findMerchantId")
    Observable<Response<MerchantInfoModel>> getMerchantInfo(@Header("bigScreenInfo") String bigScreenId,
                                                            @Query("merchantId") String businessCircleId);
    @GET("theSecondPhase-Aic/gongshang/getGongshangInfo")
    Observable<Response<MerchantGSInfo>> getMerchantGSInfo(@Header("bigScreenInfo") String bigScreenId,
                                                           @Query("merchantId") String businessCircleId);
}
