package com.wangxiaobao.gsj.base;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wangxiaobao.gsj.common.CommonUtil;
import com.wangxiaobao.gsj.common.DialogTools;
import com.wangxiaobao.gsj.common.LogTool;
import com.wangxiaobao.gsj.common.ScreenUtil;
import com.wangxiaobao.gsj.http.Api;
import com.wangxiaobao.gsj.http.Response;
import com.wangxiaobao.gsj.http.ResponseException;
import com.wangxiaobao.gsj.http.RetrofitClient;
import com.wangxiaobao.gsj.http.RxSchedulers;

import com.wangxiaobao.waiter.R;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.BindView;
import io.reactivex.Observable;

/**
 * Created by Administrator on 2016-04-05.
 */
public abstract class BaseActivity extends RxAppCompatActivity {
    public static final String INIENT_FINISH = "com.wangxiaobao.waiter.broadcast.finish";
    private static final String TAG = BaseActivity.class.getSimpleName();
    protected App application;

    protected abstract int generateLayoutId();

    private ProgressDialogManager mProgressDialogManager;
    protected Context mContext;
    protected boolean isCustom = false;
    protected RetrofitClient mRetrofitClient;
    protected Api mApi;


    @BindView(R.id.layout_error)
    protected View mNetworkErrorLayout;
    @BindView(R.id.data_container)
    protected FrameLayout mDataContainer;
    private boolean isPause;

    protected Dialog waitingDilaog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogTool.saveLog(TAG, getClass().getName() + " onCreate");
        mContext = this;
        mRetrofitClient = RetrofitClient.getInstance();
        if (!isCustom) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.new_primary_black_color));
            }
        }

        application = (App) getApplication();
        mProgressDialogManager = new ProgressDialogManager(this);
        mProgressDialogManager.resume();
        initFinishReceiver();


        View rootView = getLayoutInflater().inflate(getRootLayout(), null);
        mDataContainer = rootView.findViewById(R.id.data_container);
        View contentView = getLayoutInflater().inflate(generateLayoutId(), mDataContainer, false);
        sizeView(contentView);
        mDataContainer.addView(contentView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(rootView);
        ButterKnife.bind(this);
        mNetworkErrorLayout.setOnClickListener(v -> reload());

        initView();
        initData();

        waitingDilaog = DialogTools.getLoadingDialog(this, "");
        EventBus.getDefault().register(this);
    }

    public void sizeView(View view){

    }

    protected void jumpToActivity(Class cla) {
        startActivity(new Intent(mContext, cla));
    }

    protected void jumpToActivity(Class cla, int requestCode) {
        startActivityForResult(new Intent(mContext, cla), requestCode);
    }

    protected int getRootLayout() {
        return R.layout.activity_base;
    }

    public void reload() {

    }


    protected <T> Observable<T> startRequest(Observable<Response<T>> observable) {
        return observable.compose(RxSchedulers.compose()).compose(RetrofitClient.errorTransformer);
    }


    /**
     * 显示网络错误界面
     */


    public void showErrorLayout() {
        mDataContainer.setVisibility(View.GONE);
        mNetworkErrorLayout.setVisibility(View.VISIBLE);
    }

    public void showSuccessLayout() {
        mDataContainer.setVisibility(View.VISIBLE);
        mNetworkErrorLayout.setVisibility(View.GONE);
    }


    protected void setOnClickListener(View view) {
//        view.setOnClickListener(this);
    }

    /**
     * 获取网络数据，在initView之后；
     */
    protected void initData() {
        mApi = RetrofitClient.getInstance().getService();
    }

    protected void initView() {

    }


    protected void hideTitleActionBtn() {
        findViewById(R.id.right_action).setVisibility(View.GONE);
    }

    protected void setTitleText(String titleContent) {
        TextView title = (TextView) findViewById(R.id.actionbar_title);
        title.setText(titleContent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogTool.saveLog(TAG, getClass().getName() + " onResume");
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(getClass().getName());

        isPause = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogTool.saveLog(TAG, getClass().getName() + " onPause");

        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(getClass().getName());
        isPause = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogTool.saveLog(TAG, getClass().getName() + " onStop");
//        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        IntentFilter intentFilter = new IntentFilter(RxBus.ACTION_SEND_CODE);
//        registerReceiver(mBroadcastReceiver, intentFilter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProgressDialogManager.dismissDialog();

        mProgressDialogManager.stop();
        unregisterReceiver(mFinishReceiver);

        LogTool.saveLog(TAG, getClass().getName() + " onDestroy");
        EventBus.getDefault().unregister(this);

    }


    protected Observable getResponse(Observable<Response> observable) {
        return observable.compose(RxSchedulers.compose()).compose(RetrofitClient.errorTransformer);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handlBack(Integer code) {
        if (code == 111) {
            finish();
        }

    }


    protected void showToast(String message) {
        CommonUtil.showShortToast(mContext, message);
    }

    protected void showToast(ResponseException responseException) {
        CommonUtil.showShortToast(mContext, responseException.getErrorMessage());
    }

    /**
     * 初始化退出广播
     */
    public void initFinishReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(INIENT_FINISH);
        registerReceiver(mFinishReceiver, filter);
    }

    /**
     * 监听是否退出的广播
     */
    public BroadcastReceiver mFinishReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
        }
    };


    public void showShowEmptyView() {
    }

    /**
     * 监听是否退出的广播
     */
    public BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            LogTool.saveLog(TAG, "onReceive action:" + intent.getAction());
//            if (intent.getAction().equals(RxBus.ACTION_SEND_CODE)) {
//            }
        }
    };

    protected void showWaitDialog() {
        LogTool.saveLog(TAG, "显示对话框");
        if (waitingDilaog == null) {
            waitingDilaog = DialogTools.getLoadingDialog(this, "");
        }
        waitingDilaog.show();

    }

    protected void showWaitDialog(String message) {
        LogTool.saveLog(TAG, "显示对话框:" + message);
        if (waitingDilaog == null) {
            waitingDilaog = DialogTools.getLoadingDialog(this, message);
        }
        waitingDilaog.show();
    }

    protected void dismissWaitDialog() {
        LogTool.saveLog(TAG, "关闭对话框");
        if (waitingDilaog != null) {
            waitingDilaog.dismiss();
        }
    }


}
