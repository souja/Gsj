package com.wangxiaobao.gsj.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wangxiaobao.gsj.adapter.BaseAdapter;
import com.wangxiaobao.gsj.adapter.DataRecyclerViewHolder;
import com.wangxiaobao.waiter.R;

import butterknife.BindView;

/**
 * Created by candy on 18-3-6.
 */

public class HonorListAdapter extends BaseAdapter<String, HonorListAdapter.ViewHolder> {


    public HonorListAdapter(Context context) {
        super(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_honor);
    }

    public class ViewHolder extends DataRecyclerViewHolder<String> {

        @BindView(R.id.text)
        TextView textView;

        @BindView(R.id.image)
        ImageView image;

        public ViewHolder(@NonNull ViewGroup parent, int layoutResId) {
            super(parent, layoutResId);
        }

        @Override
        public void showViewContent(String honor) {
            super.showViewContent(honor);


            if (honor.contains("@IMG")) {
                String imageUrl = honor.replace("@IMG", "");
                Glide.with(mContext).load(imageUrl).into(image);
                textView.setVisibility(View.GONE);
                image.setVisibility(View.VISIBLE);
            } else {
                textView.setText(honor);
                textView.setVisibility(View.VISIBLE);
                image.setVisibility(View.GONE);
            }


        }
    }


}
