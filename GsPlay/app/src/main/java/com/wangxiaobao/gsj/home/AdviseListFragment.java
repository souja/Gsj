package com.wangxiaobao.gsj.home;

import android.view.View;

import com.wangxiaobao.waiter.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.OnClick;

/**
 * Created by candy on 18-3-7.
 */

public class AdviseListFragment extends PullListFragment {

    private AdviseListAdapter mCommentListAdapter;

    @Override
    public void initView(View view) {
        super.initView(view);

        mCommentListAdapter = new AdviseListAdapter(mContext);
        mPullToRefreshRecyclerView.setAdapter(mCommentListAdapter);
        mPullToRefreshRecyclerView.setPullRefreshEnabled(false);
        mMainActivity.setActionBarTitle("用户意见");
        mMainActivity.setEnglishTitle("Opinion");


//        mMainActivity.showSjx(R.id.advise_sjx);
    }


    @Override
    public int generateLayoutID() {
        return R.layout.fragment_advisement;
    }


    @OnClick(R.id.add_advisement)
    public void addAdvisement() {
//        Comment comment = new Comment();
//        comment.setMerchantId(CommonUtil.getMerchantID(mContext));
//        comment.setUserNickname("xxx");
//        comment.setAdvise("意见少放点辣椒");
//        comment.setUserId(CommonUtil.getMerchantAccount(mContext));
//        comment.setMerchantName(CommonUtil.getMerchantName(mContext));

//        mMainActivity.showAdvisementDetailFragment();

//        ApiManager.getInstance().addAdvisement(comment);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bindCommentList(AdviseListResponse adviseListResponse) {
        mPullToRefreshRecyclerView.setVisibility(View.VISIBLE);
        mCommentListAdapter.addAll(adviseListResponse.getDatas());

        mPullToRefreshRecyclerView.setLoadMoreComplete();

        if (mCommentListAdapter.getDatas().size() == adviseListResponse.getCount()) {
            mPullToRefreshRecyclerView.setLoadingMoreEnabled(false);
        }


    }


    @Override
    protected void onLoadMore() {
        super.onLoadMore();
        loadAdvisementList();

    }


    @Override
    public void initData() {
        super.initData();
        loadAdvisementList();
    }

    private void loadAdvisementList() {
        mPageNum++;
        ApiManager.getInstance().getAdvisementList(mPageNum, mPageSize, mContext);

    }
}
