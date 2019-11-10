package com.wangxiaobao.gsj.http;


import android.text.TextUtils;

import com.wangxiaobao.gsj.base.DialogEvent;
import com.wangxiaobao.gsj.base.App;
import com.wangxiaobao.gsj.common.CommonUtil;
import com.wangxiaobao.gsj.common.LogTool;


import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class RetrofitObserver<T> implements Observer<T> {

    private static final String TAG = "RetrofitObserver";

    RetrofitObserverOption retrofitObserverOption;


    public RetrofitObserver() {
        retrofitObserverOption = new RetrofitObserverOption();
    }

    public RetrofitObserver(boolean needShowHttpErrorLayout) {
        this();
        retrofitObserverOption.setNeedShowHttpErrorLayout(needShowHttpErrorLayout);
    }

    public RetrofitObserver(DialogEvent dialogEvent) {
        this();
        retrofitObserverOption.setDialogEvent(dialogEvent);
    }


    public RetrofitObserver(String successMessage) {
        this();
        retrofitObserverOption.setSuccessMessage(successMessage);
    }

    public RetrofitObserver(RetrofitObserverOption retrofitObserverOption) {
        this.retrofitObserverOption = retrofitObserverOption;
    }

    public RetrofitObserver(int viewMode) {
        retrofitObserverOption.setViewMode(viewMode);
    }


    @Override
    public void onSubscribe(Disposable disposable) {
        LogTool.saveLog(TAG, "开始执行");
        if (retrofitObserverOption.isNeedShowDialog()) {
            LogTool.saveLog(TAG, "显示对话框");
            showWaitDialog();
        }

    }


    public static class NetworkEvent {
        public static int TYPE_ACTIVITY = 1;
        public static int TYPE_FRAGMENT = 2;

        public static final int ACTION_SHOW_ERROR_VIEW = 1;
        public static final int ACTION_SHOW_EMPTY_VIEW = 2;
        public static final int ACTION_SHOW_DATA_VIEW = 3;

        public int type;
        public int action;
    }


    @Override
    public void onNext(T t) {
        showSuccessTip();
        LogTool.saveLog(TAG, "执行成功" + t.toString());

        try {
            if (t == null) {
                showEmptyView();
            } else {
                showDataView();
                onHandleSuccess(t);
            }
        } catch (Exception e) {
            onHandleSuccess(null);
            showEmptyView();
        }
        hideWaitDialog();
    }

    private void showSuccessTip() {
        if (!TextUtils.isEmpty(retrofitObserverOption.getSuccessMessage())) {
            CommonUtil.showShortToast(retrofitObserverOption.getSuccessMessage());
        }
    }


    /**
     * 显示网络错误界面
     */
    public void showErrorView() {
        switch (retrofitObserverOption.getViewMode()) {
            case RetrofitObserverOption.ACTIVITY_VIEW_MODE:
                showErrorView(NetworkEvent.TYPE_ACTIVITY);
                break;
            case RetrofitObserverOption.FRAGMENT_VIEW_MODE:
                showErrorView(NetworkEvent.TYPE_FRAGMENT);
                break;
        }
    }


    public void showErrorView(int type) {
        NetworkEvent activityNetworkEvent = new NetworkEvent();
        activityNetworkEvent.type = type;
        activityNetworkEvent.action = NetworkEvent.ACTION_SHOW_ERROR_VIEW;
        EventBus.getDefault().post(activityNetworkEvent);
    }

    public void showEmptyView(int type) {
        NetworkEvent activityNetworkEvent = new NetworkEvent();
        activityNetworkEvent.type = type;
        activityNetworkEvent.action = NetworkEvent.ACTION_SHOW_EMPTY_VIEW;
        EventBus.getDefault().post(activityNetworkEvent);
    }

    public void showEmptyView() {
        switch (retrofitObserverOption.getViewMode()) {
            case RetrofitObserverOption.ACTIVITY_VIEW_MODE:
                showEmptyView(NetworkEvent.TYPE_ACTIVITY);
                break;
            case RetrofitObserverOption.FRAGMENT_VIEW_MODE:
                showEmptyView(NetworkEvent.TYPE_FRAGMENT);
                break;
        }
    }


    public void showDataView(int type) {
        NetworkEvent activityNetworkEvent = new NetworkEvent();
        activityNetworkEvent.type = type;
        activityNetworkEvent.action = NetworkEvent.ACTION_SHOW_DATA_VIEW;
        EventBus.getDefault().post(activityNetworkEvent);
    }

    /**
     * 处理数据加载成功后的事件
     */
    private void showDataView() {
        switch (retrofitObserverOption.getViewMode()) {
            case RetrofitObserverOption.ACTIVITY_VIEW_MODE:
                showDataView(NetworkEvent.TYPE_ACTIVITY);
                break;
            case RetrofitObserverOption.FRAGMENT_VIEW_MODE:
                showDataView(NetworkEvent.TYPE_FRAGMENT);
                break;
        }
    }


    @Override
    public void onError(Throwable e) {
        LogTool.saveLog(TAG, "执行异常", e);


        if (e instanceof ResponseException) {

            ResponseException responseException = (ResponseException) e;
            if (TextUtils.isEmpty(responseException.errorMessage)) {
                responseException.errorMessage = "未知错误";
            }
            CommonUtil.showShortToast(App.getContext(), responseException.errorMessage);
            if (responseException.getCode() == 7) {
                //处理用户过期
                App.jumpToLogin(App.getContext());
                return;
            }
//            showErrorView();
        } else {
            showUnknowTip(e);
        }

        onHandleError();
        hideWaitDialog();
        LogTool.saveLog(TAG, "执行异常", e);

    }


    /**
     * 显示对话框
     */
    private void showWaitDialog() {
        DialogEvent dialogEvent = new DialogEvent();
        dialogEvent.eventCode = DialogEvent.EVENT_SHOW_DIALOG;
        EventBus.getDefault().post(dialogEvent);
    }


    /**
     * 隐藏对话框
     */
    private void hideWaitDialog() {
        DialogEvent dialogEvent = new DialogEvent();
        dialogEvent.eventCode = DialogEvent.EVENT_HIDE_DIALOG;
        EventBus.getDefault().post(dialogEvent);
    }

    private void showUnknowTip(Throwable e) {
        ResponseException responseException = new ResponseException(e, ExceptionHandler.ERROR.UNKNOWN);
        responseException.errorMessage = "未知错误";
        CommonUtil.showShortToast(responseException.errorMessage);
    }


    @Override
    public void onComplete() {
        LogTool.saveLog(TAG, "执行完成");

    }

    protected abstract void onHandleSuccess(T t);

    protected void onHandleError() {
    }
}
