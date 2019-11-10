package com.wangxiaobao.gsj.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hedgehog.ratingbar.RatingBar;
import com.wangxiaobao.gsj.adapter.BaseAdapter;
import com.wangxiaobao.gsj.adapter.DataRecyclerViewHolder;
import com.wangxiaobao.gsj.base.TimeUtils;
import com.wangxiaobao.gsj.common.CommonUtil;
import com.wangxiaobao.gsj.common.LogTool;
import com.wangxiaobao.gsj.enity.ComplainListModel;
import com.wangxiaobao.gsj.util.AudioManager;
import com.wangxiaobao.gsj.util.GlideUtil;
import com.wangxiaobao.waiter.R;

import java.util.Calendar;

import butterknife.BindView;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by candy on 18-3-7.
 */

public class ComplainListAdapter extends BaseAdapter<ComplainListModel, ComplainListAdapter.ViewHolder> {


    public ComplainListAdapter(Context context) {
        super(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_complain);
    }


    public String getTime(long time) {

        return TimeUtils.getTime(time);


    }


    public class ViewHolder extends DataRecyclerViewHolder<ComplainListModel> {

        @BindView(R.id.iv_icon)
        ImageView iv_icon;
        @BindView(R.id.tv_user_name)
        TextView tv_user_name;
        @BindView(R.id.tv_merchant_name)
        TextView tv_merchant_name;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.phone_number_list)
        TextView phone_number_list;
        @BindView(R.id.tv_reply)
        TextView tv_reply;
        @BindView(R.id.tv_reply_date)
        TextView tv_reply_date;
        @BindView(R.id.ll_container)
        LinearLayout ll_container;
        @BindView(R.id.rateBar)
        RatingBar rateBar;
        @BindView(R.id.comment)
        TextView comment;

        @BindView(R.id.group_merchant)
        Group groupMerchant;


        public ViewHolder(@NonNull ViewGroup parent, int layoutResId) {
            super(parent, layoutResId);
        }


        @Override
        public void showViewContent(ComplainListModel complain) {
            super.showViewContent(complain);

            tv_user_name.setText(complain.getUserName());
            tv_merchant_name.setText(complain.getMerchantName());

            //替换手机号中间4位为*
            String phoneNumber = null;
            if (TextUtils.isEmpty(complain.getUserMobile())) {
                phoneNumber = "暂无手机号";
            } else {
                phoneNumber = complain.getUserMobile().substring(0, 3) + "****" + complain.getUserMobile().substring(7, 11);
            }

            phone_number_list.setText(phoneNumber);


            if (TextUtils.isEmpty(complain.getHeadUrl())) {
                iv_icon.setImageResource(R.drawable.complain_user_icon);
            } else {
                GlideUtil.displayRoundImg(iv_icon, complain.getHeadUrl());

            }

            //转换时间戳为00月00日格式
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(complain.getComplaintDate());

            if (TextUtils.isEmpty(complain.getMerchantReply())) {
                tv_reply.setHint("商家暂未回应~");
                tv_reply_date.setVisibility(View.GONE);
            } else {
                tv_reply_date.setVisibility(View.VISIBLE);
                tv_reply.setText(complain.getMerchantReply());
                tv_reply_date.setText(getTime(complain.getReplyDate()));

            }

            if (complain.getUserStar()==0) {
                groupMerchant.setVisibility(View.GONE);
            }else {
                groupMerchant.setVisibility(View.VISIBLE);
                rateBar.setStar(complain.getUserStar());
            }

            complain.setComplaintDate(calendar.getTimeInMillis());
            time.setText(getTime(complain.getComplaintDate()));

            ll_container.removeAllViews();
            View v = null;

            if (!TextUtils.isEmpty(complain.getVoiceUrl())) {
                //语音
                v = LayoutInflater.from(context).inflate(R.layout.item_voice_turn_layout, null);
                GifImageView voice_img = v.findViewById(R.id.iv_voice_icon);
                TextView content = v.findViewById(R.id.tv_turn_text);
                content.setText(complain.getContent());
                voice_img.setOnClickListener(v1 -> {
                    if (AudioManager.INSTANCE.isPlaying()) {
                        AudioManager.INSTANCE.finishPlay();
                    }
                    if (TextUtils.isEmpty(complain.getVoiceUrl())) {
                        CommonUtil.showShortToast("播放语音失败");
                        return;
                    }

                    voice_img.setImageResource(R.drawable.gif_voice_play_list);
                    LogTool.saveLog("播放音频");

                    GifDrawable gifDrawable = (GifDrawable) voice_img.getDrawable();
                    gifDrawable.start();
                    AudioManager.INSTANCE.playOnlineAudio(complain.getVoiceUrl());
                    AudioManager.INSTANCE.setOnCompletionListener(() -> gifDrawable.stop());
                });
            } else {
                //纯文字
                v = LayoutInflater.from(context).inflate(R.layout.item_normal_text_layout, null);
                TextView content = (TextView) v.findViewById(R.id.tv_text);
                content.setText(complain.getContent());
            }
            ll_container.addView(v);
        }


    }
}
