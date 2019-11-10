package com.wangxiaobao.gsj.enity

import java.io.Serializable

/**
 * 大屏首页返回的评价信息
 * Created by ijays on 2018/8/21.
 */
data class HomeCommentModel(var higherStarNo: Int,
                            val mediumStarNo: Int,
                            val lowerStarNo: Int,
        //	当月评价数
                            val monthStarNo: Int,
        //评价评分
                            val evaluationScores: Float) : Serializable