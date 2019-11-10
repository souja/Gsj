package com.wangxiaobao.gsj.home;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.wangxiaobao.gsj.base.App;
import com.wangxiaobao.gsj.base.CrashMessage;
import com.wangxiaobao.gsj.common.CommonUtil;
import com.wangxiaobao.gsj.http.BaseRetrofit;
import com.wangxiaobao.gsj.http.Constant;
import com.wangxiaobao.gsj.http.RetrofitClient;
import com.wangxiaobao.gsj.http.RetrofitObserver;
import com.wangxiaobao.waiter.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by candy on 18-3-7.
 */

public class ApiManager {

    private static final String TAG = ApiManager.class.getSimpleName();

    private static class SingletonHolder {
        private static ApiManager INSTANCE = new ApiManager(
        );
    }

    public static ApiManager getInstance() {
        return SingletonHolder.INSTANCE;
    }


    /**
     * 添加投诉
     *
     * @param complain
     */
    public void addComplain(Complain complain) {


        RequestBody body = RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(complain));


        BaseRetrofit.startRequest(RetrofitClient.getInstance().getService().addComplain(body))
                .subscribe(new RetrofitObserver<Object>() {
                    @Override
                    protected void onHandleSuccess(Object o) {
                        CommonUtil.showShortToast("投诉成功!");

                        Constant.Event event = new Constant.Event();
                        event.code = 3;
                        EventBus.getDefault().post(event);
                    }
                });


    }


    /**
     * 添加评论
     *
     * @param comment
     */
    public void addCommend(Comment comment) {


        RequestBody body = RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(comment));


        BaseRetrofit.startRequest(RetrofitClient.getInstance().getService().addCommend(body))
                .subscribe(new RetrofitObserver<Object>() {
                    @Override
                    protected void onHandleSuccess(Object o) {
                        CommonUtil.showShortToast("添加评论成功!");


                        Constant.Event event = new Constant.Event();
                        event.code = 2;
                        EventBus.getDefault().post(event);
                    }
                });


    }

    /**
     * 添加意见
     */
    public void addAdvisement(Comment comment) {


        RequestBody body = RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(comment));


        BaseRetrofit.startRequest(RetrofitClient.getInstance().getService().addCommend(body))
                .subscribe(new RetrofitObserver<Object>() {
                    @Override
                    protected void onHandleSuccess(Object o) {
                        CommonUtil.showShortToast("添加意见成功!!");

                        Constant.Event event = new Constant.Event();
                        event.code = 1;
                        EventBus.getDefault().post(event);
                    }
                });


    }

    /**
     * 获取投诉列表
     *
     * @param
     */
    public void getComplainList(int pageNum, int pageSize, Context context) {


        Map<String, String> map = new HashMap<>();


        map.put("pageNum", pageNum + "");

        map.put("pageSize", pageSize + "");

        map.put("merchantId", CommonUtil.getMerchantID(context));


        BaseRetrofit.startRequest(RetrofitClient.getInstance().getService().getComplainList(map))
                .subscribe(new RetrofitObserver<ComplainListResponse>() {


                    @Override
                    protected void onHandleSuccess(ComplainListResponse complainListResponse) {
                        if (complainListResponse == null || complainListResponse.getCount() == 0) {
                            showEmptyView();
                            EmptyData emptyData = new EmptyData();
                            emptyData.drawable = R.drawable.ic_empty_complain;
                            emptyData.text = "商家还没有收到投诉~";
                            EventBus.getDefault().post(emptyData);
                        } else {
                            EventBus.getDefault().post(complainListResponse);
                        }
                    }
                });


    }


    /**
     * 上报崩溃信息
     */
    public void reportCrashMessage() {

        Log.i(TAG, "上报崩溃信息");

        Map<String, String> map = new HashMap<>();
        map.put("app", "cateringMonitorStatus");
        map.put("topic", "cateringMonitorStatus");
        CrashMessage crashMessage = new CrashMessage();
        crashMessage.setMonitorStatus("0");
        crashMessage.setMoniterTime(new Date().getTime() + "");
        crashMessage.setTargetType("merchant");
        crashMessage.setTarget(CommonUtil.getMerchantName(App.getContext()));
        crashMessage.setMonitorType("GSJ_Server");
        Log.i(TAG, new Gson().toJson(crashMessage));
        map.put("messageStr", new Gson().toJson(crashMessage));


        OkHttpUtils.post().url("http://platform.wangxiaobao.com/send/").params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.i(TAG, "上报错误");
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i(TAG, "上报成功:" + response);
            }
        });


    }


    /**
     * 获取意见列表
     *
     * @param
     */
    public void getAdvisementList(int pageNum, int pageSize, Context context) {


        Map<String, String> map = new HashMap<>();


        map.put("pageNum", pageNum + "");

        map.put("pageSize", pageSize + "");

        map.put("merchantId", CommonUtil.getMerchantID(context));


        BaseRetrofit.startRequest(RetrofitClient.getInstance().getService().getAdvisementList(map))
                .subscribe(new RetrofitObserver<AdviseListResponse>() {


                    @Override
                    protected void onHandleSuccess(AdviseListResponse adviseListResponse) {
                        if (adviseListResponse == null || adviseListResponse.getCount() == 0) {
                            showEmptyView();
                            EmptyData emptyData = new EmptyData();
                            emptyData.drawable = R.drawable.ic_empty_advise;
                            emptyData.text = "商家还没有收到意见~";
                            EventBus.getDefault().post(emptyData);

                        } else {
                            EventBus.getDefault().post(adviseListResponse);
                        }

                    }
                });


    }

    /**
     * 获取评价列表
     *
     * @param
     */
    public void getCommentList(int pageNum, int pageSize, Context context) {
//        Map<String, String> map = new HashMap<>();
//        map.put("mPageSize", mPageSize + "");
//        map.put("pageSize", pageSize + "");
//        map.put("merchantId", CommonUtil.getMerchantID(context));

        BaseRetrofit.startRequest(RetrofitClient.getInstance().getService()
                .getCommentList(pageNum, pageSize, CommonUtil.getMerchantID(context)))
                .subscribe(new RetrofitObserver<CommentListResponse>() {
                    @Override
                    protected void onHandleSuccess(CommentListResponse commentListResponse) {
                        if (commentListResponse == null || commentListResponse.getCount() == 0) {
                            showEmptyView();
                            EmptyData emptyData = new EmptyData();
                            emptyData.drawable = R.drawable.ic_empty_comment;
                            emptyData.text = "商家还没有收到评价~";
                            EventBus.getDefault().post(emptyData);
                        } else {
                            EventBus.getDefault().post(commentListResponse);
                        }
                    }
                });
    }
}
