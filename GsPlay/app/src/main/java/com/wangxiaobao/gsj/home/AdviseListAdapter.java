package com.wangxiaobao.gsj.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wangxiaobao.gsj.adapter.BaseAdapter;
import com.wangxiaobao.gsj.adapter.DataRecyclerViewHolder;
import com.wangxiaobao.gsj.base.TimeUtils;
import com.wangxiaobao.waiter.R;

import butterknife.BindView;

/**
 * Created by candy on 18-3-7.
 */

public class AdviseListAdapter extends BaseAdapter<Comment, AdviseListAdapter.ViewHolder> {


    public AdviseListAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_advise);
    }


    public String getTime(long time) {

        return TimeUtils.getTime(time);
    }


    public class ViewHolder extends DataRecyclerViewHolder<Comment> {
        @BindView(R.id.advise)

        TextView advise;

        @BindView(R.id.userNickname)

        TextView userNickname;

        @BindView(R.id.userImage)
        ImageView userImage;
        @BindView(R.id.time)
        TextView time;

        public ViewHolder(@NonNull ViewGroup parent, int layoutResId) {
            super(parent, layoutResId);
        }


        @Override
        public void showViewContent(Comment comment) {
            super.showViewContent(comment);

            userNickname.setText(comment.getUserNickname());

            if (TextUtils.isEmpty(comment.getUserImage())) {
                userImage.setImageResource(R.drawable.header_final);

            } else {
                Glide.with(mContext).load(comment.getUserImage()).into(userImage);
            }


            time.setText(getTime(comment.getCreateDate()));

            advise.setText(comment.getAdvise());


        }
    }
}
