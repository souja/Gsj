package com.wangxiaobao.gsj.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.androidkun.PullToRefreshRecyclerView;
import com.androidkun.callback.PullToRefreshListener;
import com.wangxiaobao.gsj.base.BaseFragment;
import com.wangxiaobao.waiter.R;

import butterknife.BindView;

/**
 * Created by candy on 18-3-7.
 */

public class PullListFragment extends BaseFragment {

    @BindView(R.id.refresh_recyclerview)
    PullToRefreshRecyclerView mPullToRefreshRecyclerView;

    protected int mPageNum;
    protected int mPageSize = 20;
    protected int totalNumber = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void showEmptyView() {
        super.showEmptyView();
        mDataContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int generateLayoutID() {
        return 0;
    }

    protected void onRefresh() {

    }

    protected void onLoadMore() {

    }

    @Override
    public void initView(View view) {
        super.initView(view);
        mPullToRefreshRecyclerView.setPullRefreshEnabled(false);
        mPullToRefreshRecyclerView.setVisibility(View.GONE);
        mPullToRefreshRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mPullToRefreshRecyclerView.setPullToRefreshListener(new PullToRefreshListener() {
            @Override
            public void onRefresh() {
                PullListFragment.this.onRefresh();
            }

            @Override
            public void onLoadMore() {
                PullListFragment.this.onLoadMore();
            }
        });
    }
}
