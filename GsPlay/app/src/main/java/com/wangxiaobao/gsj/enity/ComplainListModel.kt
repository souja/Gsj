package com.wangxiaobao.gsj.enity

import java.io.Serializable
import java.util.*

/**
 * Created by ijays on 2018/8/20.
 */
data class ComplainListModel (val userName: String,
                              val headUrl: String,
                              val merchantName: String,
                              val userMobile: String,
                              var complaintDate: Long,
                              val content: String,
                              val merchantReply:String,
                              val replyDate: Long,
                              val userStar: Int,
                              val voiceUrl: String?) : Serializable