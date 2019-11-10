package com.wangxiaobao.gsj.enity

/**
 * Created by ijays on 2018/7/17.
 */
data class WrapModel<T>(val pageNo: Int,
                        val pageSize:Int,
                        var records:T?,
                        val nextPageNo:Int,
                        val count:Int)