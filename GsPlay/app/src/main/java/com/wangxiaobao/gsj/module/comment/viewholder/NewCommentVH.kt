package com.wangxiaobao.gsj.module.comment.viewholder

import android.support.constraint.Group
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.hedgehog.ratingbar.RatingBar
import com.wangxiaobao.gsj.base.TimeUtils
import com.wangxiaobao.gsj.base.decorate.BaseViewHolder
import com.wangxiaobao.gsj.common.CommonUtil
import com.wangxiaobao.gsj.common.LogTool
import com.wangxiaobao.gsj.enity.NewCommentModel
import com.wangxiaobao.gsj.util.AudioManager
import com.wangxiaobao.gsj.util.GlideUtil
import com.wangxiaobao.gsj.util.UnicodeUtils
import com.wangxiaobao.waiter.R
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView

/**
 * Created by ijays on 2018/8/14.
 */
class NewCommentVH(parent: ViewGroup, layoutId: Int) : BaseViewHolder<NewCommentModel>(parent, layoutId) {

    private var llContainer: LinearLayout = itemView.findViewById(R.id.ll_container)

    private var tvTime: TextView = itemView.findViewById(R.id.time)
    private var tvUserName: TextView = itemView.findViewById(R.id.tv_user_name)
    private var tvMerchantName: TextView = itemView.findViewById(R.id.tv_merchant_name)
    private var ivIcon: ImageView = itemView.findViewById(R.id.iv_icon)

    private var tvReply: TextView = itemView.findViewById(R.id.tv_reply)
    private var tvReplyDate: TextView = itemView.findViewById(R.id.tv_reply_date)

    private var groupMerchant: Group = itemView.findViewById(R.id.group_merchant)

    private var rateBar: RatingBar = itemView.findViewById(R.id.rateBar)


    override fun show(t: NewCommentModel?, position: Int) {
        t?.let {

            tvUserName.text = UnicodeUtils.decode(t.userName)
            tvMerchantName.text = t.merchantName

            if (!TextUtils.isEmpty(t.userHeadImg)) {
                GlideUtil.displayRoundImg(ivIcon, t.userHeadImg)
            } else {
                ivIcon.setImageResource(R.drawable.complain_user_icon)
            }


            tvTime.text = TimeUtils.getTime(t.createDate)
            if (TextUtils.isEmpty(t.merchantReply)) {
                groupMerchant.visibility = View.GONE
            } else {
                groupMerchant.visibility = View.VISIBLE
                tvReply.text = t.merchantReply ?: ""
                tvReplyDate.text = TimeUtils.getTime(t.merchantReplyDate ?: 0)
            }

            rateBar.setStar(t.userStar.toFloat())


            llContainer.removeAllViews()
            val v: View
            if (!TextUtils.isEmpty(t.voiceUrl)) {
//                语音
                v = LayoutInflater.from(context).inflate(R.layout.item_voice_turn_layout, null)
                v.findViewById<TextView>(R.id.tv_turn_text).text = t.content ?: ""
                val voiceIcon = v.findViewById<GifImageView>(R.id.iv_voice_icon)
                voiceIcon.setOnClickListener {

                    if (TextUtils.isEmpty(t.voiceUrl)) {
                        CommonUtil.showShortToast("播放语音失败")
                        return@setOnClickListener
                    }
                    if (AudioManager.isPlaying()) {
                        AudioManager.finishPlay()
                    }
                    voiceIcon.setImageResource(R.drawable.gif_voice_play_list)
                    LogTool.saveLog("播放音频")

                    val gifDrawable = voiceIcon.drawable as GifDrawable
                    gifDrawable.start()
                    AudioManager.playOnlineAudio(t.voiceUrl)
                    AudioManager.setOnCompletionListener(object : AudioManager.OnCompletionListener {
                        override fun onCompletion() {
                            gifDrawable.stop()
                        }
                    })
                }
            } else {
//                纯文字
                v = LayoutInflater.from(context).inflate(R.layout.item_normal_text_layout, null)
                v.findViewById<TextView>(R.id.tv_text).text = t.content ?: ""

            }
            llContainer.addView(v)
        }
    }

}