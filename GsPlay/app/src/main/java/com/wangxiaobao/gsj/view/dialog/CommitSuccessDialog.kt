package com.wangxiaobao.gsj.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.wangxiaobao.gsj.common.DensityTools
import com.wangxiaobao.waiter.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by ijays on 2018/8/23.
 */
class CommitSuccessDialog : BaseDialogFragment() {

    private var listener: OnDismissListener? = null

    private var disposable: Disposable? = null
    override fun getLayoutId() = R.layout.dialog_commit_success

    override fun init(inflater: LayoutInflater?, rootView: View?, savedInstanceState: Bundle?) {
        super.init(inflater, rootView, savedInstanceState)

        val textView = rootView?.findViewById<TextView>(R.id.tv_count_down)

        disposable = Observable.intervalRange(1, 4, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {

                    val time = 4 - it.toInt()
                    textView?.text = "${time}秒后自动关闭"
                    if (time == 0) {
                        listener?.onDismiss()
                        this.dismiss()
                    }
                }
    }

    override fun onResume() {
        super.onResume()

        val screenWidth = DensityTools.getScreenWidth(context)
        val windowWidth = screenWidth - 2 * resources.getDimensionPixelOffset(R.dimen.dialog_margin)
        dialog.window!!.setLayout(windowWidth, WindowManager.LayoutParams.MATCH_PARENT)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))//设置该属性才能全屏
    }

    override fun onDestroy() {
        super.onDestroy()
        listener?.onDismiss()
    }

    fun setOnDismissListener(listener: OnDismissListener) {
        this.listener = listener
    }

    interface OnDismissListener {
        fun onDismiss()
    }

}