package com.wangxiaobao.gsj.base;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.trello.rxlifecycle2.components.support.RxFragment;
import com.umeng.analytics.MobclickAgent;
import com.wangxiaobao.gsj.common.CommonUtil;
import com.wangxiaobao.gsj.common.DialogTools;
import com.wangxiaobao.gsj.common.LogTool;
import com.wangxiaobao.gsj.home.EmptyData;
import com.wangxiaobao.gsj.http.Api;
import com.wangxiaobao.gsj.http.Response;
import com.wangxiaobao.gsj.http.RetrofitClient;
import com.wangxiaobao.gsj.http.RetrofitObserver;
import com.wangxiaobao.gsj.http.RxSchedulers;
import com.wangxiaobao.waiter.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;

//import com.wangxiaobao.SmartTable.rxbus2.RxBus;

/**
 * Created by Administrator on 2016-04-05.
 */
public abstract class BaseFragment extends RxFragment {
    private static final String TAG = BaseFragment.class.getSimpleName();
    protected App application;

    @BindView(R.id.layout_error)
    View mNetworkErrorLayout;

    protected Api mApi;

    protected FrameLayout mDataContainer;

    protected MainActivity mMainActivity;

    @BindView(R.id.empty_image)
    ImageView emptyImage;
    @BindView(R.id.empty_text)
    TextView emptyText;
    @BindView(R.id.fragment_empty)
    public View mEmptyView;


    /**
     * 显示空界面
     */
    protected void showEmptyView() {
        mEmptyView.setVisibility(View.VISIBLE);
        mDataContainer.setVisibility(View.GONE);
        mNetworkErrorLayout.setVisibility(View.GONE);
    }

    /**
     * 显示网络错误界面
     */
    public void showErrorView() {
        mDataContainer.setVisibility(View.GONE);
        mNetworkErrorLayout.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
    }

    /**
     * 显示数据界面
     */
    protected void showDataView() {
        mDataContainer.setVisibility(View.VISIBLE);
        mNetworkErrorLayout.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
    }

    protected void hideEmptyView() {
        mEmptyView.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogTool.I(TAG, getClass().getName() + " onResume");
        MobclickAgent.onPageStart(getClass().getName());
        MobclickAgent.onResume(mContext);
    }

    @Override
    public void onPause() {
        super.onPause();
        LogTool.I(TAG, getClass().getName() + " onPause");
        MobclickAgent.onPageEnd(getClass().getName());
        MobclickAgent.onPause(mContext);

    }

    protected <T> Observable<T> startQuest(Observable<Response<T>> observable) {
        return observable.compose(RxSchedulers.compose()).compose(RetrofitClient.errorTransformer);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public abstract int generateLayoutID();

    protected Dialog waitingDilaog;
    protected Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogTool.saveLog(TAG, this.getClass().getName() + " onCreateView");
        mContext = getActivity();
        FrameLayout frameLayout = (FrameLayout) inflater.inflate(R.layout.fragment_base, null);

        mDataContainer = frameLayout.findViewById(R.id.data_container);
        application = (App) getActivity().getApplication();

        waitingDilaog = DialogTools.getLoadingDialog(getContext(), "");
        View contentView = inflater.inflate(generateLayoutID(), mDataContainer, false);
        sizeView(contentView);
        mDataContainer.addView(contentView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ButterKnife.bind(this, frameLayout);
        initView(frameLayout);
        initView(frameLayout, savedInstanceState);
        initData();

        return frameLayout;
    }

    public void sizeView(View view) {

    }

    protected void initView(FrameLayout frameLayout, Bundle savedInstanceState) {

    }

    @OnClick(R.id.layout_error)
    public void errorLayoutClick() {
        reload();
    }

    protected void reload() {

    }

    public void initView(View view) {

    }

    @Override
    public void onStart() {
        super.onStart();
        initBroadcastReceiver();

    }


    /**
     * 初始化数据，在initView之后；
     */
    public void initData() {
        mApi = RetrofitClient.getInstance().getService();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogTool.saveLog(TAG, this.getClass().getSimpleName() + " onDestroy");
        EventBus.getDefault().unregister(this);


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveEmptyData(EmptyData emptyData) {
        emptyImage.setImageResource(emptyData.drawable);
        emptyText.setText(emptyData.text);
    }

    /**
     * 显示网络错误界面
     */

    @Subscribe(threadMode = ThreadMode.MAIN)

    public void handleNetworkEvent(RetrofitObserver.NetworkEvent event) {
        switch (event.action) {
            case RetrofitObserver.NetworkEvent.ACTION_SHOW_DATA_VIEW:
                showDataView();
                break;
            case RetrofitObserver.NetworkEvent.ACTION_SHOW_EMPTY_VIEW:
                showEmptyView();
                break;
            case RetrofitObserver.NetworkEvent.ACTION_SHOW_ERROR_VIEW:
                showErrorView();
                break;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }


    public void initBroadcastReceiver() {
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(RxBus.ACTION_SEND_CODE);
//        mContext.registerReceiver(mBroadcastReceiver, filter);
    }


    @Override
    public void onStop() {
        super.onStop();
        LogTool.saveLog(TAG, this.getClass().getName() + " onStop");
//        mContext.unregisterReceiver(mBroadcastReceiver);
    }


    /**
     * 监听是否退出的广播
     */
    public BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
//            LogTool.saveLog(TAG, "onReceive action:" + intent.getAction());
//            if (intent.getAction().equals(RxBus.ACTION_SEND_CODE)) {
//                int code = intent.getIntExtra("code", 0);
//                if (EventCode.EVENT_FRAGMENT_SHOW_DATA_LAYOUT == code) {
//                    showDataView();
//                } else if (EventCode.EVENT_FRAGMENT_SHOW_ERROR_LAYOUT == code) {
//                    showErrorView();
//                }
//            }
        }
    };

    protected void showWaitDialog() {
        LogTool.saveLog(TAG, "显示对话框");
        if (waitingDilaog == null) {
            waitingDilaog = DialogTools.getLoadingDialog(getContext(), "");
        }
        waitingDilaog.show();
    }

    protected void showWaitDialog(String message) {
        LogTool.saveLog(TAG, "显示对话框:" + message);
        if (waitingDilaog == null) {
            waitingDilaog = DialogTools.getLoadingDialog(getContext(), message);
        }
        waitingDilaog.show();
    }


    protected void dismissWaitDialog() {
        LogTool.saveLog(TAG, "关闭对话框");
        if (waitingDilaog != null) {
            waitingDilaog.dismiss();
        }
    }

    protected Observable getResponse(Observable<Response> observable) {
        return observable.compose(RxSchedulers.compose()).compose(RetrofitClient.errorTransformer);
    }

    protected void showToast(String msg) {
        if (getActivity() != null) {
            CommonUtil.showShortToast(msg);
            Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogTool.saveLog(TAG, this.getClass().getName() + " onDetach");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogTool.saveLog(TAG, this.getClass().getName() + " onAttach");
        mMainActivity = (MainActivity) context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        RxBus.get().unRegister(this);
        LogTool.saveLog(TAG, this.getClass().getName() + " onDestroyView");

    }
}
