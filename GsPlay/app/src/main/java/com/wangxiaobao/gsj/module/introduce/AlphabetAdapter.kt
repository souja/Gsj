package com.wangxiaobao.gsj.module.introduce

import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView
import com.wangxiaobao.gsj.adapter.BaseRecyclerAdapter
import com.wangxiaobao.gsj.adapter.DataRecyclerViewHolder
import com.wangxiaobao.gsj.enity.AlphabetModel
import com.wangxiaobao.waiter.R

/**
 * Created by ijays on 2018/8/15.
 */
class AlphabetAdapter(dataList: List<AlphabetModel>) : BaseRecyclerAdapter<AlphabetAdapter.AlphabetVH,
        AlphabetModel>(dataList) {
    override fun onRealCreateViewHolder(parent: ViewGroup?, viewType: Int): AlphabetVH {
        return AlphabetVH(parent!!, R.layout.item_alphabet)
    }

    override fun onRealBindViewHolder(viewHolder: AlphabetVH?, position: Int) {
        super.onRealBindViewHolder(viewHolder, position)
        viewHolder?.showViewContent(getItem(position), position)
    }

    class AlphabetVH(parent: ViewGroup, layoutId: Int) : DataRecyclerViewHolder<AlphabetModel>(parent, layoutId) {
        private var tvAlphabet = itemView?.findViewById<TextView>(R.id.tv_alpha)
        override fun showViewContent(t: AlphabetModel?, position: Int) {
            t?.let {
                if (it.isSelected) {
                    tvAlphabet?.setTextColor(ContextCompat.getColor(context, R.color.shop_alphabet_chosen))
//                    tvAlphabet?.textSize = DensityTools.sp2px(App.getContext(), 18).toFloat()
                    tvAlphabet?.setTextSize(TypedValue.COMPLEX_UNIT_PX, 33f)
                } else {
                    tvAlphabet?.setTextColor(ContextCompat.getColor(context, R.color.alphabet_color))
//                    tvAlphabet?.textSize = DensityTools.sp2px(App.getContext(), 14).toFloat()
                    tvAlphabet?.setTextSize(TypedValue.COMPLEX_UNIT_PX, 24f)
                }
                tvAlphabet?.text = t.alphabet
            }
        }
    }
}