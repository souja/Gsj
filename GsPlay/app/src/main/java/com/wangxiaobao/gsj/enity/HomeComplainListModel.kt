package com.wangxiaobao.gsj.enity

import java.io.Serializable

/**
 * 首页投诉的滚动模型
 * Created by ijays on 2018/8/21.
 */
data class HomeComplainListModel(
        //投诉日期
        var complaintDate: Long,
        var content: String?) : Serializable