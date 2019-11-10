package com.wangxiaobao.gsj.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class CommentListAdapter extends BaseAdapter<Comment, CommentListAdapter.ViewHolder> {


    public CommentListAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_comment);
    }


    public String getTime(long time) {
        return TimeUtils.getTime(time);
    }


    public class ViewHolder extends DataRecyclerViewHolder<Comment> {

        private void setStart(int k) {
            k--;
            for (int kk = 0; kk < starLayout.getChildCount(); kk++) {
                if (kk <= k) {
                    starLayout.getChildAt(kk).setSelected(true);
                } else {
                    starLayout.getChildAt(kk).setSelected(false);

                }
            }
        }

        @BindView(R.id.layout_star)
        LinearLayout starLayout;

        @BindView(R.id.commentInfo)

        TextView commentInfo;
        @BindView(R.id.merchantResponse)

        TextView merchantResponse;
        @BindView(R.id.userNickname)

        TextView userNickname;

        @BindView(R.id.userImage)
        ImageView userImage;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.response_time)
        TextView responseTime;
        @BindView(R.id.layout_merchant)
        View merchantLayout;
        @BindView(R.id.merchant_response_layout)
        View merchantResponseView;

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

//            SimpleDateFormat sp = new SimpleDateFormat("MM月dd日");


            time.setText(getTime(comment.getCreateDate()));

            responseTime.setText(getTime(comment.getResponseDate()));

            commentInfo.setText(comment.getCommentInfo());


            if (TextUtils.isEmpty(comment.getMerchantResponse())) {
                merchantLayout.setVisibility(View.GONE);
            } else {
                merchantResponse.setText(comment.getMerchantResponse());
                merchantLayout.setVisibility(View.VISIBLE);
            }





            if (!TextUtils.isEmpty(comment.getStar())) {
                merchantResponseView.setVisibility(View.VISIBLE);
                int starCount = Integer.parseInt(comment.getStar());
                if (starCount == 0) {
                    starLayout.setVisibility(View.GONE);
                } else {
                    starLayout.setVisibility(View.VISIBLE);
                    setStart(starCount);
                }
            } else {
                merchantResponseView.setVisibility(View.GONE);
                starLayout.setVisibility(View.GONE);
            }


        }
    }
}
