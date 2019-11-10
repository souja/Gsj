package com.wangxiaobao.gsj.enity

import com.wangxiaobao.gsj.util.helper.BaseEntity
import java.io.Serializable

/**
 * Created by ijays on 2018/8/15.
 */
data class MapMerchantModel(var merchantName: String,
                            var merchantId: String,
                            var tradeAreaName: String,
                            var businessCircleId: Int,
                            var isSelected: Boolean) : Serializable, BaseEntity {

    override fun getIndexField() = merchantName
}