package com.wangxiaobao.gsj.home;

import android.content.Context;
import android.support.annotation.NonNull;
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

public class ImageListAdapter extends BaseAdapter<String, ImageListAdapter.ViewHolder> {


    public ImageListAdapter(Context context) {
        super(context);
    }


    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent,R.layout.item_image);
    }

    public class ViewHolder extends DataRecyclerViewHolder<String> {
        @BindView(R.id.image)
        ImageView mImageView;

        @BindView(R.id.text)
        TextView text;

        public ViewHolder(@NonNull ViewGroup parent, int layoutResId) {
            super(parent, layoutResId);
        }


        @Override
        public void showViewContent(String s) {
            super.showViewContent(s);
            Glide.with(mContext).load(s).into(mImageView);

//            text.setText(s);
        }
    }


}
