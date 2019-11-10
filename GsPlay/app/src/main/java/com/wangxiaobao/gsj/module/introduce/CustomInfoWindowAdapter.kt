package com.wangxiaobao.gsj.module.introduce

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.model.Marker
import com.wangxiaobao.waiter.R

/**
 * Created by ijays on 2018/8/16.
 */
class CustomInfoWindowAdapter(val context: Context) : AMap.InfoWindowAdapter {
    override fun getInfoWindow(p0: Marker?): View {
        val v = LayoutInflater.from(context).inflate(R.layout.custom_info_window_layout, null)
        v.findViewById<TextView>(R.id.tv_name)?.text = p0?.title
        return v
    }

    override fun getInfoContents(p0: Marker?): View? {
        return null
    }


}