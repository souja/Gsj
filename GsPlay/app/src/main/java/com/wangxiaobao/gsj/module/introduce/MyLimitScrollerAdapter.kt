package com.wangxiaobao.gsj.module.introduce

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.wangxiaobao.gsj.base.TimeUtils
import com.wangxiaobao.gsj.enity.HomeComplainListModel
import com.wangxiaobao.gsj.view.LimitScrollerView
import com.wangxiaobao.waiter.R

/**
 * Created by ijays on 2018/8/22.
 */
class MyLimitScrollerAdapter() : LimitScrollerView.LimitScrollAdapter {

    private var dataList = ArrayList<HomeComplainListModel>()

    private var limitScrollerView: LimitScrollerView? = null

    private var context: Context? = null

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getView(index: Int): View {
        val v = LayoutInflater.from(context).inflate(R.layout.item_home_complain_layout, null)
        val model = dataList[index]

        v.findViewById<TextView>(R.id.tv_time).text = TimeUtils.getDateToString(model.complaintDate, "yyyy-MM-dd HH:mm")
        v.findViewById<TextView>(R.id.tv_complain_msg).text = model.content ?: ""
        return v
    }

    fun setData(context: Context, list: List<HomeComplainListModel>) {
        this.dataList = list as ArrayList<HomeComplainListModel>
        this.context = context
        limitScrollerView?.startScroll()
    }

    fun setLimitScrollerView(limitView: LimitScrollerView) {
        this.limitScrollerView = limitView
    }

    fun getDataList(): MutableList<HomeComplainListModel> {
        return dataList
    }

}