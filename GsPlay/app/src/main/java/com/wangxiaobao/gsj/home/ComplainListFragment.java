package com.wangxiaobao.gsj.home;

import android.os.Bundle;
import android.view.View;

import com.wangxiaobao.gsj.base.App;
import com.wangxiaobao.gsj.common.AppConstants;
import com.wangxiaobao.gsj.common.CommonUtil;
import com.wangxiaobao.gsj.enity.ComplainListModel;
import com.wangxiaobao.gsj.enity.MapMerchantModel;
import com.wangxiaobao.gsj.enity.result.JsonListResult;
import com.wangxiaobao.gsj.http.RetrofitClient;
import com.wangxiaobao.gsj.module.introduce.MapFragment;
import com.wangxiaobao.gsj.util.ClickUtil;
import com.wangxiaobao.waiter.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by candy on 18-3-7.
 */

public class ComplainListFragment extends PullListFragment {


    @Override
    public int generateLayoutID() {
        return R.layout.fragment_complain;
    }

    public static ComplainListFragment newInstance(MapMerchantModel mapMerchantModel) {
        Bundle args = new Bundle();
        args.putSerializable(AppConstants.EXTRA_DATA, mapMerchantModel);
        ComplainListFragment fragment = new ComplainListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private MapMerchantModel mapMerchantModel;

    private ComplainListAdapter mComplainListAdapter;

    private Disposable disposable;

    private View goBackView;
    @Override
    public void initView(View view) {
        super.initView(view);
        Bundle b = getArguments();
        if (b != null) {
            mapMerchantModel = (MapMerchantModel) b.getSerializable(AppConstants.EXTRA_DATA);
        }

        mComplainListAdapter = new ComplainListAdapter(mContext);
        mPullToRefreshRecyclerView.setAdapter(mComplainListAdapter);
        mPullToRefreshRecyclerView.setPullRefreshEnabled(false);

        mMainActivity.setActionBarTitle("用户投诉");
        mMainActivity.setEnglishTitle("Complain");


        if (mapMerchantModel != null) {
            goBackView = mDataContainer.findViewById(R.id.goBack);
            goBackView.setVisibility(View.VISIBLE);
            goBackView.setOnClickListener(v -> {
                mMainActivity.displayMerchantInfoFragment(mapMerchantModel);
            });
        }
    }

    @Override
    public void initData() {
        super.initData();
        loadComplainList();
    }


    @OnClick(R.id.add_complain)
    public void addComplain() {
        if (ClickUtil.isFastClick()) {
            return;
        }

        if (mapMerchantModel != null) {
            mMainActivity.showFragment(ComplainDetailFragment.newInstance(mapMerchantModel));
        } else {
            mMainActivity.prepareToCommitComment(MapFragment.TYPE_TO_COMPLAIN);
        }
    }

    @Override
    protected void onLoadMore() {
        loadComplainList();
    }

    private void loadComplainList() {
        mPageNum++;
        showWaitDialog();

        if (mapMerchantModel != null && App.mApp.isLoadMerchantList) {
            loadMerchantComplainLIst();
        } else {
            disposable = RetrofitClient.getInstance().getComplainList(mPageNum)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(jsonListResult -> handleData(jsonListResult), throwable -> {
                        dismissWaitDialog();
                        if (mPageNum == 1) {
                            showEmptyView();
                            EmptyData emptyData = new EmptyData();
                            emptyData.drawable = R.drawable.ic_empty_complain;
                            emptyData.text = "商家还没有收到投诉~";
                            EventBus.getDefault().post(emptyData);
                        } else {
                            CommonUtil.showShortToast("请求数据异常");
                        }
                    });
        }

    }

    private void loadMerchantComplainLIst() {
        disposable = RetrofitClient.getInstance().getMerchantComplainList(mapMerchantModel.getMerchantId(), mPageNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jsonListResult -> handleData(jsonListResult), throwable -> {
                    dismissWaitDialog();
                    if (mPageNum == 1) {
                        showEmptyView();
                        EmptyData emptyData = new EmptyData();
                        emptyData.drawable = R.drawable.ic_empty_complain;
                        emptyData.text = "商家还没有收到投诉~";
                        EventBus.getDefault().post(emptyData);
                    } else {
                        CommonUtil.showShortToast("请求数据异常");
                    }
                });
    }

    private void handleData(JsonListResult<ComplainListModel> jsonListResult) {
        dismissWaitDialog();
        if (mPageNum == 1 && (jsonListResult.getData().size() == 0 || jsonListResult == null)) {
            showEmptyView();
            EmptyData emptyData = new EmptyData();
            emptyData.drawable = R.drawable.ic_empty_complain;
            emptyData.text = "商家还没有收到投诉~";
            EventBus.getDefault().post(emptyData);
        } else {
            mComplainListAdapter.addAll(jsonListResult.getData());
            mPullToRefreshRecyclerView.setLoadMoreComplete();
            mPullToRefreshRecyclerView.setVisibility(View.VISIBLE);
            if (20 == jsonListResult.getData().size()) {
                mPullToRefreshRecyclerView.setLoadingMoreEnabled(true);
            } else {
                mPullToRefreshRecyclerView.setLoadingMoreEnabled(false);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        dismissWaitDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
