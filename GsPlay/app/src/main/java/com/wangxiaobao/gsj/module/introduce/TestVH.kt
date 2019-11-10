package com.wangxiaobao.gsj.module.introduce

import android.view.ViewGroup
import android.widget.TextView
import com.wangxiaobao.gsj.adapter.DataRecyclerViewHolder
import com.wangxiaobao.gsj.base.decorate.BaseViewHolder
import com.wangxiaobao.gsj.enity.AlphabetModel
import com.wangxiaobao.waiter.R

/**
 * Created by ijays on 2018/8/15.
 */
class TestVH(parent: ViewGroup, layoutId: Int) : BaseViewHolder<AlphabetModel>(parent, layoutId) {
    private var tvAlphabet = itemView?.findViewById<TextView>(R.id.tv_alpha)
    override fun show(t: AlphabetModel?, position: Int) {
        t?.let {
            tvAlphabet?.text = t.alphabet
        }
    }
}