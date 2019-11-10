package com.wangxiaobao.gsj.module.introduce

import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.wangxiaobao.gsj.adapter.BaseRecyclerAdapter
import com.wangxiaobao.gsj.adapter.DataRecyclerViewHolder
import com.wangxiaobao.gsj.enity.MapMerchantModel
import com.wangxiaobao.gsj.util.helper.IndexStickyEntity
import com.wangxiaobao.waiter.R

/**
 * Created by ijays on 2018/8/15.
 */
class MapShopAdapter(dataList: List<IndexStickyEntity<MapMerchantModel>>) :
        BaseRecyclerAdapter<MapShopAdapter.MapShopVH, IndexStickyEntity<MapMerchantModel>>(dataList) {

    /**
     * 索引值在列表中位置的映射关系
     */
    private var mIndexValuePositionMap: MutableMap<String, Int>? = null

    override fun onRealCreateViewHolder(parent: ViewGroup?, viewType: Int): MapShopVH {
        return MapShopVH(parent!!, R.layout.item_choose_shop_layout)
    }

    override fun onRealBindViewHolder(viewHolder: MapShopVH?, position: Int) {
        super.onRealBindViewHolder(viewHolder, position)
        viewHolder?.showViewContent(getItem(position), position)
    }

    fun setIndexValuePositionMap(map: MutableMap<String, Int>) {
        this.mIndexValuePositionMap = map
    }

    /**
     * 获取索引值在列表中的位置
     * @param indexValue
     * @return
     */
    fun getIndexValuePosition(indexValue: String): Int {

        return if (mIndexValuePositionMap?.containsKey(indexValue) == true) {
            return mIndexValuePositionMap?.get(indexValue) ?: 0
        } else -1
    }


    class MapShopVH(parent: ViewGroup, layoutId: Int) :
            DataRecyclerViewHolder<IndexStickyEntity<MapMerchantModel>>(parent, layoutId) {
        private val tvShopName = itemView.findViewById<TextView>(R.id.tv_merchant_name)

        override fun showViewContent(t: IndexStickyEntity<MapMerchantModel>?, position: Int) {
            t?.let {
                val mapMerchantModel = t.originalData

                if (mapMerchantModel == null) {
                    itemView.visibility = View.GONE
                } else {
                    itemView.visibility = View.VISIBLE

                    if (mapMerchantModel.isSelected) {
                        itemView.setBackgroundResource(R.drawable.purple_solid_with_radius)
                        tvShopName.setTextColor(ContextCompat.getColor(context, R.color.white))
                    } else {
                        itemView.setBackgroundResource(R.drawable.grey_solid_with_stroke_bg)
                        tvShopName.setTextColor(ContextCompat.getColor(context, R.color.font_black))

                    }
                    tvShopName.text = mapMerchantModel.merchantName
                }
            }

        }

    }
}
