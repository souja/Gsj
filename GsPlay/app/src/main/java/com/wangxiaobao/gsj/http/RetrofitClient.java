package com.wangxiaobao.gsj.http;


import com.wangxiaobao.gsj.acount.AccountApi;
import com.wangxiaobao.gsj.common.LogTool;
import com.wangxiaobao.gsj.enity.AddComplainModel;
import com.wangxiaobao.gsj.enity.BigScreenHeadModel;
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
import com.wangxiaobao.gsj.log.JsonUtil;
import com.wangxiaobao.pushmanager.CommonUtils;

import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by candy on 17-10-26.
 */

public class RetrofitClient extends BaseRetrofit {

    private AccountApi accountApi;
    private static final String TAG = RetrofitClient.class.getSimpleName();


    //默认baseUrl的单例
    private static class SingletonHolder {
        private static RetrofitClient INSTANCE = new RetrofitClient(
        );
    }

    public static RetrofitClient getInstance() {
        return SingletonHolder.INSTANCE;
    }


    public AccountApi getAccountApi() {
        accountApi = create(AccountApi.class);
        return accountApi;
    }


    public RecommendApi getRecommendApi() {

        LogTool.saveLog(TAG, "create service " + "get recommend api");
        return create(RecommendApi.class);
    }

    public Observable<Response> login() {
        if (accountApi == null) {
            accountApi = getAccountApi();
        }

        Map<String, String> req = new HashMap<>(4);

        req.put("username", "111");
        req.put("password", "111");
        req.put("type", "1");

        return accountApi.login(getScreenHeader(), req);
    }

    private String getScreenHeader() {
        BigScreenHeadModel model = new BigScreenHeadModel(CommonUtils.getSn());
//        BigScreenHeadModel model = new BigScreenHeadModel("PKLPAX68KI");
        String s = URLEncoder.encode(JsonUtil.objectToJson(model));
        LogTool.saveLog("发送的header===>" + s);
        return s;
//        return "%7b%22bigScreenId%22%3a%2251644243090257f0b06%22%7d";
    }


    public Observable<JsonListResult<TradeAreaModel>> getTradingArea() {
        if (accountApi == null) {
            accountApi = getAccountApi();
        }

        return accountApi.getTradingArea(getScreenHeader());
    }

    /**
     * @param businessCircleId 根据商圈id获取首页投诉信息
     * @return
     */
    public Observable<Response<HomeComplainInfoModel>> getHomeComplainInfo(int businessCircleId) {
        if (accountApi == null) {
            accountApi = getAccountApi();
        }

        return accountApi.getHomeComplainInfo(getScreenHeader(), businessCircleId);

    }

    /**
     * @param businessCircleId 根据商圈id获取首页评价信息
     * @return
     */
    public Observable<Response<HomeCommentModel>> getHomeCommentInfo(int businessCircleId) {
        if (accountApi == null) {
            accountApi = getAccountApi();
        }

        return accountApi.getHomeCommentInfo(getScreenHeader(), businessCircleId);

    }

    /**
     * @param businessCircleId 根据商圈id获取首页投诉列表
     * @param pageNo
     * @return
     */
    public Observable<JsonListResult<HomeComplainListModel>> getHomeComplainList(int businessCircleId, int pageNo) {
        if (accountApi == null) {
            accountApi = getAccountApi();
        }

        return accountApi.getHomeComplainList(getScreenHeader(), businessCircleId, pageNo, 50);

    }

    /**
     * @param businessCircleId 根据商圈id获取商家列表
     * @return
     */
    public Observable<JsonListResult<MapMerchantModel>> getMerchantByArea(int businessCircleId) {
        if (accountApi == null) {
            accountApi = getAccountApi();
        }

        return accountApi.getMerchantByArea(getScreenHeader(), businessCircleId);

    }

    /**
     * @param businessCircleId 根据商圈id获取评价列表
     * @param pageNo
     * @return
     */
    public Observable<Response<WrapModel<List<NewCommentModel>>>> getCommentList(int businessCircleId, int pageNo,
                                                                                 int pageSize) {
        if (accountApi == null) {
            accountApi = getAccountApi();
        }

        return accountApi.getCommentList(getScreenHeader(), businessCircleId, pageNo, pageSize);

    }

    /**
     * @param merchantId 根据merchantId获取评价列表
     * @param pageNo
     * @return
     */
    public Observable<Response<WrapModel<List<NewCommentModel>>>> getMerchantCommentList(String merchantId, int pageNo,
                                                                                         int pageSize) {
        if (accountApi == null) {
            accountApi = getAccountApi();
        }

        return accountApi.getMerchantCommentList(getScreenHeader(), merchantId, pageNo, pageSize);

    }

    /**
     * @param pageNo
     * @return 评价列表根据大屏sn获取评价信息
     */
    public Observable<Response<WrapModel<List<NewCommentModel>>>> getNewCommentList(int pageNo,
                                                                                    int pageSize) {
        if (accountApi == null) {
            accountApi = getAccountApi();
        }

        return accountApi.getNewCommentList(getScreenHeader(), CommonUtils.getSn(), pageNo, pageSize);

    }

    /**
     * 添加评论
     */
    public Observable<Response> addComment(CommitCommentModel model) {
        if (accountApi == null) {
            accountApi = getAccountApi();
        }


        return accountApi.addComment(getScreenHeader(), model);

    }

    /**
     * 添加评论
     */
    public Observable<Response<VoiceRecognizeModel>> resolveAudio(String filePath) {
        if (accountApi == null) {
            accountApi = getAccountApi();
        }

        File filePath1 = new File(filePath);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), filePath1);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", filePath1.getName(), requestFile);

        return accountApi.resolveAudio(getScreenHeader(), body);

    }

    /**
     * 添加投诉
     */
    public Observable<Response> addComplain(AddComplainModel model) {
        if (accountApi == null) {
            accountApi = getAccountApi();
        }

        return accountApi.addComplain(getScreenHeader(), model);

    }

    /**
     * @param pageNo
     * @return
     */
    public Observable<JsonListResult<ComplainListModel>> getComplainList(int pageNo) {
        if (accountApi == null) {
            accountApi = getAccountApi();
        }
        return accountApi.getComplainList(getScreenHeader(), pageNo, 20);
    }
    /**
     * 获取投诉类型列表
     * @return
     */
    public Observable<JsonListResult<ComplainTypeModel>> getComplainTypeList(String merchantId) {
        if (accountApi == null) {
            accountApi = getAccountApi();
        }
        return accountApi.getComplainTypeList(getScreenHeader(), merchantId);
    }

    /**
     * @param merchantId 获取商家投诉列表
     * @param pageNo
     * @return
     */
    public Observable<JsonListResult<ComplainListModel>> getMerchantComplainList(String merchantId, int pageNo) {
        if (accountApi == null) {
            accountApi = getAccountApi();
        }
        return accountApi.getMerchantComplainList(getScreenHeader(), merchantId, pageNo, 20);
    }

    /**
     * @param merchantId 根据merchantId获取商家信息
     * @return
     */
    public Observable<Response<MerchantInfoModel>> getMerchantInfo(String merchantId) {
        if (accountApi == null) {
            accountApi = getAccountApi();
        }
        return accountApi.getMerchantInfo(getScreenHeader(), merchantId);
    }

    /**
     * @param merchantId 根据merchantId获取商家信用信息
     * @return
     */
    public Observable<Response<MerchantGSInfo>> getMerchantGSInfo(String merchantId) {
        if (accountApi == null) {
            accountApi = getAccountApi();
        }
        return accountApi.getMerchantGSInfo(getScreenHeader(), merchantId);
    }

}
