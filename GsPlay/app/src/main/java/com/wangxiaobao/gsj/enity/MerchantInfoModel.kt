package com.wangxiaobao.gsj.enity

import java.io.Serializable

/**
 * 商家介绍界面
 * Created by ijays on 2018/8/23.
 */
data class MerchantInfoModel(val merchantName: String,
                             val merchantId: String,
                             val merchantAccount: String,
                             val sn: String,
                             val introduction: String?,
                             val images: String?,
                             val address: String?) : Serializable