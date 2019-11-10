package com.wangxiaobao.gsj.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


import com.wangxiaobao.waiter.R;

import java.util.List;

/**
 * Created by ijays on 25/01/2018.
 */

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerVH> {

    private List<Object> mData;
    private RecyclerViewBanner.OnSwitchRvBannerListener mSwitchListener;

    public BannerAdapter(List<Object> dataList) {
        this.mData = dataList;
    }

    @Override
    public BannerVH onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_banner, parent, false);
//        return new RecyclerView.ViewHolder(view) {
//            public FrameLayout mContainer;
//        };
        return new BannerVH(view);
    }

    @Override
    public void onBindViewHolder(BannerVH holder, int position) {
        if (mSwitchListener != null) {
            mSwitchListener.switchBanner(position % mData.size(), holder.mContainer);
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size() < 2 ? mData.size() : Integer.MAX_VALUE;
    }

    public void setSwitchListener(RecyclerViewBanner.OnSwitchRvBannerListener listener) {
        this.mSwitchListener = listener;
    }

    public class BannerVH extends RecyclerView.ViewHolder {
        public FrameLayout mContainer;

        public BannerVH(View itemView) {
            super(itemView);

            mContainer = (FrameLayout) itemView.findViewById(R.id.fl_container);
        }
    }

}
