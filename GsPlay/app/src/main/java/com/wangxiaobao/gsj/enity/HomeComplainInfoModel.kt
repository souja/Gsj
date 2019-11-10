package com.wangxiaobao.gsj.enity

import java.io.Serializable

/**
 * Created by ijays on 2018/8/20.
 */
data class HomeComplainInfoModel(val male: Int,
                                 val female: Int,
                                 val malePercent: Int,
                                 val femalePercent: Int,
                                 val complaintNumber: Int,
                                 val complaintScore: String) : Serializable