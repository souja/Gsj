package com.wangxiaobao.gsj.view.dialog

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.wangxiaobao.gsj.common.CommonUtil
import com.wangxiaobao.gsj.common.LogTool
import com.wangxiaobao.gsj.view.StrokeImageView
import com.wangxiaobao.waiter.R

/**
 * Created by ijays on 2018/8/14.
 */
class RecordDialogFragment : BaseDialogFragment(), View.OnClickListener {
    private var title = ""

    /**
     * 中间显示的图标
     */
    private var voiceImg: ImageView? = null

    private var recordImgView: StrokeImageView? = null

    private var listener: OnUploadAudioListener? = null

    private var titleTextView: TextView? = null


    override fun getLayoutId() = R.layout.dialog_record

    override fun init(inflater: LayoutInflater?, rootView: View?, savedInstanceState: Bundle?) {

        titleTextView = rootView?.findViewById(R.id.tv_title)
        rootView?.findViewById<ImageView>(R.id.iv_close)?.setOnClickListener(this)
        recordImgView = rootView?.findViewById(R.id.iv_img)
        voiceImg = rootView?.findViewById(R.id.iv_img)
        voiceImg?.setOnClickListener(this)

        titleTextView?.text = title

        recordImgView?.setOnRecordListener(object : StrokeImageView.OnRecordListener {
            override fun onRecordFinish(filePath: String, timeDiff: Int) {

                if (timeDiff < 3000) {
                    CommonUtil.showShortToast("录音少于3s，请重新录音")
                    return

                }
                Log.e("SONGJIE", "FILE===path--$filePath")
                dismiss()

                if (!TextUtils.isEmpty(filePath)) {
                    listener?.onUploadAudio(filePath = filePath)
                } else {
                    CommonUtil.showShortToast("录音错误")
                    LogTool.saveLog("上传音频时路径为空")
                }
            }

            override fun onRecordStart() {
                titleTextView?.text = "松开 结束"

            }
        })


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_close -> {
                this.dismiss()
            }
        }
    }

    fun setTitle(title: String): RecordDialogFragment {
        this.title = title
        return this
    }

    fun setOnUploadAudioListener(listener: OnUploadAudioListener) {
        this.listener = listener
    }


    interface OnUploadAudioListener {
        fun onUploadAudio(filePath: String)
    }


}
