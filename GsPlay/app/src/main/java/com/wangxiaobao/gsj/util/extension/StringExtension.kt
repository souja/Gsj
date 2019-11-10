package com.wangxiaobao.gsj.util.extension

import com.wangxiaobao.waiter.R

/**
 * Created by ijays on 2018/11/5.
 */
fun String.getHeadImgUrl(): Int {
    return when (this) {
        //春熙路
        "PKLPAX68KI" -> R.drawable.icon_chunxi_road
        //北街社区
        "28b28a2bc8a597d4" -> R.drawable.ic_top_bg1
        //街子古镇
        "05d817ef64bc17ab" -> R.drawable.ic_top_bg2
        //富森美
        else -> R.drawable.icon_fusenmei
    }
}

fun String.getDeviceType(): Int {

    return when (this) {
        "PKLPAX68KI" -> 0

        else -> 1
    }
}