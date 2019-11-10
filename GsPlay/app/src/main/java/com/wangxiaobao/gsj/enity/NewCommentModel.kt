package com.wangxiaobao.gsj.enity

import com.wangxiaobao.gsj.base.decorate.TypeFactory
import com.wangxiaobao.gsj.base.decorate.Visitable
import java.io.Serializable

/**
 * Created by ijays on 2018/8/14.
 */
data class NewCommentModel(val isValidate: String,
                           val createUser: String,
                           val createDate: Long,
                           val updateUser: String,
                           val updateDate: Long,
                           val commentId: Int,
                           val merchantId: String,
                           val merchantName: String,
                           val userId: Int,
                           val userHeadImg: String,
                           val userName:String,
                           val content: String,
                           val voiceUrl: String,
                           val merchantReplyDate: Long?,
                           val merchantReply: String?,
                           val userStar: Int,
                           val feedbackAic: String?,
                           val userStarDate: String) : Serializable, Visitable {
    override fun type(factory: TypeFactory?): Int {
        return factory?.type(this) ?: 0
    }


}